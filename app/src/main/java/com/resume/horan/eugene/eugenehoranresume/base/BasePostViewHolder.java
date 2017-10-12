package com.resume.horan.eugene.eugenehoranresume.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;

public abstract class BasePostViewHolder extends RecyclerView.ViewHolder {

    public BasePostViewHolder(ViewDataBinding itemView) {
        super(itemView.getRoot());
    }

    private DatabaseReference likeRef;

    public void setPost(Post post) {
        likeRef = FirebaseUtil.getPostLikesRef(post.getKey());
        likeRef.addValueEventListener(likeListener);
    }

    public void cancelRef() {
        if (likeRef != null) {
            if (likeListener != null) {
                likeRef.removeEventListener(likeListener);
            }
        }
    }

    public abstract void setLikes(String likes);

    public abstract void setLiked(boolean liked);

    private void setNumLikes(long numLikes) {
        String likes = numLikes == 0 || numLikes == 1 ? " like" : " likes";
        String totalLikes = String.valueOf(numLikes) + " ";
        setLikes(totalLikes + likes);
    }

    private ValueEventListener likeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setLikes(dataSnapshot.getChildrenCount() == 1 ? " like" : " likes");
            setNumLikes(dataSnapshot.getChildrenCount());
            if (dataSnapshot.hasChild(FirebaseUtil.getUser().getUid())) {
                setLiked(true);
            } else {
                setLiked(false);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
