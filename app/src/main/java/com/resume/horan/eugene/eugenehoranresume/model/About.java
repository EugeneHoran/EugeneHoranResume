package com.resume.horan.eugene.eugenehoranresume.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeBaseObject;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

import java.util.ArrayList;
import java.util.List;

public class About implements Parcelable {
    private List<Goals> goals;
    private List<SocialMedia> socialMedia = null;
    private List<AlbumImage> albumImage = null;

    public ResumeBaseObject getFilteredAboutList() {
        List<Object> mObjectList = new ArrayList<>();
        mObjectList.add(new Header("Goals"));
        List<Object> mExpandGoalItems = new ArrayList<>();
        for (int i = 0; i < getGoals().size(); i++) {
            Goals gObject = getGoals().get(i);
            if (gObject.isVisible()) {
                mObjectList.add(gObject);
            } else {
                mExpandGoalItems.add(gObject);
            }
        }
        mObjectList.add(new DividerFiller(Common.DIVIDER_LINE_NO_SPACE));
        mObjectList.add(new LoaderObject(mExpandGoalItems, false));
        mObjectList.add(new Header("Social Media"));
        List<Object> mExpandSocialItems = new ArrayList<>();
        for (int i = 0; i < getSocialMedia().size(); i++) {
            SocialMedia smObject = getSocialMedia().get(i);
            if (smObject.getVisible()) {
                mObjectList.add(smObject);
            } else {
                mExpandSocialItems.add(smObject);
            }
        }
        mObjectList.add(new DividerFiller(Common.DIVIDER_LINE_NO_SPACE));
        mObjectList.add(new LoaderObject(mExpandSocialItems, false));
        mObjectList.add(new Header("Photos"));
        mObjectList.addAll(getAlbumImage());
        return new ResumeBaseObject(mObjectList);
    }


    public List<Goals> getGoals() {
        return goals;
    }

    public void setGoals(List<Goals> goals) {
        this.goals = goals;
    }

    public List<SocialMedia> getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(List<SocialMedia> socialMedia) {
        this.socialMedia = socialMedia;
    }

    public List<AlbumImage> getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(List<AlbumImage> albumImage) {
        this.albumImage = albumImage;
    }

    public About() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.goals);
        dest.writeTypedList(this.socialMedia);
        dest.writeTypedList(this.albumImage);
    }

    protected About(Parcel in) {
        this.goals = in.createTypedArrayList(Goals.CREATOR);
        this.socialMedia = in.createTypedArrayList(SocialMedia.CREATOR);
        this.albumImage = in.createTypedArrayList(AlbumImage.CREATOR);
    }

    public static final Creator<About> CREATOR = new Creator<About>() {
        @Override
        public About createFromParcel(Parcel source) {
            return new About(source);
        }

        @Override
        public About[] newArray(int size) {
            return new About[size];
        }
    };
}
