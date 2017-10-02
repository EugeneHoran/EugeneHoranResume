package com.resume.horan.eugene.eugenehoranresume.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AboutObject implements Parcelable {
    private List<Object> mObjectList = null;

    public AboutObject() {
    }

    public AboutObject(List<Object> mObjectList) {
        this.mObjectList = mObjectList;
    }

    public List<Object> getmObjectList() {
        return mObjectList;
    }

    public void setmObjectList(List<Object> mObjectList) {
        this.mObjectList = mObjectList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mObjectList);
    }

    protected AboutObject(Parcel in) {
        this.mObjectList = new ArrayList<Object>();
        in.readList(this.mObjectList, Object.class.getClassLoader());
    }

    public static final Parcelable.Creator<AboutObject> CREATOR = new Parcelable.Creator<AboutObject>() {
        @Override
        public AboutObject createFromParcel(Parcel source) {
            return new AboutObject(source);
        }

        @Override
        public AboutObject[] newArray(int size) {
            return new AboutObject[size];
        }
    };
}
