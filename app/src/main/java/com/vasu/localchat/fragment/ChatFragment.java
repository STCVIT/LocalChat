package com.vasu.localchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasu.localchat.R;
import com.vasu.localchat.adapter.ChatAdapter;
import com.vasu.localchat.model.MessageModel;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

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
        ArrayList<MessageModel> messagesList = new ArrayList<>();
        for (int i=0;i<20;i++) {
            messagesList.add(new MessageModel(message, i % 2 == 0 ? ChatAdapter.MESSAGE_TYPE_IN : ChatAdapter.MESSAGE_TYPE_OUT));
        }

        ChatAdapter chatAdapter= new ChatAdapter(getContext(),messagesList);

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);
        super.onViewCreated(view, savedInstanceState);
    }
}