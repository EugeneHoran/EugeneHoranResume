package com.resume.horan.eugene.eugenehoranresume.model;

public class User {
    public String email;
    public String displayName;
    public String imageUrl;

    public User() {
    }

    public User(String email, String displayName, String imageUrl) {
        this.email = email;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
