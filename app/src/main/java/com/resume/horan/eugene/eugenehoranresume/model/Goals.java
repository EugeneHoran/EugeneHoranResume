
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Goals implements Parcelable {

    private String goalCurrent;
    private String goalFuture;


    /**
     * No args constructor for use in serialization
     */
    public Goals() {
    }

    /**
     * @param goalCurrent
     * @param goalFuture
     */
    public Goals(String goalCurrent, String goalFuture) {
        super();
        this.goalCurrent = goalCurrent;
        this.goalFuture = goalFuture;
    }

    public String getGoalCurrent() {
        return goalCurrent;
    }

    public void setGoalCurrent(String goalCurrent) {
        this.goalCurrent = goalCurrent;
    }

    public String getGoalFuture() {
        return goalFuture;
    }

    public void setGoalFuture(String goalFuture) {
        this.goalFuture = goalFuture;
    }

    /**
     * Parcel
     */
    public final static Creator<Goals> CREATOR = new Creator<Goals>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Goals createFromParcel(Parcel in) {
            return new Goals(in);
        }

        public Goals[] newArray(int size) {
            return (new Goals[size]);
        }

    };

    protected Goals(Parcel in) {
        this.goalCurrent = ((String) in.readValue((String.class.getClassLoader())));
        this.goalFuture = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(goalCurrent);
        dest.writeValue(goalFuture);
    }

    public int describeContents() {
        return 0;
    }

}
