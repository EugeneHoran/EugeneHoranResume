package com.resume.horan.eugene.eugenehoranresume.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getCurrentUserId() {
        return getUser().getUid();
    }

    /**
     * Main Data References
     */
    public static DatabaseReference getResumeRef() {
        return getBaseRef().child(Common.FB_REF_EUGENE_HORAN);
    }

    public static DatabaseReference getContactRef() {
        return getBaseRef().child(Common.FB_REF_EUGENE_HORAN).child(Common.FB_REF_CONTACT);
    }

    public static DatabaseReference getAboutRef() {
        return getBaseRef().child(Common.FB_REF_EUGENE_HORAN).child(Common.FB_REF_ABOUT);
    }


    /**
     * User References
     */

    private static DatabaseReference getUserRef() {
        return getBaseRef().child(Common.FB_REF_USERS);
    }

    public static DatabaseReference getCurrentUserRef() {
        return getUserRef().child(getCurrentUserId());
    }
}
