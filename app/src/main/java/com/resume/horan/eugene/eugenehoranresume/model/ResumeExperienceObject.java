package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ResumeExperienceObject implements Parcelable {
    private List<Object> mExperienceList;

    public ResumeExperienceObject() {
    }

    public ResumeExperienceObject(List<Object> experienceList) {
        this.mExperienceList = experienceList;
    }

    public List<Object> getExperienceList() {
        return mExperienceList;
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
        dest.writeList(this.mExperienceList);
    }

    protected ResumeExperienceObject(Parcel in) {
        this.mExperienceList = new ArrayList<Object>();
        in.readList(this.mExperienceList, Object.class.getClassLoader());
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
