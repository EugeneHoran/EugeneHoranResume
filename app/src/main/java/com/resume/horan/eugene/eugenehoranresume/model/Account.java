package com.resume.horan.eugene.eugenehoranresume.model;

import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;

public class Account implements Parcelable {
    private String type;
    private String name;
    private String url;

    public Account() {
    }

    public Account(String type, String name, String url) {
        this.type = type;
        this.name = name;
        this.url = url;
    }

    @BindingAdapter("load_image")
    public static void loadImage(TextView view, Account object) {
        view.setCompoundDrawablesWithIntrinsicBounds(object.getImage(), 0, R.drawable.ic_chevron_right, 0);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImage() {
        switch (type) {
            case "0":
                return R.drawable.ic_stackoverflow;
            case "1":
                return R.drawable.ic_github;
            default:
                return 0;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.url);
    }

    protected Account(Parcel in) {
        this.type = in.readString();
        this.name = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
