package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentFeedBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Author;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.post.NewPostActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedFragment extends Fragment {
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private FeedViewModel model;
    private FeedRecyclerAdapter adapterFeed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new FeedViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFeedBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        // Recycler Users
        binding.setAdapterUsers(new FeedUserRecyclerAdapter());
        binding.recyclerUsers.setNestedScrollingEnabled(false);
        binding.recyclerUsers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        // Recycler Feed
        adapterFeed = new FeedRecyclerAdapter();
        binding.setAdapter(adapterFeed);
        binding.recycler.setNestedScrollingEnabled(false);
        adapterFeed.setListener(listener);

        binding.setModel(model);
        return binding.getRoot();
    }

    private void setLiked(final Post post) {
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
//                    FirebaseUtil.getUserPostLikeRef(post.getKey()).setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public void onUserClicked(Post post) {
        Query likesQuery = FirebaseUtil.getPostLikesListRef(post.getKey()).orderByChild("-timestamp");
        Activity activity = getActivity();

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View v = activity.getLayoutInflater().inflate(R.layout.layout_bs_feed_likes, null);
        RecyclerView recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerAdapter<User, ViewHolderLikes> adapter = new FirebaseRecyclerAdapter<User, ViewHolderLikes>(User.class, R.layout.recycler_user_likes, ViewHolderLikes.class, likesQuery) {
            @Override
            public void populateViewHolder(final ViewHolderLikes postViewHolder, final User user, final int position) {
                postViewHolder.name.setText(user.getDisplayName());
                Drawable drawable = ContextCompat.getDrawable(postViewHolder.itemView.getContext(), R.drawable.ic_account_circle_white);
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(postViewHolder.itemView.getContext(), R.color.greyIconNormal), PorterDuff.Mode.MULTIPLY));
                drawable.mutate();
                Picasso.with(postViewHolder.itemView.getContext()).load(user.getImageUrl()).error(drawable).into(postViewHolder.circleImage);
            }
        };
        recyclerView.setAdapter(adapter);
        mBottomSheetDialog.setContentView(v);
        mBottomSheetDialog.show();
    }

    private FeedRecyclerAdapter.Listener listener = new FeedRecyclerAdapter.Listener() {
        @Override
        public void onAddImageClick() {
            Intent i = new Intent(getActivity(), NewPostActivity.class);
            startActivity(i);
        }

        @Override
        public void onLikedClicked(Post post) {
            setLiked(post);
        }

        @Override
        public void onShowLikesClicked(Post post) {
            onUserClicked(post);
        }
    };


    public static class ViewHolderLikes extends RecyclerView.ViewHolder {
        private CircleImageView circleImage;
        private TextView name;

        public ViewHolderLikes(View itemView) {
            super(itemView);
            circleImage = itemView.findViewById(R.id.circleImage);
            name = itemView.findViewById(R.id.name);
        }

        public void bindViews(Author author) {
            name.setText(author.getUserName());
            Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_account_circle_white);
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.greyIconNormal), PorterDuff.Mode.MULTIPLY));
            drawable.mutate();
            Picasso.with(itemView.getContext()).load(author.getProfilePicture()).error(drawable).into(circleImage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        model.initItems();
    }

    @Override
    public void onDestroy() {
        model.onDestroy();
        adapterFeed.onDestroy();
        super.onDestroy();
    }
}