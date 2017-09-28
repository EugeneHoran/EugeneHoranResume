
package com.resume.horan.eugene.eugenehoranresume.model;

import java.util.List;

public class Resume {

    private List<Experience> experience = null;
    private List<Education> education = null;

    /**
     * No args constructor for use in serialization
     */
    public Resume() {
    }

    /**
     * @param experience
     * @param education
     */
    public Resume(List<Experience> experience, List<Education> education) {
        super();
        this.experience = experience;
        this.education = education;
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

}
