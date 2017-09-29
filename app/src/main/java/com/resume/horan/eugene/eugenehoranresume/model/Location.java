
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {

    private String country;
    private String state;
    private String city;
    private String street;
    private String zip;


    /**
     * No args constructor for use in serialization
     */
    public Location() {
    }

    /**
     * @param zip
     * @param street
     * @param state
     * @param city
     * @param country
     */
    public Location(String country, String state, String city, String street, String zip) {
        super();
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.zip = zip;
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

    /**
     * Parcel
     */
    public final static Creator<Location> CREATOR = new Creator<Location>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return (new Location[size]);
        }
    };

    protected Location(Parcel in) {
        this.country = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.street = ((String) in.readValue((String.class.getClassLoader())));
        this.zip = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(country);
        dest.writeValue(state);
        dest.writeValue(city);
        dest.writeValue(street);
        dest.writeValue(zip);
    }

    public int describeContents() {
        return 0;
    }

}
