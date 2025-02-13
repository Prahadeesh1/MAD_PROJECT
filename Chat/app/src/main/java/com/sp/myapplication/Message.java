package com.sp.myapplication;

import java.util.Date;

public class Message {
    private String messageId;
    private String senderId;
    private String message;
    private long timestamp;

    public Message(String messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = new Date().getTime();

    }

    public Message() {
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {return timestamp;}

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {this.timestamp = timestamp;}
}
