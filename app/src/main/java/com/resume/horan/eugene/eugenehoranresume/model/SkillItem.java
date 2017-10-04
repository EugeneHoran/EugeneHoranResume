package com.resume.horan.eugene.eugenehoranresume.model;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;

public class SkillItem implements Parcelable {

    private String skillName;
    private String skillColorHEX;

    public SkillItem() {
    }

    public SkillItem(String skillName, String skillColorHEX) {
        this.skillName = skillName;
        this.skillColorHEX = skillColorHEX;
    }

    @BindingAdapter("background_tint")
    public static void loadBackground(TextView view, SkillItem skill) {
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.shape_chip_drawable);
        drawable.setColorFilter(skill.getChipColor(), PorterDuff.Mode.SRC_ATOP);
        view.setBackground(drawable);
    }

    public int getChipColor() {
        return Color.parseColor(skillColorHEX);
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

    public String getSkillName() {
        return skillName;
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
