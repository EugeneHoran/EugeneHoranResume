package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentFeedBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Comment;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.post.NewPostActivity;
import com.resume.horan.eugene.eugenehoranresume.userprofile.UserProfileActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;

public class FeedFragment extends Fragment implements FeedUserRecyclerAdapterNew.Listener, FeedRecyclerAdapter.Listener, FeedAddCommentBottomSheetFragment.Listener {
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private FeedViewModel model;
    private FeedUserRecyclerAdapterNew adapterUsers;
    private FeedRecyclerAdapter adapterFeed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new FeedViewModel();
        adapterUsers = new FeedUserRecyclerAdapterNew();
        adapterFeed = new FeedRecyclerAdapter();
        adapterUsers.setListener(this);
        adapterFeed.setListener(this);
    }

    @Override
    public void onDestroy() {
        model.onDestroy();
        adapterFeed.onDestroy();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFeedBinding binding = FragmentFeedBinding.inflate(getLayoutInflater());
        // Recycler Users
        binding.setAdapterUsers(adapterUsers);
        binding.recyclerUsers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerUsers.setNestedScrollingEnabled(false);
        // Recycler Feed
        binding.setAdapter(adapterFeed);
        binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recycler.setNestedScrollingEnabled(false);
        // Init
        binding.setModel(model);
        model.initItems();
        return binding.getRoot();
    }

    @Override
    public void onViewUserProfileClicked(String uid) {
        Intent i = new Intent(getActivity(), UserProfileActivity.class);
        i.putExtra(Common.ARG_USER_ID, uid);
        startActivity(i);
    }

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
        commentBS.setListener(this);
    }

    @Override
    public void onLikedClicked(Post post) {
        model.onPostLikeClicked(post);
    }

    /**
     * User wants to make comment on post
     * <p>
     * From {@link FeedAddCommentBottomSheetFragment}
     *
     * @param postKey     post key
     * @param postComment post comment
     */
    @Override
    public void addCommentToPost(final String postKey, String postComment) {
        Comment comment = new Comment(FirebaseUtil.getUser().getUid(), postKey, postComment, ServerValue.TIMESTAMP);
        FirebaseUtil.getPostCommentsRef(postKey).push().setValue(comment, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                if (error != null) {
                    Log.w("ERROR", "Error posting comment: " + error.getMessage());
                    Toast.makeText(getActivity(), "Error posting comment.", Toast.LENGTH_SHORT).show();
                } else {
                    FeedLikesCommentsBottomSheetFragment.newInstance(Common.WHICH_COMMENTS, postKey).show(getChildFragmentManager(), Common.DIALOG_FEED_LIKES_COMMENTS);
                    Toast.makeText(getActivity(), "Comment Posted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}