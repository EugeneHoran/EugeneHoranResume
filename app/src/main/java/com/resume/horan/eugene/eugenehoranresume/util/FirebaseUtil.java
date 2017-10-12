package com.resume.horan.eugene.eugenehoranresume.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@SuppressWarnings("ConstantConditions")
public class FirebaseUtil {
    /**
     * Initializer's
     */
    private static FirebaseDatabase getFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    private static DatabaseReference getRef() {
        return getFirebaseDatabase().getReference();
    }

    public static FirebaseUser getUser() {
        return getAuth().getCurrentUser();
    }

    private static String getUserId() {
        if (getUser() != null) {
            return getUser().getUid();
        }
        return null;
    }

    /**
     * Main Data References
     */
    public static DatabaseReference getNewUserRef() {
        return getRef().child(Common.FB_REF_USERS).child(getUserId());
    }

    public static DatabaseReference getResumeRef() {
        return getRef().child(Common.FB_REF_EUGENE_HORAN);
    }

    public static DatabaseReference getContactRef() {
        return getRef().child(Common.FB_REF_EUGENE_HORAN).child(Common.FB_REF_CONTACT);
    }

    public static DatabaseReference getAboutRef() {
        return getRef().child(Common.FB_REF_EUGENE_HORAN).child(Common.FB_REF_ABOUT);
    }

    public static DatabaseReference getAllPostsRef() {
        return getRef().child(Common.FB_REF_POSTS);
    }

    public static DatabaseReference getAllUsersRef() {
        return getRef().child(Common.FB_REF_USERS);
    }

    public static DatabaseReference getCurrentUserRef() {
        return getAllUsersRef().child(getUserId());
    }

    public static DatabaseReference getAllLikesRef() {
        return getRef().child(Common.FB_REF_LIKES);
    }

    public static DatabaseReference getPostLikesRef(String postKey) {
        return getAllLikesRef().child(postKey);
    }

    public static DatabaseReference getUserPostLikeRef(String postKey) {
        return getAllLikesRef().child(postKey).child(getUserId());
    }

    public static DatabaseReference getPostLikesListRef(String postKey) {
        return getAllLikesRef().child(postKey);
    }
}
