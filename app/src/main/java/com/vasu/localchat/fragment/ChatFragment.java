package com.vasu.localchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    final String message = "It is a long established fact that a reader will be distracted " +
            "by the readable content of a page when looking at its layout. " +
            "The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, " +
            "as opposed to using 'Content here, content here', making it look like readable English. " +
            " sometimes on purpose (injected humour and the like).";




    public ChatFragment() {
        // Required empty public constructor
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
//        for (int i=0;i<20;i++) {
//            messagesList.add(new MessageModel(message, i % 2 == 0 ? ChatAdapter.MESSAGE_TYPE_IN : ChatAdapter.MESSAGE_TYPE_OUT));
//        }

        if(!getActivity().getLocalClassName().equals("activity.ServerActivity")) {
            new CountDownTimer(30000,1000) {
                @Override
                public void onTick(long l) {
                    if(connected!=-1){
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
        buttonMsgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("chatFragment",getActivity().getLocalClassName());
                String message = messageEditText.getText().toString();
                if(message.trim().length()>0){
                if(getActivity().getLocalClassName().equals("activity.ServerActivity")){
                    String hashedMessage = hash(message.trim(),getActivity().getLocalClassName());
                    try {
                        ((ServerActivity) getActivity()).mService.sendToClients(hashedMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    String hashedMessage = hash(message.trim(),getActivity().getLocalClassName());
                    ((ClientActivity)getActivity()).sendMessage(hashedMessage);
                }
            }else{
                    Toast.makeText(getContext(),"Empty input field",Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    String hash(String str,String activity){
        if(activity.equals("activity.ServerActivity")){
            str = str+"##%%"+((ServerActivity)getActivity()).userId+"##%%"+((ServerActivity)getActivity()).name;
        }else{
            str = str+"##%%"+((ClientActivity)getActivity()).userId+"##%%"+((ClientActivity)getActivity()).name;
        }
        return str;
    }

    static String[] unHash(String message){
        String[] split = message.split("##%%");
        return split;
    }

    public static void addToList(String message){
       String[] msg = unHash(message);
       String mainMessage = msg[0];
       String userId = msg[1];
       String name = msg[2];

       if(userId.equals(userIdC)){
           messagesList.add(new MessageModel(mainMessage,2));
       }else{
           System.out.println("");
           messagesList.add(new MessageModel(mainMessage,1));
       }
    }
}