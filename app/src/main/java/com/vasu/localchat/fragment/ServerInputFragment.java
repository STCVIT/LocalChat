package com.vasu.localchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vasu.localchat.R;
import com.vasu.localchat.activity.ServerActivity;

import java.io.IOException;

public class ServerInputFragment extends Fragment {
    TextView idTv;
    EditText userNameEditText;
    Button serverButton;

    ServerActivity serverActivity;


    public ServerInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        idTv.setText(serverActivity.charId);
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userNameEditText.getText().toString().trim().length()>0){
                    Log.i("fragment","here");
                    try {
                        serverActivity.mService.constructor(getContext(),"Test101");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.asFragment,new ChatFragment()).commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(getContext(),"Empty input field",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


   void findViewById(View view){
        idTv = view.findViewById(R.id.id_tv);
        userNameEditText = view.findViewById(R.id.username_edit_text);
        serverButton = view.findViewById(R.id.start_server_button);
        serverActivity = (ServerActivity) getActivity();
    }



}