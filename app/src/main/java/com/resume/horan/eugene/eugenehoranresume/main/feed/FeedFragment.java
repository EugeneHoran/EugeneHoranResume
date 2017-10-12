package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentFeedBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Comment;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.post.NewPostActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class FeedFragment extends Fragment {
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private FeedViewModel model;
    private FeedRecyclerAdapter adapterFeed;
    private FeedUserRecyclerAdapter adapterUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new FeedViewModel();
        adapterFeed = new FeedRecyclerAdapter();
        adapterUsers = new FeedUserRecyclerAdapter();
        adapterFeed.setListener(feedRecyclerAdapterListener);
    }

    @Override
    public void onDestroy() {
        model.onDestroy();
        adapterFeed.onDestroy();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFeedBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
//        binding.nestedScroll.setNestedScrollingEnabled(false);
        // Recycler Users
        binding.setAdapterUsers(adapterUsers);
        binding.recyclerUsers.setNestedScrollingEnabled(false);
        binding.recyclerUsers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        // Recycler Feed
        binding.setAdapter(adapterFeed);
        binding.recycler.setNestedScrollingEnabled(false);
        // Init
        binding.setModel(model);
        model.initItems();
        return binding.getRoot();
    }

    private FeedRecyclerAdapter.Listener feedRecyclerAdapterListener = new FeedRecyclerAdapter.Listener() {
        @Override
        public void onAddImageClick() {
            Intent i = new Intent(getActivity(), NewPostActivity.class);
            startActivity(i);
        }

        @Override
        public void onShowLikesClicked(Post post) {
            FeedLikesCommentsBottomSheetFragment.newInstance(Common.WHICH_LIKES, post.getKey()).show(getChildFragmentManager(), Common.DIALOG_FEED_LIKES_COMMENTS);
        }

        @Override
        public void onShowCommentsClicked(Post post) {
            FeedLikesCommentsBottomSheetFragment.newInstance(Common.WHICH_COMMENTS, post.getKey()).show(getChildFragmentManager(), Common.DIALOG_FEED_LIKES_COMMENTS);
        }

        @Override
        public void onAddCommentClicked(Post post) {
            FeedAddCommentBottomSheetFragment commentBS = FeedAddCommentBottomSheetFragment.newInstance(post.getKey());
            commentBS.show(getChildFragmentManager(), Common.DIALOG_FEED_ADD_COMMENT);
            commentBS.setListener(new FeedAddCommentBottomSheetFragment.Listener() {
                @Override
                public void addCommentToPost(String postKey, String postComment) {
                    postComment(postKey, postComment);
                }
            });
        }

        @Override
        public void onLikedClicked(final Post post) {
            FirebaseUtil.getUserPostLikeRef(post.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        FirebaseUtil.getUserPostLikeRef(post.getKey()).removeValue();
                        FirebaseUtil.getUserPostLikeRef(post.getKey()).child("user").removeValue();
                    } else {
                        FirebaseUser user = FirebaseUtil.getUser();
                        Map<String, Object> updatedUserData = new HashMap<>();
                        updatedUserData.put(FirebaseUtil.getUser().getUid(), new ObjectMapper().convertValue(new User(user.getEmail(), user.getDisplayName(), String.valueOf(user.getPhotoUrl())), Map.class));
                        FirebaseUtil.getPostLikesListRef(post.getKey()).updateChildren(updatedUserData);
                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        }
    };

    private void postComment(final String strPostKey, String strComment) {
//        FirebaseUser fbUser = FirebaseUtil.getUser();
//        DatabaseReference commentRef = FirebaseUtil.getPostCommentsRef(strPostKey);
//        String commentKey = commentRef.push().getKey();
//        User user = new User(fbUser.getEmail(), fbUser.getDisplayName(), String.valueOf(fbUser.getPhotoUrl()));
//        Comment comment = new Comment(user, FirebaseUtil.getUser().getUid(), strPostKey, commentKey, strComment, ServerValue.TIMESTAMP);
//        commentRef.setValue(comment, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
//                if (error != null) {
//                    Log.w("ERROR", "Error posting comment: " + error.getMessage());
//                    Toast.makeText(getActivity(), "Error posting comment.", Toast.LENGTH_SHORT).show();
//                } else {
//                    FeedLikesCommentsBottomSheetFragment.newInstance(Common.WHICH_COMMENTS, strPostKey).show(getChildFragmentManager(), Common.DIALOG_FEED_LIKES_COMMENTS);
//                    Toast.makeText(getActivity(), "Comment Posted", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        FirebaseUser fbUser = FirebaseUtil.getUser();
        User user = new User(fbUser.getEmail(), fbUser.getDisplayName(), String.valueOf(fbUser.getPhotoUrl()));
        Comment comment = new Comment(user, FirebaseUtil.getUser().getUid(), strPostKey, strComment, ServerValue.TIMESTAMP);
        FirebaseUtil.getPostCommentsRef(strPostKey).push().setValue(comment, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                if (error != null) {
                    Log.w("ERROR", "Error posting comment: " + error.getMessage());
                    Toast.makeText(getActivity(), "Error posting comment.", Toast.LENGTH_SHORT).show();
                } else {
                    FeedLikesCommentsBottomSheetFragment.newInstance(Common.WHICH_COMMENTS, strPostKey).show(getChildFragmentManager(), Common.DIALOG_FEED_LIKES_COMMENTS);
                    Toast.makeText(getActivity(), "Comment Posted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}