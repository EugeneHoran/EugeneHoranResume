package com.resume.horan.eugene.eugenehoranresume.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Header implements Parcelable {
    private String headerTitle;

    public Header() {
    }

    public Header(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headerTitle);
    }

    protected Header(Parcel in) {
        this.headerTitle = in.readString();
    }

    public static final Parcelable.Creator<Header> CREATOR = new Parcelable.Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel source) {
            return new Header(source);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };
}
