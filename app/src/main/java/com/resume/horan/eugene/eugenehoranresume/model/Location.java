
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private String country;
    private String state;
    private String city;
    private String street;
    private String zip;
    private Double latitude;
    private Double longitude;


    public String getMapUrlString() {
        return "https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=600x400&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + latitude + "," + longitude + "&key=AIzaSyADB4npZO7NgdyOGniM3Hl7AETODFVR3CE";
    }

    /**
     * No args constructor for use in serialization
     */
    public Location() {
    }

    public Location(String country, String state, String city, String street, String zip, Double latitude, Double longitude) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.state);
        dest.writeString(this.city);
        dest.writeString(this.street);
        dest.writeString(this.zip);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
    }

    protected Location(Parcel in) {
        this.country = in.readString();
        this.state = in.readString();
        this.city = in.readString();
        this.street = in.readString();
        this.zip = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
