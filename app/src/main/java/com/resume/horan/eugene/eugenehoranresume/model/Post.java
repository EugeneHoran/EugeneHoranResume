package com.resume.horan.eugene.eugenehoranresume.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.database.Exclude;

public class Post {
    private String uid;
    private String full_url;
    private String thumb_storage_uri;
    private String thumb_url;
    private String text;
    private Object timestamp;
    private String full_storage_uri;
    private int type;


    @Exclude
    private String key;

    public Post() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public Post(String uid, String full_url, String full_storage_uri, String thumb_url, String thumb_storage_uri, String text, Object timestamp, int type) {
        this.uid = uid;
        this.full_url = full_url;
        this.text = text;
        this.timestamp = timestamp;
        this.thumb_storage_uri = thumb_storage_uri;
        this.thumb_url = thumb_url;
        this.full_storage_uri = full_storage_uri;
        this.type = type;
    }

    @JsonIgnore
    @Exclude
    public String getKey() {
        return key;
    }

    @JsonIgnore
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFull_url() {
        return full_url;
    }

    public String getText() {
        return text;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getThumb_storage_uri() {
        return thumb_storage_uri;
    }

    @JsonProperty("thumb_url")
    public String getThumb_url() {
        return thumb_url;
    }

    public String getFull_storage_uri() {
        return full_storage_uri;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
