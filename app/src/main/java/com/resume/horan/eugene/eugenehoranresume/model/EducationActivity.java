package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EducationActivity implements Parcelable {

    private String description;
    public EducationActivity() {
    }
    public EducationActivity(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
    }

    protected EducationActivity(Parcel in) {
        this.description = in.readString();
    }

    public static final Parcelable.Creator<EducationActivity> CREATOR = new Parcelable.Creator<EducationActivity>() {
        @Override
        public EducationActivity createFromParcel(Parcel source) {
            return new EducationActivity(source);
        }

        @Override
        public EducationActivity[] newArray(int size) {
            return new EducationActivity[size];
        }
    };
}
