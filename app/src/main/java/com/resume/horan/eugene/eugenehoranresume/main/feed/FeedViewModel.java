package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.model.FeedNewPost;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedViewModel extends BaseObservable {

    public FeedViewModel() {
    }

    private List<Object> userList = new ArrayList<>();
    private List<Object> postHeader = new ArrayList<>();
    private List<Object> postList = new ArrayList<>();

    private Query allUsersQuery;
    private Query allPostsQuery;
    private DatabaseReference postLikeRef;
    private ValueEventListener eventListenerOnPostLike;

    void initItems() {
        allUsersQuery = FirebaseUtil.getAllUsersRef();
        allPostsQuery = FirebaseUtil.getAllPostsRef();
        allUsersQuery.addValueEventListener(allUsersListener);
        List<Object> makePostHeader = new ArrayList<>();
        makePostHeader.add(new FeedNewPost(FirebaseUtil.getUser().getPhotoUrl() != null ? FirebaseUtil.getUser().getPhotoUrl().toString() : "null"));
        setObjectList(makePostHeader);
        allPostsQuery.addValueEventListener(allPostListener);
    }

    public void onDestroy() {
        if (allUsersQuery != null) {
            if (allUsersListener != null) {
                allUsersQuery.removeEventListener(allUsersListener);
            }
        }
        if (allPostsQuery != null && allPostListener != null) {
            allPostsQuery.removeEventListener(allPostListener);
        }
        if (postLikeRef != null && eventListenerOnPostLike != null) {
            postLikeRef.removeEventListener(eventListenerOnPostLike);
        }
    }

    private ValueEventListener allUsersListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Object> addUserList = new ArrayList<>();
            List<User> userList = new ArrayList<>();
            // Add me in first visible place
            User me = new User("eh841271@gmail.com", "Eugene Horan", "https://lh3.googleusercontent.com/-9ilxAhDN6C0/AAAAAAAAAAI/AAAAAAAAOj0/RqjXQyEYHiM/s96-c/photo.jpg");
            me.setUid("o370oIr8YMMoqHRP5CtM9BOQe4l2");
            addUserList.add(me);
            // Add Divider
            addUserList.add("DIVIDER");
            // Add Rest of users
            if (dataSnapshot.exists()) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    if (!item.getKey().equalsIgnoreCase(me.getUid())) {
                        User pos = item.getValue(User.class);
                        pos.setUid(item.getKey());
                        userList.add(pos);
                    }
                }
            }
            // Adding empty users to take up space
            userList.add(new User(null, null, "null"));
            userList.add(new User(null, null, "null"));
            userList.add(new User(null, null, "null"));
            userList.add(new User(null, null, "null"));
            userList.add(new User(null, null, "null"));
            userList.add(new User(null, null, "null"));
            userList.add(new User(null, null, "null"));
            addUserList.addAll(userList);
            setUserList(addUserList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private ValueEventListener allPostListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Object> allPostsList = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Post pos = item.getValue(Post.class);
                    pos.setKey(item.getKey());
                    allPostsList.add(pos);
                }
            }
            setAddObjectList(allPostsList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    /**
     * Updates
     */
    void onPostLikeClicked(final Post post) {
        postLikeRef = FirebaseUtil.getUserPostLikeRef(post.getKey());
        eventListenerOnPostLike = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FirebaseUtil.getUserPostLikeRef(post.getKey()).removeValue();
                    FirebaseUtil.getUserPostLikeRef(post.getKey()).child("user").removeValue();
                } else {
                    Map<String, Object> updatedUserData = new HashMap<>();
                    updatedUserData.put(FirebaseUtil.getUser().getUid(), true);
                    FirebaseUtil.getPostLikesListRef(post.getKey()).updateChildren(updatedUserData);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        };
        postLikeRef.addListenerForSingleValueEvent(eventListenerOnPostLike);
    }

    /**
     * Bindable data
     */
    @Bindable
    public List<Object> getUserList() {
        return userList;
    }

    private void setUserList(List<Object> mUserList) {
        this.userList = mUserList;
        notifyChange();
    }

    @Bindable
    public List<Object> getAddObjectList() {
        return postList;
    }

    private void setAddObjectList(List<Object> addObjectList) {
        Collections.reverse(addObjectList);
        this.postList = addObjectList;
        notifyChange();
    }

    @Bindable
    public List<Object> getObjectList() {
        return postHeader;
    }

    public void setObjectList(List<Object> objectList) {
        this.postHeader = objectList;
        notifyChange();
    }
}
