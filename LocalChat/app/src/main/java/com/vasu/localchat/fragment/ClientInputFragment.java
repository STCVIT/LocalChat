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

import com.vasu.localchat.R;
import com.vasu.localchat.activity.ClientActivity;


public class ClientInputFragment extends Fragment {

    public ClientInputFragment() {
        // Required empty public constructor
    }

//    void changeText(String str){
//        ((ClientActivity)getActivity()).name=str;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Button button = getView().findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditText editText = getView().findViewById(R.id.input);
//                changeText(editText.getText().toString());
//                Log.i("tag",editText.getText().toString());
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.acFragment,new ClientChatFragment()).commit();
//            }
//        });
    }
}