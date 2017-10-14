package com.resume.horan.eugene.eugenehoranresume.userprofile;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.ActivityUserProfileBinding;
import com.resume.horan.eugene.eugenehoranresume.main.feed.FeedAddCommentBottomSheetFragment;
import com.resume.horan.eugene.eugenehoranresume.main.feed.FeedLikesCommentsBottomSheetFragment;
import com.resume.horan.eugene.eugenehoranresume.main.feed.FeedRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.model.Comment;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.resume.horan.eugene.eugenehoranresume.util.ui.LayoutUtil;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private ActivityUserProfileBinding binding;
    private UserProfileViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        String uid = getIntent().getExtras().getString(Common.ARG_USER_ID);
        model = new UserProfileViewModel(uid);
        binding.setModel(model);
        FeedRecyclerAdapter feedRecyclerAdapter = new FeedRecyclerAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(feedRecyclerAdapter);
        binding.recycler.setNestedScrollingEnabled(false);
        feedRecyclerAdapter.setListener(listener);
        if (LayoutUtil.isL()) {
            animateHeader();
        }
        initHeader();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateHeader() {
        final AnimatedVectorDrawable avd = (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.avd_header_feed);
        binding.animImage.setImageDrawable(avd);
        avd.start();
    }

    private void initHeader() {
        binding.toolbar.setNavigationIcon(LayoutUtil.getDrawableMutate(this, R.drawable.ic_arrow_back, R.color.black));
        binding.collapsingToolbar.setExpandedTitleTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        mMaxScrollSize = binding.appBar.getTotalScrollRange();
        binding.appBar.addOnOffsetChangedListener(this);
        binding.toolbar.setNavigationOnClickListener(this);
//        ViewCompat.setElevation(binding.appBar, 10);
    }

    private FeedRecyclerAdapter.Listener listener = new FeedRecyclerAdapter.Listener() {
        @Override
        public void onAddImageClick() {

        }

        @Override
        public void onShowLikesClicked(Post post) {
            FeedLikesCommentsBottomSheetFragment.newInstance(Common.WHICH_LIKES, post.getKey()).show(getSupportFragmentManager(), Common.DIALOG_FEED_LIKES_COMMENTS);
        }

        @Override
        public void onShowCommentsClicked(Post post) {
            FeedLikesCommentsBottomSheetFragment.newInstance(Common.WHICH_COMMENTS, post.getKey()).show(getSupportFragmentManager(), Common.DIALOG_FEED_LIKES_COMMENTS);
        }

        @Override
        public void onAddCommentClicked(Post post) {
            FeedAddCommentBottomSheetFragment commentBS = FeedAddCommentBottomSheetFragment.newInstance(post.getKey());
            commentBS.show(getSupportFragmentManager(), Common.DIALOG_FEED_ADD_COMMENT);
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
                        Map<String, Object> updatedUserData = new HashMap<>();
                        updatedUserData.put(FirebaseUtil.getUser().getUid(), true);
                        FirebaseUtil.getPostLikesListRef(post.getKey()).updateChildren(updatedUserData);
                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        }

        @Override
        public void onViewUserProfileClicked(String uid) {

        }
    };


    private void postComment(final String strPostKey, String strComment) {
        Comment comment = new Comment(FirebaseUtil.getUser().getUid(), strPostKey, strComment, ServerValue.TIMESTAMP);
        FirebaseUtil.getPostCommentsRef(strPostKey).push().setValue(comment, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                if (error != null) {
                    Log.w("ERROR", "Error posting comment: " + error.getMessage());
                    Toast.makeText(UserProfileActivity.this, "Error posting comment.", Toast.LENGTH_SHORT).show();
                } else {
                    FeedLikesCommentsBottomSheetFragment.newInstance(Common.WHICH_COMMENTS, strPostKey).show(getSupportFragmentManager(), Common.DIALOG_FEED_LIKES_COMMENTS);
                    Toast.makeText(UserProfileActivity.this, "Comment Posted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == binding.toolbar.getChildAt(0)) {
            onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        model.onDestroy();
        super.onDestroy();
    }


    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 15;
    private boolean mIsAvatarShown = true;
    private int mMaxScrollSize;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float totalPercentage = ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
        ((ImageView) binding.toolbar.getChildAt(0)).setColorFilter(ColorUtils.blendARGB(Color.parseColor("#000000"), Color.parseColor("#FFFFFF"), totalPercentage));
        if (mMaxScrollSize == 0) {
            mMaxScrollSize = appBarLayout.getTotalScrollRange();
        }
        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;
        // Collapse
        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            binding.imageProfile.animate().scaleY(0).scaleX(0).setDuration(100).start();
        }
        // Expanded
        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;
            binding.imageProfile.animate().scaleY(1).scaleX(1).start();
        }
    }
}
