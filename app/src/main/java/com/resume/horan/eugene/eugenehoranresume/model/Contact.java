
package com.resume.horan.eugene.eugenehoranresume.model;


public class Contact {

    private String phonePrimary;
    private String phoneSecondary;
    private String text;
    private String email;

    /**
     * No args constructor for use in serialization
     */
    public Contact() {
    }

    /**
     * @param text
     * @param phonePrimary
     * @param email
     * @param phoneSecondary
     */
    public Contact(String phonePrimary, String phoneSecondary, String text, String email) {
        super();
        this.phonePrimary = phonePrimary;
        this.phoneSecondary = phoneSecondary;
        this.text = text;
        this.email = email;
    }

    public String getPhonePrimary() {
        return phonePrimary;
    }

    public void setPhonePrimary(String phonePrimary) {
        this.phonePrimary = phonePrimary;
    }

    public String getPhoneSecondary() {
        return phoneSecondary;
    }

    public void setPhoneSecondary(String phoneSecondary) {
        this.phoneSecondary = phoneSecondary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
