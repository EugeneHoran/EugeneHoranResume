package com.resume.horan.eugene.eugenehoranresume.base;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.main.feed.FeedRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.model.Comment;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.resume.horan.eugene.eugenehoranresume.util.ui.LayoutUtil;
import com.resume.horan.eugene.eugenehoranresume.viewimage.ViewImageActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "ConstantConditions"})
public abstract class BasePostViewHolder extends RecyclerView.ViewHolder {
    private FeedRecyclerAdapter.Listener listener;
    private Post post;

    private DatabaseReference userRef;
    private DatabaseReference likeRef;
    private DatabaseReference commentRef;

    public final ObservableField<User> obsUser = new ObservableField<>();
    public final ObservableField<String> strNumOfLikes = new ObservableField<>();
    public final ObservableField<String> strLikedStatus = new ObservableField<>();
    public final ObservableField<Integer> intLikedStatus = new ObservableField<>();
    public final ObservableField<String> strNumOfComments = new ObservableField<>();
    public final ObservableField<Boolean> boolShowMenu = new ObservableField<>(false);

    public BasePostViewHolder(ViewDataBinding itemView) {
        super(itemView.getRoot());
    }

    protected void bindBaseViews(FeedRecyclerAdapter.Listener listener, Post post) {
        this.listener = listener;
        this.post = post;
        userRef = FirebaseUtil.getAllUsersRef().child(post.getUid());
        likeRef = FirebaseUtil.getPostLikesRef(post.getKey());
        commentRef = FirebaseUtil.getPostCommentsRef(post.getKey());
        userRef.addListenerForSingleValueEvent(userValueEventListener);
        likeRef.addValueEventListener(likeValueEventListener);
        commentRef.addValueEventListener(commentValueEventListener);
        if (post == null) {
            return;
        }
        if (post.getUid() == null) {
            return;
        }
        if (post.getUid().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            boolShowMenu.set(true);
        } else {
            boolShowMenu.set(false);
        }
    }

    /**
     * Handle Event Life
     */
    private ValueEventListener userValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            obsUser.set(user);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    private ValueEventListener commentValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Comment> comments = new ArrayList<>();
            for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                Comment comment = commentSnapshot.getValue(Comment.class);
                comments.add(comment);
            }
            String commentSize = comments.size() == 0 ? "0" : comments.size() + "";
            strNumOfComments.set(String.valueOf(comments.size()) + String.valueOf(comments.size() == 1 ? " Comment" : " Comments"));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    private ValueEventListener likeValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            long numLikes = dataSnapshot.getChildrenCount();
            boolean isLiked = dataSnapshot.hasChild(FirebaseUtil.getUser().getUid());
            strNumOfLikes.set(String.valueOf(numLikes) + String.valueOf(numLikes == 1 ? " like" : " likes"));
            strLikedStatus.set(isLiked ? "Unlike" : "Like");
            intLikedStatus.set(isLiked ? R.drawable.ic_heart_full : R.drawable.ic_heart_empty);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    /**
     * Handle Shared Clicks
     *
     * @param view clicked
     */
    public void onMenuClicked(View view) {
        onMenuClick(view, post);
    }

    public void onProfileClicked(View view) {
        if (listener != null) {
            listener.onViewUserProfileClicked(post.getUid());
        }
    }

    public void onImageClicked(View view) {
        imageClicked(view);
    }

    public void onShowLikesClicked(View view) {
        if (listener != null) {
            listener.onShowLikesClicked(post);
        }
    }

    public void onLikeClicked(View view) {
        if (listener != null) {
            listener.onLikedClicked(post);
        }
    }

    public void onAddCommentsClicked(View view) {
        if (listener != null) {
            listener.onAddCommentClicked(post);
        }
    }

    public void onShowCommentsClicked(View view) {
        if (listener != null) {
            listener.onShowCommentsClicked(post);
        }
    }

    public void cancelRef() {
        if (userRef != null) {
            if (userValueEventListener != null) {
                userRef.removeEventListener(userValueEventListener);
            }
        }
        if (likeRef != null) {
            if (likeValueEventListener != null) {
                likeRef.removeEventListener(likeValueEventListener);
            }
        }
        if (commentRef != null) {
            if (commentValueEventListener != null) {
                commentRef.removeEventListener(commentValueEventListener);
            }
        }
    }

    /**
     * handle Menu Click
     */
    private void onMenuClick(final View view, final Post post) {
        PopupMenu popup = new PopupMenu(view.getContext(), view, GravityCompat.START);
        popup.getMenuInflater().inflate(R.menu.menu_feed, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Query postQuery = FirebaseUtil.getAllPostsRef().child(post.getKey());
                postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            FirebaseUtil.getPostCommentsRef(post.getKey()).removeValue();
                            FirebaseUtil.getPostLikesListRef(post.getKey()).removeValue();
                            FirebaseUtil.getCurrentUserPostRef(post.getKey()).removeValue();
                        }
                        Toast.makeText(view.getContext(), "Deleted Post", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                return true;
            }
        });
        popup.show();
    }

    /**
     * Handle Image Click
     */
    @SuppressWarnings("unchecked")
    private void imageClicked(View view) {
        Activity activity = (Activity) view.getContext();
        Intent intent = new Intent(activity, ViewImageActivity.class);
        intent.putExtra(Common.ARG_IMAGE_STRING, post.getFull_url());
        if (LayoutUtil.isL()) {
            Pair<View, String> p2 = Pair.create(view, "image");
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p2);
            activity.startActivity(intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }
}
