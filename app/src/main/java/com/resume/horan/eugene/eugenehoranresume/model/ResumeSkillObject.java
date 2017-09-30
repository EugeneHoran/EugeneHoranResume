package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class ResumeSkillObject implements Parcelable {

    private List<Object> skillObjectList;

    public ResumeSkillObject() {
    }

    public ResumeSkillObject(List<Object> skillObjectList) {
        this.skillObjectList = skillObjectList;
    }

    public List<Object> getSkillObjectList() {
        return skillObjectList;
    }

    public void setSkillObjectList(List<Object> skillObjectList) {
        this.skillObjectList = skillObjectList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.skillObjectList);
    }

    protected ResumeSkillObject(Parcel in) {
        this.skillObjectList = new ArrayList<Object>();
        in.readList(this.skillObjectList, Object.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResumeSkillObject> CREATOR = new Parcelable.Creator<ResumeSkillObject>() {
        @Override
        public ResumeSkillObject createFromParcel(Parcel source) {
            return new ResumeSkillObject(source);
        }

        @Override
        public ResumeSkillObject[] newArray(int size) {
            return new ResumeSkillObject[size];
        }
    };
}
