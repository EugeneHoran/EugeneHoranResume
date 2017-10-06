package com.resume.horan.eugene.eugenehoranresume.model;


import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import com.resume.horan.eugene.eugenehoranresume.main.about.AboutRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeBaseObject;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeEducationRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeExperienceRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeSkillRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AboutObject extends BaseObservable implements Parcelable {
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
