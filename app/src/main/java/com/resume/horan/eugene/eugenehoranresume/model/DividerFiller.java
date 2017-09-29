package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DividerFiller implements Parcelable {
    private String fillerBreak = "break";

    public DividerFiller(String fillerBreak) {
        this.fillerBreak = fillerBreak;
    }

    public String getFillerBreak() {
        return fillerBreak;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fillerBreak);
    }

    protected DividerFiller(Parcel in) {
        this.fillerBreak = in.readString();
    }

    public static final Parcelable.Creator<DividerFiller> CREATOR = new Parcelable.Creator<DividerFiller>() {
        @Override
        public DividerFiller createFromParcel(Parcel source) {
            return new DividerFiller(source);
        }

        @Override
        public DividerFiller[] newArray(int size) {
            return new DividerFiller[size];
        }
    };
}
