
package com.resume.horan.eugene.eugenehoranresume.model;


public class Education {

    private String order;
    private String university;
    private String location;
    private String dateRange;
    private String major;

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
     * @param major
     */
    public Education(String order, String university, String location, String dateRange, String major) {
        super();
        this.order = order;
        this.university = university;
        this.location = location;
        this.dateRange = dateRange;
        this.major = major;
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

}
