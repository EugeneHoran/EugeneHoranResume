
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialMedia implements Parcelable {

    private String account;
    private String url;
    private String visible;

    /**
     * No args constructor for use in serialization
     */
    public SocialMedia() {
    }

    /**
     * @param visible
     * @param account
     * @param url
     */
    public SocialMedia(String account, String url, String visible) {
        super();
        this.account = account;
        this.url = url;
        this.visible = visible;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    /**
     * Parcel
     */
    public final static Creator<SocialMedia> CREATOR = new Creator<SocialMedia>() {
        @SuppressWarnings({
                "unchecked"
        })
        public SocialMedia createFromParcel(Parcel in) {
            return new SocialMedia(in);
        }

        public SocialMedia[] newArray(int size) {
            return (new SocialMedia[size]);
        }

    };

    protected SocialMedia(Parcel in) {
        this.account = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.visible = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(account);
        dest.writeValue(url);
        dest.writeValue(visible);
    }

    public int describeContents() {
        return 0;
    }

}
