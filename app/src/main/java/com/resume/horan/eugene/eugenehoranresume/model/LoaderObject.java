package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class LoaderObject implements Parcelable {
    private List<Object> objectList;
    boolean expanded;

    public LoaderObject(List<Object> objectList, boolean expanded) {
        this.objectList = objectList;
        this.expanded = expanded;
    }

    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.objectList);
        dest.writeByte(this.expanded ? (byte) 1 : (byte) 0);
    }

    protected LoaderObject(Parcel in) {
        this.objectList = new ArrayList<Object>();
        in.readList(this.objectList, Object.class.getClassLoader());
        this.expanded = in.readByte() != 0;
    }

    public static final Parcelable.Creator<LoaderObject> CREATOR = new Parcelable.Creator<LoaderObject>() {
        @Override
        public LoaderObject createFromParcel(Parcel source) {
            return new LoaderObject(source);
        }

        @Override
        public LoaderObject[] newArray(int size) {
            return new LoaderObject[size];
        }
    };
}
