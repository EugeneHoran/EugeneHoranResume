
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {

    private String phonePrimary;
    private String phoneSecondary;
    private String text;
    private String email;
    private Location location;

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

    public Contact(String phonePrimary, String phoneSecondary, String text, String email, Location location) {
        this.phonePrimary = phonePrimary;
        this.phoneSecondary = phoneSecondary;
        this.text = text;
        this.email = email;
        this.location = location;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phonePrimary);
        dest.writeString(this.phoneSecondary);
        dest.writeString(this.text);
        dest.writeString(this.email);
        dest.writeParcelable(this.location, flags);
    }

    protected Contact(Parcel in) {
        this.phonePrimary = in.readString();
        this.phoneSecondary = in.readString();
        this.text = in.readString();
        this.email = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
