package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class DividerFiller implements Parcelable {
    private String fillerBreak = "break";

    private boolean showLine;
    private boolean showSpace;


    public DividerFiller(String fillerBreak) {
        this.fillerBreak = fillerBreak;
    }

    public boolean getLineVisibility() {
        if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_NO_SPACE)) {
            showLine = true;
        } else if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_WITH_SPACE)) {
            showLine = true;
        } else if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_NO_LINE_WITH_SPACE)) {
            showLine = false;
        } else {
            showLine = true;
        }
        return showLine;
    }

    public boolean getSpaceVisibility() {
        if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_NO_SPACE)) {
            showSpace = false;
        } else if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_WITH_SPACE)) {
            showSpace = true;
        } else if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_NO_LINE_WITH_SPACE)) {
            showSpace = true;
        } else {
            showSpace = true;
        }
        return showSpace;
    }


    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    public boolean isShowSpace() {
        return showSpace;
    }

    public void setShowSpace(boolean showSpace) {
        this.showSpace = showSpace;
    }

    public String getFillerBreak() {
        return fillerBreak;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fillerBreak);
    }

    protected DividerFiller(Parcel in) {
        this.fillerBreak = in.readString();
    }

    public static final Parcelable.Creator<DividerFiller> CREATOR = new Parcelable.Creator<DividerFiller>() {
        @Override
        public DividerFiller createFromParcel(Parcel source) {
            return new DividerFiller(source);
        }

        @Override
        public DividerFiller[] newArray(int size) {
            return new DividerFiller[size];
        }
    };
}
