package com.vasu.localchat.model;

import java.util.Date;

public class MessageModel {

    public String message;
    public int messageType;
    public String name;
    public Date messageTime = new Date();
    // Constructor
    public MessageModel(String message, int messageType,String name) {
        this.message = message;
        this.messageType = messageType;
        this.name = name;
    }

}
