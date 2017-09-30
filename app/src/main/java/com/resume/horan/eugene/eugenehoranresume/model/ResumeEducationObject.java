package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ResumeEducationObject implements Parcelable {
    private List<Object> mObjectList;

    public ResumeEducationObject() {
    }

    public ResumeEducationObject(List<Object> eductionList) {
        this.mObjectList = eductionList;
    }

    public List<Object> getEducationList() {
        return mObjectList;
    }

    /**
     * Parcel
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mObjectList);
    }

    protected ResumeEducationObject(Parcel in) {
        this.mObjectList = new ArrayList<Object>();
        in.readList(this.mObjectList, Object.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResumeExperienceObject> CREATOR = new Parcelable.Creator<ResumeExperienceObject>() {
        @Override
        public ResumeExperienceObject createFromParcel(Parcel source) {
            return new ResumeExperienceObject(source);
        }

        @Override
        public ResumeExperienceObject[] newArray(int size) {
            return new ResumeExperienceObject[size];
        }
    };
}
