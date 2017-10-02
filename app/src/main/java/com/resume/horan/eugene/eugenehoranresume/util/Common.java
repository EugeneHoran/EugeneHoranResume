package com.resume.horan.eugene.eugenehoranresume.util;


public class Common {
    public static final String PREF_FINGERPRINT = "pref_fingerprint";
    public static final String KEY_FINGERPRINT = "key_eugene_fingerprint";

    /**
     * Firebase Database References
     */
    public static final String FB_REF_EUGENE_HORAN = "eugeneHoran";
    public static final String FB_REF_CONTACT = "eugeneHoran/contact";
    public static final String FB_REF_ABOUT = "eugeneHoran/about";

    /**
     * Navigation Views
     */
    public static final int WHICH_RESUME_FRAGMENT = 0;
    public static final int WHICH_CONTACT_FRAGMENT = 1;
    public static final int WHICH_ABOUT_FRAGMENT = 2;

    /**
     * Pager Fragments
     */
    public static final int WHICH_EXPERIENCE_FRAGMENT = 0;
    public static final int WHICH_SKILL_FRAGMENT = 1;
    public static final int WHICH_EDUCATION_FRAGMENT = 2;

    /**
     * Common Arguments
     */
    public static final String ARG_WHICH_FRAGMENT = "arg_which_fragment";
    public static final String ARG_RESUME_EXPERIENCE = "arg_resume_experience";
    public static final String ARG_RESUME_SKILL = "arg_resume_skills";
    public static final String ARG_RESUME_EDUCATION = "arg_resume_education";
    public static final String ARG_CONTACT = "arg_contact";
    public static final String ARG_ABOUT = "arg_about";
}
