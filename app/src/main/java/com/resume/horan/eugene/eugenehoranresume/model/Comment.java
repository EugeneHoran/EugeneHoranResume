package com.resume.horan.eugene.eugenehoranresume.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.Exclude;

public class Comment {
    @JsonIgnore
    @Exclude
    private User users;
    private String uid;
    private String currentPostKey;
    private String text;
    private Object timestamp;

    public Comment() {
    }

    public Comment( String uid, String currentPostKey, String text, Object timestamp) {
        this.uid = uid;
        this.currentPostKey = currentPostKey;
        this.text = text;
        this.timestamp = timestamp;
    }

    @JsonIgnore
    @Exclude
    public User getUsers() {
        return users;
    }

    @JsonIgnore
    @Exclude
    public void setUsers(User users) {
        this.users = users;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrentPostKey() {
        return currentPostKey;
    }

    public void setCurrentPostKey(String currentPostKey) {
        this.currentPostKey = currentPostKey;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
