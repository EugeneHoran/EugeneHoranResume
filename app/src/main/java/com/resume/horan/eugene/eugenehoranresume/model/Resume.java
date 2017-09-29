
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Resume implements Parcelable {

    private List<Experience> experience = null;
    private List<Education> education = null;
    private List<Account> account = null;

    /**
     * No args constructor for use in serialization
     */
    public Resume() {
    }

    /**
     * @param experience
     * @param education
     */
    public Resume(List<Experience> experience, List<Education> education, List<Account> account) {
        super();
        this.experience = experience;
        this.education = education;
        this.account = account;
    }

    public List<Experience> getExperience() {
        return experience;
    }

    public void setExperience(List<Experience> experience) {
        this.experience = experience;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<Account> getAccount() {
        return account;
    }

    public void setAccount(List<Account> account) {
        this.account = account;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.experience);
        dest.writeTypedList(this.education);
        dest.writeTypedList(this.account);
    }

    protected Resume(Parcel in) {
        this.experience = in.createTypedArrayList(Experience.CREATOR);
        this.education = in.createTypedArrayList(Education.CREATOR);
        this.account = in.createTypedArrayList(Account.CREATOR);
    }

    public static final Parcelable.Creator<Resume> CREATOR = new Parcelable.Creator<Resume>() {
        @Override
        public Resume createFromParcel(Parcel source) {
            return new Resume(source);
        }

        @Override
        public Resume[] newArray(int size) {
            return new Resume[size];
        }
    };
}
