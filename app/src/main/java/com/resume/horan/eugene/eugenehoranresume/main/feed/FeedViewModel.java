package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.model.FeedNewPost;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedViewModel extends BaseObservable {

    public FeedViewModel() {
    }

    private List<Object> objectList = new ArrayList<>();
    private List<Object> addObjectList = new ArrayList<>();
    private List<Object> mUserList = new ArrayList<>();

    void initItems() {
        loadUsers();
        List<Object> testing = new ArrayList<>();
        testing.add(new FeedNewPost(FirebaseUtil.getUser().getPhotoUrl() != null ? FirebaseUtil.getUser().getPhotoUrl().toString() : "null"));
        setObjectList(testing);
        filterItems();
    }

    private void loadUsers() {
        final List<Object> testing = new ArrayList<>();
        Query allPostsQuery = FirebaseDatabase.getInstance().getReference().child("users");
        ValueEventListener allPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        User pos = item.getValue(User.class);
                        userList.add(pos);
                    }
                }
                userList.add(new User(null, null, "null"));
                userList.add(new User(null, null, "null"));
                userList.add(new User(null, null, "null"));
                userList.add(new User(null, null, "null"));
                userList.add(new User(null, null, "null"));
                userList.add(new User(null, null, "null"));
                userList.add(new User(null, null, "null"));
                testing.addAll(userList);
                setUserList(testing);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        allPostsQuery.addValueEventListener(allPostListener);
    }

    private void filterItems() {
        final List<Object> testing = new ArrayList<>();
        Query allPostsQuery = FirebaseDatabase.getInstance().getReference().child("posts");
        ValueEventListener allPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Post pos = item.getValue(Post.class);
                        pos.setKey(item.getKey());
                        testing.add(pos);
                    }
                }
                setAddObjectList(testing);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        allPostsQuery.addValueEventListener(allPostListener);
    }

    @Bindable
    public List<Object> getUserList() {
        return mUserList;
    }

    public void setUserList(List<Object> mUserList) {
        this.mUserList = mUserList;
        notifyChange();
    }

    @Bindable
    public List<Object> getAddObjectList() {
        return addObjectList;
    }

    public void setAddObjectList(List<Object> addObjectList) {
        Collections.reverse(addObjectList);
        this.addObjectList = addObjectList;
        notifyChange();
    }

    @Bindable
    public List<Object> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
        notifyChange();
    }
}
