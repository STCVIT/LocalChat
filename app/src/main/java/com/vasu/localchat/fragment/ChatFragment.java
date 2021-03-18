package com.vasu.localchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vasu.localchat.R;
import com.vasu.localchat.activity.ClientActivity;
import com.vasu.localchat.activity.ServerActivity;
import com.vasu.localchat.adapter.ChatAdapter;
import com.vasu.localchat.model.MessageModel;
import com.vasu.localchat.util.Methods;

import java.io.IOException;
import java.util.ArrayList;

public class ChatFragment extends Fragment {

  static  ChatAdapter chatAdapter;
    RecyclerView recyclerView;
    ImageButton buttonMsgSend;
    EditText messageEditText;
   static ArrayList<MessageModel> messagesList;

   static String userIdC;

   public static int connected=-1;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        messagesList = new ArrayList<>();

//        Checks if the activity that started the fragment is ClientActivity or not
        if(!getActivity().getLocalClassName().equals("activity.ServerActivity")) {
            new CountDownTimer(15000,1000) {
                @Override
                public void onTick(long l) {
                    if(connected!=-1){
//                        Starts the NSD Service
                    ((ClientActivity)getActivity()).nsdConstructor();
                    Log.i("chatFragment","started constructor");
                    cancel();
                }}
                @Override
                public void onFinish() {

                }
            }.start();
        }
        findViewById(view);
        initialize(getActivity().getLocalClassName());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);

//        Send Message Listener
        buttonMsgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("chatFragment",getActivity().getLocalClassName());
                String message = messageEditText.getText().toString();
                if(message.trim().length()>0){
//                    Checks if the user has started the server
                if(getActivity().getLocalClassName().equals("activity.ServerActivity")){
                    Methods.hideKeyboard((ServerActivity)getActivity());
//                    Calls the hash method and hashes the message
                    String hashedMessage = hash(message.trim(),getActivity().getLocalClassName());
                    try {
//                        Sends the message to all the clients connected to the server
                        ((ServerActivity) getActivity()).mService.sendToClients(hashedMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Methods.hideKeyboard((ClientActivity)getActivity());
                    String hashedMessage = hash(message.trim(),getActivity().getLocalClassName());
//                    Sends the message to the server
                    ((ClientActivity)getActivity()).sendMessage(hashedMessage);
                }
                messageEditText.setText("");
            }else{
                    Toast.makeText(getContext(),"Empty input field",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Keeps updating the recyclerview
        new CountDownTimer(Long.MAX_VALUE,2000) {
            @Override
            public void onTick(long l) {
              chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        super.onViewCreated(view, savedInstanceState);
    }

    void findViewById(View view){
        recyclerView = getView().findViewById(R.id.recycler_view);
        buttonMsgSend = view.findViewById(R.id.button_msg_send);
        messageEditText = view.findViewById(R.id.message_edit_text);
    }

    void initialize(String activity){

        if(activity.equals("activity.ServerActivity")){
            userIdC = ((ServerActivity)getActivity()).userId;
        }else{
            userIdC = ((ClientActivity)getActivity()).userId;
        }

        chatAdapter= new ChatAdapter(getContext(),messagesList);

    }

//    A custom hash method to hash the outgoing message
    String hash(String str,String activity){
        if(activity.equals("activity.ServerActivity")){
            str = str+"##%%"+((ServerActivity)getActivity()).userId+"##%%"+((ServerActivity)getActivity()).name;
        }else{
            str = str+"##%%"+((ClientActivity)getActivity()).userId+"##%%"+((ClientActivity)getActivity()).name;
        }
        return str;
    }


//    A custom unHash method to un-hash the incoming message
    static String[] unHash(String message){
        String[] split = message.split("##%%");
        return split;
    }

//    Adds the incoming message to the arrayList
    public static void addToList(String message){
       String[] msg = unHash(message);
       String mainMessage = msg[0];
       String userId = msg[1];
       String name = msg[2];

       if(userId.equals(userIdC)){
           messagesList.add(new MessageModel(mainMessage,2,name));
       }else{
           System.out.println("");
           messagesList.add(new MessageModel(mainMessage,1,name));
       }
    }

}