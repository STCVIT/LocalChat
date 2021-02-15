package com.vasu.localchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vasu.localchat.R;
import com.vasu.localchat.activity.ClientActivity;


public class ClientInputFragment extends Fragment {

    EditText serverCodeEditText,usernameEditText;
    Button joinServerButton;
    ClientActivity clientActivity;

    public ClientInputFragment() {
        // Required empty public constructor
    }

//    void changeText(String str){
//        ((ClientActivity)getActivity()).name=str;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        joinServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code=serverCodeEditText.getText().toString();
                Log.i("code",code);
                if(serverCodeEditText.getText().toString().trim().length()>0 && usernameEditText.getText().toString().trim().length()>0){
                    clientActivity.startNSD(serverCodeEditText.getText().toString().trim());
                    clientActivity.name=usernameEditText.getText().toString().trim();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left).replace(R.id.acFragment,new ChatFragment()).commit();
                }else{
                    Toast.makeText(getContext(),"Empty input field",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void findViewById(View view){
        serverCodeEditText = view.findViewById(R.id.server_code_edit_text);
        usernameEditText = view.findViewById(R.id.username_edit_text);
        joinServerButton = view.findViewById(R.id.join_server_button);
        clientActivity = (ClientActivity) getActivity();
    }
}