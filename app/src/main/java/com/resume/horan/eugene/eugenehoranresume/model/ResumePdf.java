package com.resume.horan.eugene.eugenehoranresume.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ResumePdf implements Parcelable {
    public String fileLocation;

    public ResumePdf() {
    }

    public ResumePdf(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileLocation);
    }

    protected ResumePdf(Parcel in) {
        this.fileLocation = in.readString();
    }

    public static final Parcelable.Creator<ResumePdf> CREATOR = new Parcelable.Creator<ResumePdf>() {
        @Override
        public ResumePdf createFromParcel(Parcel source) {
            return new ResumePdf(source);
        }

        @Override
        public ResumePdf[] newArray(int size) {
            return new ResumePdf[size];
        }
    };
}
