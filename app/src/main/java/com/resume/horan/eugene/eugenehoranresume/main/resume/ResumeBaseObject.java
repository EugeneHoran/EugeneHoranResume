package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import com.resume.horan.eugene.eugenehoranresume.main.about.AboutRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResumeBaseObject implements Parcelable {

    private List<Object> objectList;

    public ResumeBaseObject(List<Object> objectList) {
        this.objectList = objectList;
    }

    @BindingAdapter("set_adapter")
    public static void setRecyclerAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        view.setAdapter(adapter);
    }

    @BindingAdapter("set_items")
    public static void setAdapterItems(RecyclerView recyclerView, ResumeBaseObject object) {
        if (recyclerView.getAdapter() != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter instanceof ResumeSkillRecyclerAdapter) {
                ResumeSkillRecyclerAdapter mAdapter = (ResumeSkillRecyclerAdapter) adapter;
                mAdapter.setItems(object.getObjectList());
            } else if (adapter instanceof ResumeEducationRecyclerAdapter) {
                ResumeEducationRecyclerAdapter mAdapter = (ResumeEducationRecyclerAdapter) adapter;
                mAdapter.setItems(object.getObjectList());
            } else if (adapter instanceof ResumeExperienceRecyclerAdapter) {
                ResumeExperienceRecyclerAdapter mAdapter = (ResumeExperienceRecyclerAdapter) adapter;
                mAdapter.setItems(object.getObjectList());
            } else if (adapter instanceof AboutRecyclerAdapter) {
                AboutRecyclerAdapter mAdapter = (AboutRecyclerAdapter) adapter;
                mAdapter.setItems(object.getObjectList());
            }
        }
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
        this.objectList = new ArrayList<>();
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
