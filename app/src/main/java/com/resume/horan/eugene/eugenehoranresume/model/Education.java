
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Education implements Parcelable {

    private String order;
    private String university;
    private String location;
    private String dateRange;
    private String major;
    private String logoUrl;
    private List<EducationActivity> educationActivity;

    public Education() {
    }

    public Education(String order, String university, String location, String dateRange, String major, String logoUrl, List<EducationActivity> educationActivity) {
        this.order = order;
        this.university = university;
        this.location = location;
        this.dateRange = dateRange;
        this.major = major;
        this.logoUrl = logoUrl;
        this.educationActivity = educationActivity;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<EducationActivity> getEducationActivity() {
        return educationActivity;
    }

    public void setEducationActivity(List<EducationActivity> educationActivity) {
        this.educationActivity = educationActivity;
    }

    /**
     * Parcel
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order);
        dest.writeString(this.university);
        dest.writeString(this.location);
        dest.writeString(this.dateRange);
        dest.writeString(this.major);
        dest.writeString(this.logoUrl);
        dest.writeTypedList(this.educationActivity);
    }

    protected Education(Parcel in) {
        this.order = in.readString();
        this.university = in.readString();
        this.location = in.readString();
        this.dateRange = in.readString();
        this.major = in.readString();
        this.logoUrl = in.readString();
        this.educationActivity = in.createTypedArrayList(EducationActivity.CREATOR);
    }

    public static final Creator<Education> CREATOR = new Creator<Education>() {
        @Override
        public Education createFromParcel(Parcel source) {
            return new Education(source);
        }

        @Override
        public Education[] newArray(int size) {
            return new Education[size];
        }
    };
}
