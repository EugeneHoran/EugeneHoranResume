
package com.resume.horan.eugene.eugenehoranresume.model;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;

public class SocialMedia implements Parcelable {

    private String account;
    private String url;
    private Integer imageInt;
    private boolean visible;

    @BindingAdapter("load_image")
    public static void loadImage(TextView view, SocialMedia object) {
        view.setCompoundDrawablesWithIntrinsicBounds(object.getLogo(), 0, 0, 0);
    }


    public int getLogo() {
        switch (imageInt) {
            case 0:
                return R.drawable.ic_github;
            case 1:
                return R.drawable.ic_stackoverflow;
            case 2:
                return R.drawable.ic_google_plus;
            case 3:
                return R.drawable.ic_facebook;
            case 4:
                return R.drawable.ic_twitter;
            case 5:
                return R.drawable.ic_linkedin;
            default:
                return 0;
        }
    }


    /**
     * No args constructor for use in serialization
     */
    public SocialMedia() {
    }

    /**
     * @param visible
     * @param account
     * @param imageInt
     * @param url
     */

    public SocialMedia(String account, String url, Integer imageInt, boolean visible) {
        this.account = account;
        this.url = url;
        this.imageInt = imageInt;
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

    public Integer getImageInt() {
        return imageInt;
    }

    public void setImageInt(Integer imageInt) {
        this.imageInt = imageInt;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.account);
        dest.writeString(this.url);
        dest.writeValue(this.imageInt);
        dest.writeByte(this.visible ? (byte) 1 : (byte) 0);
    }

    protected SocialMedia(Parcel in) {
        this.account = in.readString();
        this.url = in.readString();
        this.imageInt = (Integer) in.readValue(Integer.class.getClassLoader());
        this.visible = in.readByte() != 0;
    }

    public static final Creator<SocialMedia> CREATOR = new Creator<SocialMedia>() {
        @Override
        public SocialMedia createFromParcel(Parcel source) {
            return new SocialMedia(source);
        }

        @Override
        public SocialMedia[] newArray(int size) {
            return new SocialMedia[size];
        }
    };
}
