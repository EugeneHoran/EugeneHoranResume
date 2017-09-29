
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Education implements Parcelable {

    private String order;
    private String university;
    private String location;
    private String dateRange;
    private String major;
    private String logoUrl;


    /**
     * No args constructor for use in serialization
     */
    public Education() {
    }

    /**
     * @param dateRange
     * @param university
     * @param order
     * @param location
     * @param logoUrl
     * @param major
     */
    public Education(String order, String university, String location, String dateRange, String major, String logoUrl) {
        super();
        this.order = order;
        this.university = university;
        this.location = location;
        this.dateRange = dateRange;
        this.major = major;
        this.logoUrl = logoUrl;
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

    /**
     * Parcel
     */
    public final static Creator<Education> CREATOR = new Creator<Education>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Education createFromParcel(Parcel in) {
            return new Education(in);
        }

        public Education[] newArray(int size) {
            return (new Education[size]);
        }

    };

    protected Education(Parcel in) {
        this.order = ((String) in.readValue((String.class.getClassLoader())));
        this.university = ((String) in.readValue((String.class.getClassLoader())));
        this.location = ((String) in.readValue((String.class.getClassLoader())));
        this.dateRange = ((String) in.readValue((String.class.getClassLoader())));
        this.major = ((String) in.readValue((String.class.getClassLoader())));
        this.logoUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(order);
        dest.writeValue(university);
        dest.writeValue(location);
        dest.writeValue(dateRange);
        dest.writeValue(major);
        dest.writeValue(logoUrl);
    }

    public int describeContents() {
        return 0;
    }

}
