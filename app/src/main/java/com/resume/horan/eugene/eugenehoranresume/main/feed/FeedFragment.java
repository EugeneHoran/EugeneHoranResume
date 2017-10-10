package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentFeedBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.post.NewPostActivity;

public class FeedFragment extends Fragment {
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FeedViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFeedBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        FeedRecyclerAdapter adapter = new FeedRecyclerAdapter();
        binding.setAdapter(adapter);
        adapter.setListener(new FeedRecyclerAdapter.Listener() {
            @Override
            public void onAddImageClick() {
                Intent i = new Intent(getActivity(), NewPostActivity.class);
                startActivity(i);
            }

            @Override
            public void onLikedClicked(Post post) {
                setLiked(post);
            }
        });
        model = new FeedViewModel();
        binding.setModel(model);
        return binding.getRoot();
    }

    private void setLiked(Post post) {
        final Post mPost = post;
        final String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference postLikesRef = FirebaseDatabase.getInstance().getReference().child("likes");
        postLikesRef.child(post.getKey()).child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    postLikesRef.child(mPost.getKey()).child(userKey).removeValue();
                } else {
                    postLikesRef.child(mPost.getKey()).child(userKey).setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        model.initItems();
        model.filterItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}