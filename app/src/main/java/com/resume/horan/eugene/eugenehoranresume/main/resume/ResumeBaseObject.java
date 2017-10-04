package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ResumeBaseObject implements Parcelable {

    private List<Object> objectList;

    public ResumeBaseObject(List<Object> objectList) {
        this.objectList = objectList;
    }

    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.objectList);
    }

    protected ResumeBaseObject(Parcel in) {
        this.objectList = new ArrayList<Object>();
        in.readList(this.objectList, Object.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResumeBaseObject> CREATOR = new Parcelable.Creator<ResumeBaseObject>() {
        @Override
        public ResumeBaseObject createFromParcel(Parcel source) {
            return new ResumeBaseObject(source);
        }

        @Override
        public ResumeBaseObject[] newArray(int size) {
            return new ResumeBaseObject[size];
        }
    };
}
