
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Goals implements Parcelable {

    private String goal;

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goal);
    }

    public Goals() {
    }

    protected Goals(Parcel in) {
        this.goal = in.readString();
    }

    public static final Creator<Goals> CREATOR = new Creator<Goals>() {
        @Override
        public Goals createFromParcel(Parcel source) {
            return new Goals(source);
        }

        @Override
        public Goals[] newArray(int size) {
            return new Goals[size];
        }
    };
}
