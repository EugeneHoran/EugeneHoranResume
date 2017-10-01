
package com.resume.horan.eugene.eugenehoranresume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class EugeneHoran implements Parcelable {

    public String nameFull;
    private String nameFirst;
    private String nameMiddle;
    private String nameLast;
    private Contact contact;
    private Goals goals;
    private List<SocialMedia> socialMedia = null;
    private Resume resume;


    /**
     * Filters
     */
    public ResumeEducationObject getFilteredEducations() {
        List<Education> educationList = resume.getEducation();
        List<Object> mObjectList = new ArrayList<>();
        for (int i = 0; i < educationList.size(); i++) {
            Education education = educationList.get(i);
            List<EducationActivity> eductionActivity = new ArrayList<>();
            if (education.getEducationActivity() != null) {
                eductionActivity.addAll(education.getEducationActivity());
                education.setEducationActivity(null);
            }
            mObjectList.add(education);
            mObjectList.addAll(eductionActivity);
        }
        return new ResumeEducationObject(mObjectList);
    }

    public ResumeSkillObject getFilteredSkills() {
        List<Skill> skillList = resume.getSkill();
        List<Object> mObjectList = new ArrayList<>();
        for (int i = 0; i < skillList.size(); i++) {
            Skill skill = skillList.get(i);
            List<SkillItem> skillItemList = new ArrayList<>();
            skillItemList.addAll(skill.getSkillItem());
            skill.setSkillItem(null);
            mObjectList.add(skill);
            mObjectList.addAll(skillItemList);
        }
        return new ResumeSkillObject(mObjectList);
    }

    public ResumeExperienceObject getFilteredExperiences() {
        List<Experience> experienceList = resume.getExperience();
        List<Object> mObjectList = new ArrayList<>();
        mObjectList.add(new Header("Public Accounts"));
        mObjectList.addAll(resume.getAccount());
        mObjectList.add(2, new DividerFiller("divider_no_space"));
        mObjectList.add(new Header("Resume"));
        for (int i = 0; i < experienceList.size(); i++) {
            if (mObjectList.size() > 5) {
                mObjectList.add(new DividerFiller("divider_space"));
            }
            Experience experience = experienceList.get(i);
            List<Bullet> bullets = new ArrayList<>();
            bullets.addAll(experience.getBullets());
            experience.setBullets(null);
            mObjectList.add(experience);
            mObjectList.addAll(bullets);
        }
        mObjectList.add(new DividerFiller("footer"));
        return new ResumeExperienceObject(mObjectList);
    }


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
     * @param nameFull
     * @param contact
     */
    public EugeneHoran(String nameFull, String nameFirst, String nameMiddle, String nameLast, Contact contact, Goals goals, List<SocialMedia> socialMedia, Resume resume) {
        super();
        this.nameFull = nameFull;
        this.nameFirst = nameFirst;
        this.nameMiddle = nameMiddle;
        this.nameLast = nameLast;
        this.contact = contact;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameFull);
        dest.writeString(this.nameFirst);
        dest.writeString(this.nameMiddle);
        dest.writeString(this.nameLast);
        dest.writeParcelable(this.contact, flags);
        dest.writeParcelable(this.goals, flags);
        dest.writeTypedList(this.socialMedia);
        dest.writeParcelable(this.resume, flags);
    }

    protected EugeneHoran(Parcel in) {
        this.nameFull = in.readString();
        this.nameFirst = in.readString();
        this.nameMiddle = in.readString();
        this.nameLast = in.readString();
        this.contact = in.readParcelable(Contact.class.getClassLoader());
        this.goals = in.readParcelable(Goals.class.getClassLoader());
        this.socialMedia = in.createTypedArrayList(SocialMedia.CREATOR);
        this.resume = in.readParcelable(Resume.class.getClassLoader());
    }

    public static final Creator<EugeneHoran> CREATOR = new Creator<EugeneHoran>() {
        @Override
        public EugeneHoran createFromParcel(Parcel source) {
            return new EugeneHoran(source);
        }

        @Override
        public EugeneHoran[] newArray(int size) {
            return new EugeneHoran[size];
        }
    };
}
