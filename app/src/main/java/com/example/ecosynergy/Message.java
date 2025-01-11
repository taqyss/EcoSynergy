package com.example.ecosynergy;

public class Message {
    private String senderId;
    private String senderName;
    private String groupId;
    private String text;
    private long timestamp;

    public Message() {
        // Default constructor for Firebase
    }

    public Message(String senderId, String senderName, String groupId, String text, long timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.groupId = groupId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
