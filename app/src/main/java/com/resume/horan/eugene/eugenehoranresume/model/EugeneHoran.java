
package com.resume.horan.eugene.eugenehoranresume.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class EugeneHoran implements Parcelable {

    public String nameFull;
    private String nameFirst;
    private String nameMiddle;
    private String nameLast;
    private Contact contact;
    private Location location;
    private Goals goals;
    private List<SocialMedia> socialMedia = null;
    private Resume resume;


    /**
     * No args constructor for use in serialization
     */
    public EugeneHoran() {
    }

    /**
     * @param goals
     * @param resume
     * @param nameLast
     * @param nameMiddle
     * @param nameFirst
     * @param socialMedia
     * @param location
     * @param nameFull
     * @param contact
     */
    public EugeneHoran(String nameFull, String nameFirst, String nameMiddle, String nameLast, Contact contact, Location location, Goals goals, List<SocialMedia> socialMedia, Resume resume) {
        super();
        this.nameFull = nameFull;
        this.nameFirst = nameFirst;
        this.nameMiddle = nameMiddle;
        this.nameLast = nameLast;
        this.contact = contact;
        this.location = location;
        this.goals = goals;
        this.socialMedia = socialMedia;
        this.resume = resume;
    }

    public String getNameFull() {
        return nameFull;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameMiddle() {
        return nameMiddle;
    }

    public void setNameMiddle(String nameMiddle) {
        this.nameMiddle = nameMiddle;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public List<SocialMedia> getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(List<SocialMedia> socialMedia) {
        this.socialMedia = socialMedia;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }


    /**
     * Parcel
     */
    public final static Creator<EugeneHoran> CREATOR = new Creator<EugeneHoran>() {
        @SuppressWarnings({
                "unchecked"
        })
        public EugeneHoran createFromParcel(Parcel in) {
            return new EugeneHoran(in);
        }

        public EugeneHoran[] newArray(int size) {
            return (new EugeneHoran[size]);
        }

    };

    protected EugeneHoran(Parcel in) {
        this.nameFull = ((String) in.readValue((String.class.getClassLoader())));
        this.nameFirst = ((String) in.readValue((String.class.getClassLoader())));
        this.nameMiddle = ((String) in.readValue((String.class.getClassLoader())));
        this.nameLast = ((String) in.readValue((String.class.getClassLoader())));
        this.contact = ((Contact) in.readValue((Contact.class.getClassLoader())));
        this.location = ((Location) in.readValue((Location.class.getClassLoader())));
        this.goals = ((Goals) in.readValue((Goals.class.getClassLoader())));
        in.readList(this.socialMedia, (SocialMedia.class.getClassLoader()));
        this.resume = ((Resume) in.readValue((Resume.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(nameFull);
        dest.writeValue(nameFirst);
        dest.writeValue(nameMiddle);
        dest.writeValue(nameLast);
        dest.writeValue(contact);
        dest.writeValue(location);
        dest.writeValue(goals);
        dest.writeList(socialMedia);
        dest.writeValue(resume);
    }

    public int describeContents() {
        return 0;
    }

}
