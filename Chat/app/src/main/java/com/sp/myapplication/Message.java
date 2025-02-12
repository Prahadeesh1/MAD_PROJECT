package com.sp.myapplication;

public class Message {
    private String messageId;
    private String senderId;
    private String message;

    public Message(String messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
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

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
