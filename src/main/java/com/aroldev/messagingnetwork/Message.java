package com.aroldev.messagingnetwork;

import com.aroldev.messagingnetwork.Enum.MessageType;

import java.text.SimpleDateFormat;

public class Message {
    private String content;
    private long timestamp;
    private MessageType type;
    private String sender;

    public Message(String content, long timestamp, MessageType type, String sender) {
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String formatDate () {
        SimpleDateFormat fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fDate.format(timestamp);
    }
}

