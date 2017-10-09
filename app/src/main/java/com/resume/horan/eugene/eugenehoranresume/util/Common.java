package com.resume.horan.eugene.eugenehoranresume.util;


public class Common {
    /**
     * Google
     */
    public static final int RC_SIGN_IN_G = 133;

    /**
     * Firebase Database References
     */
    public static final String FB_REF_EUGENE_HORAN = "eugeneHoran";
    public static final String FB_REF_CONTACT = "contact";
    public static final String FB_REF_ABOUT = "about";
    public static final String FB_REF_USERS = "users";

    /**
     * Navigation Views
     */
    public static final int WHICH_RESUME_FRAGMENT = 0;
    public static final int WHICH_CONTACT_FRAGMENT = 1;
    public static final int WHICH_ABOUT_FRAGMENT = 2;
    public static final int WHICH_FEED_FRAGMENT = 3;

    /**
     * Pager Fragments
     */
    public static final int WHICH_EXPERIENCE_FRAGMENT = 0;
    public static final int WHICH_SKILL_FRAGMENT = 1;
    public static final int WHICH_EDUCATION_FRAGMENT = 2;
    /**
     * Fingerprint Types
     */
    public static final int FINGERPRINT_RESULT = 100;
    public static final int WHICH_FINGERPRINT_LOGIN = 0;
    public static final int WHICH_FINGERPRINT_SETTINGS = 1;
    /**
     * Common Arguments
     */
    public static final String ARG_FINGERPRINT_TYPE = "arg_fingerprint_type";
    public static final String ARG_FINGERPRINT_RETURN_ERROR = "arg_fingerprint_return_error";
    public static final String ARG_WHICH_FRAGMENT = "arg_which_fragment";
    public static final String ARG_RESUME_EXPERIENCE = "arg_resume_experience";
    public static final String ARG_RESUME_SKILL = "arg_resume_skills";
    public static final String ARG_RESUME_EDUCATION = "arg_resume_education";
    public static final String ARG_CONTACT = "arg_contact";
    public static final String ARG_ABOUT = "arg_about";
    public static final String ARG_IMAGE = "image";


    /**
     * Divider
     */
    public static final String DIVIDER_LINE_NO_SPACE = "divider_no_space";
    public static final String DIVIDER_LINE_WITH_SPACE = "divider_space";
    public static final String DIVIDER_NO_LINE_WITH_SPACE = "footer";


    /**
     * Posts
     */

    public static final int TYPE_POST_MESSAGE = 0;
    public static final int TYPE_POST_IMAGE = 1;
    public static final int TYPE_POST_MESSAGE_IMAGE = 2;
    /**
     * Image Link
     */
    public static final String IMG_PROFILE = "https://firebasestorage.googleapis.com/v0/b/eugenehoranresume.appspot.com/o/eugeneProfile.jpg?alt=media&token=467c2ce9-410f-4682-9f09-b15035d25263";
}
