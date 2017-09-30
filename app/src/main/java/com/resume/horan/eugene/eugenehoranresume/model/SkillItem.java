package com.resume.horan.eugene.eugenehoranresume.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class SkillItem implements Parcelable {

    private String skillName;
    private String skillColorHEX;

    public SkillItem() {
    }

    public SkillItem(String skillName, String skillColorHEX) {
        this.skillName = skillName;
        this.skillColorHEX = skillColorHEX;
    }

    public int getChipColor() {
        return Color.parseColor(skillColorHEX);
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillColorHEX() {
        return skillColorHEX;
    }

    public void setSkillColorHEX(String skillColorHEX) {
        this.skillColorHEX = skillColorHEX;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.skillName);
        dest.writeString(this.skillColorHEX);
    }

    protected SkillItem(Parcel in) {
        this.skillName = in.readString();
        this.skillColorHEX = in.readString();
    }

    public static final Parcelable.Creator<SkillItem> CREATOR = new Parcelable.Creator<SkillItem>() {
        @Override
        public SkillItem createFromParcel(Parcel source) {
            return new SkillItem(source);
        }

        @Override
        public SkillItem[] newArray(int size) {
            return new SkillItem[size];
        }
    };
}
