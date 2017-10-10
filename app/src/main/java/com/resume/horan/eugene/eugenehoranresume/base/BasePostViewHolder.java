package com.resume.horan.eugene.eugenehoranresume.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.model.Post;

public abstract class BasePostViewHolder extends RecyclerView.ViewHolder {

    public BasePostViewHolder(ViewDataBinding itemView) {
        super(itemView.getRoot());
    }

    public void setPost(Post post) {
        ValueEventListener likeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setLikes(dataSnapshot.getChildrenCount() == 1 ? " like" : " likes");
                setNumLikes(dataSnapshot.getChildrenCount());
                if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    setLiked(true);
                } else {
                    setLiked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference().child("likes").child(post.getKey()).addValueEventListener(likeListener);
    }

    public abstract void setLikes(String likes);

    public abstract void setLiked(boolean liked);

    public void setNumLikes(long numLikes) {
        String likes = numLikes == 0 || numLikes == 1 ? " like" : " likes";
        String totalLikes = String.valueOf(numLikes) + " ";
        setLikes(totalLikes + likes);
    }
}
