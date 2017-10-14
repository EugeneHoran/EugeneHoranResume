package com.resume.horan.eugene.eugenehoranresume.userprofile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.ActivityUserProfileBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfileViewModel extends BaseObservable {

    private DatabaseReference userRef;
    private Query postQuery;

    public ObservableField<String> strUserName = new ObservableField<>();
    public ObservableField<String> strUserImageUrl = new ObservableField<>();
    public ObservableField<Boolean> boolUserHasPosts = new ObservableField<>(false);
    public ObservableField<Boolean> boolShowEmptyState = new ObservableField<>(false);

    private List<Object> allPostsList = new ArrayList<>();

    UserProfileViewModel(String uId) {
        userRef = FirebaseUtil.getUserRef(uId);
        postQuery = FirebaseUtil.getAllUserPostsQuery(uId);
        userRef.addListenerForSingleValueEvent(valueEventListenerUsers);
        postQuery.addValueEventListener(valueEventListenerPosts);
    }

    @Bindable
    public List<Object> getAllPostsList() {
        return allPostsList;
    }

    private void setPostList(List<Object> allPostsList) {
        Collections.reverse(allPostsList);
        this.allPostsList = allPostsList;
        notifyChange();
    }

    private ValueEventListener valueEventListenerUsers = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            if (user != null) {
                strUserName.set(user.displayName);
                strUserImageUrl.set(user.imageUrl != null ? user.imageUrl : "null");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener valueEventListenerPosts = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!dataSnapshot.exists()) {
                boolShowEmptyState.set(true);
                boolUserHasPosts.set(false);
            }
            final long totalPostCount = dataSnapshot.getChildrenCount();
            final List<Object> postObjectList = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    item.getKey();
                    FirebaseUtil.getAllPostsRef().child(item.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Post post = dataSnapshot.getValue(Post.class);
                                if (post != null) {
                                    post.setKey(dataSnapshot.getKey());
                                    postObjectList.add(post);
                                    if (totalPostCount == postObjectList.size()) {
                                        boolUserHasPosts.set(true);
                                        setPostList(postObjectList);
                                    }
                                }
                            } else {
                                boolShowEmptyState.set(true);
                                boolUserHasPosts.set(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public void onDestroy() {
        if (userRef != null && valueEventListenerUsers != null) {
            userRef.removeEventListener(valueEventListenerUsers);
        }
        if (postQuery != null && valueEventListenerPosts != null) {
            postQuery.removeEventListener(valueEventListenerPosts);
        }
    }
}
