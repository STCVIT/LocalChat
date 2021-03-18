package com.vasu.localchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vasu.localchat.R;
import com.vasu.localchat.model.MessageModel;

import java.util.ArrayList;

// A typical adapter for RecyclerView
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    ArrayList<MessageModel> list = new ArrayList<>();
    public static final int MESSAGE_TYPE_IN = 1;
    public static final int MESSAGE_TYPE_OUT = 2;
    private final Context context;


    public ChatAdapter(Context context,ArrayList<MessageModel> list){
        this.list=list;
        this.context = context;
    }


    private class IncomingMessageViewHolder extends RecyclerView.ViewHolder{

       TextView incomingTextView;
       TextView incomingNameTextView;

        public IncomingMessageViewHolder(View itemView){
            super(itemView);
            incomingTextView = itemView.findViewById(R.id.incoming_tv);
            incomingNameTextView=itemView.findViewById(R.id.incoming_name_tv);
        }
        void bind(int position){
            MessageModel messageModel = list.get(position);
            incomingTextView.setText(messageModel.message);
            incomingNameTextView.setText(messageModel.name);
        }
    }

    private class OutgoingMessageViewHolder extends RecyclerView.ViewHolder{

        TextView outgoingTextView;

        public OutgoingMessageViewHolder(View itemView){
            super(itemView);
            outgoingTextView = itemView.findViewById(R.id.outgoing_tv);
        }
        void bind(int position){
            MessageModel messageModel = list.get(position);
            outgoingTextView.setText(messageModel.message);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MESSAGE_TYPE_IN){
            return new IncomingMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.incoming_message_layout,parent,false));
        }

        return new OutgoingMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.outgoing_message_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (list.get(position).messageType == MESSAGE_TYPE_IN) {
            ((IncomingMessageViewHolder) holder).bind(position);
        } else {
            ((OutgoingMessageViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).messageType;
    }

}
