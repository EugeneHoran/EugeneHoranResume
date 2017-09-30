package com.resume.horan.eugene.eugenehoranresume.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import com.resume.horan.eugene.eugenehoranresume.R;

import java.util.List;

public class Skill implements Parcelable {

    private String order;
    private String skillGroupName;
    private String skillColorHEX;
    private String skillColorName;
    private String textColorHEX;
    private String textColorName;
    private List<SkillItem> skillItem;

    public Skill() {
    }


    public int getTextColor() {
        return Color.parseColor(textColorHEX);
    }


    public Drawable getIconDrawable(Context context) {
        Drawable mDrawable = ContextCompat.getDrawable(context, getDrawable());
        mDrawable.mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(getTextColor(), PorterDuff.Mode.MULTIPLY));
        return mDrawable;
    }

    public int getDrawable() {
        switch (order) {
            case "0":
                return R.drawable.ic_devices;
            case "1":
                return R.drawable.ic_assignment;
            case "2":
                return R.drawable.ic_android;
            case "3":
                return R.drawable.ic_code;
            default:
                return 0;
        }
    }


    public Skill(String order, String skillGroupName, String skillColorHEX, String skillColorName, String textColorHEX, String textColorName, List<SkillItem> skillItem) {
        this.order = order;
        this.skillGroupName = skillGroupName;
        this.skillColorHEX = skillColorHEX;
        this.skillColorName = skillColorName;
        this.textColorHEX = textColorHEX;
        this.textColorName = textColorName;
        this.skillItem = skillItem;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSkillGroupName() {
        return skillGroupName;
    }

    public void setSkillGroupName(String skillGroupName) {
        this.skillGroupName = skillGroupName;
    }

    public String getSkillColorHEX() {
        return skillColorHEX;
    }

    public void setSkillColorHEX(String skillColorHEX) {
        this.skillColorHEX = skillColorHEX;
    }

    public String getSkillColorName() {
        return skillColorName;
    }

    public void setSkillColorName(String skillColorName) {
        this.skillColorName = skillColorName;
    }

    public String getTextColorHEX() {
        return textColorHEX;
    }

    public void setTextColorHEX(String textColorHEX) {
        this.textColorHEX = textColorHEX;
    }

    public String getTextColorName() {
        return textColorName;
    }

    public void setTextColorName(String textColorName) {
        this.textColorName = textColorName;
    }

    public List<SkillItem> getSkillItem() {
        return skillItem;
    }

    public void setSkillItem(List<SkillItem> skillItem) {
        this.skillItem = skillItem;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order);
        dest.writeString(this.skillGroupName);
        dest.writeString(this.skillColorHEX);
        dest.writeString(this.skillColorName);
        dest.writeString(this.textColorHEX);
        dest.writeString(this.textColorName);
        dest.writeTypedList(this.skillItem);
    }

    protected Skill(Parcel in) {
        this.order = in.readString();
        this.skillGroupName = in.readString();
        this.skillColorHEX = in.readString();
        this.skillColorName = in.readString();
        this.textColorHEX = in.readString();
        this.textColorName = in.readString();
        this.skillItem = in.createTypedArrayList(SkillItem.CREATOR);
    }

    public static final Parcelable.Creator<Skill> CREATOR = new Parcelable.Creator<Skill>() {
        @Override
        public Skill createFromParcel(Parcel source) {
            return new Skill(source);
        }

        @Override
        public Skill[] newArray(int size) {
            return new Skill[size];
        }
    };
}
