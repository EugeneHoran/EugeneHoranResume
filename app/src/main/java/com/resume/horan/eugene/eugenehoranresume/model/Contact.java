
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {

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


    /**
     * Parcel
     */
    public final static Creator<Contact> CREATOR = new Creator<Contact>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return (new Contact[size]);
        }

    };

    protected Contact(Parcel in) {
        this.phonePrimary = ((String) in.readValue((String.class.getClassLoader())));
        this.phoneSecondary = ((String) in.readValue((String.class.getClassLoader())));
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(phonePrimary);
        dest.writeValue(phoneSecondary);
        dest.writeValue(text);
        dest.writeValue(email);
    }

    public int describeContents() {
        return 0;
    }

}
