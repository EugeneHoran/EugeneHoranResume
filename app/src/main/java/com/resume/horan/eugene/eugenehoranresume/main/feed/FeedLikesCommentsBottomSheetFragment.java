package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.Comment;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.resume.horan.eugene.eugenehoranresume.util.ui.LayoutUtil;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedLikesCommentsBottomSheetFragment extends BottomSheetDialogFragment {

    public static FeedLikesCommentsBottomSheetFragment newInstance(int which, String postKey) {
        FeedLikesCommentsBottomSheetFragment fragment = new FeedLikesCommentsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putInt(Common.ARG_FEED_LIKES_OR_COMMENTS, which);
        args.putString(Common.ARG_POST_KEY, postKey);
        fragment.setArguments(args);
        return fragment;
    }

    private Context context;
    private int which;
    private String strPostKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            which = getArguments().getInt(Common.ARG_FEED_LIKES_OR_COMMENTS);
            strPostKey = getArguments().getString(Common.ARG_POST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bs_feed_likes, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        ImageView image = v.findViewById(R.id.image);
        TextView title = v.findViewById(R.id.title);
        RecyclerView recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        switch (which) {
            case Common.WHICH_LIKES:
                image.setImageResource(R.drawable.ic_heart_full);
                title.setText(R.string.likes);
                recyclerView.setAdapter(getLikesAdapter(FirebaseUtil.getPostLikesListRef(strPostKey)));
                break;
            case Common.WHICH_COMMENTS:
                LayoutUtil.setColorImage(image, R.drawable.ic_message, R.color.colorAccentBlue);
                title.setText(R.string.comments);
                recyclerView.setAdapter(getCommentsAdapter(FirebaseUtil.getPostCommentsRef(strPostKey)));
                break;
            default:
                break;
        }
    }

    private FirebaseRecyclerAdapter<Boolean, ViewHolderLikes> getLikesAdapter(Query query) {
        return new FirebaseRecyclerAdapter<Boolean, ViewHolderLikes>(Boolean.class, R.layout.recycler_feed_post_likes, ViewHolderLikes.class, query) {
            @Override
            public void populateViewHolder(final ViewHolderLikes postViewHolder, Boolean user, final int position) {
                String key = this.getRef(position).getKey();
                FirebaseUtil.getAllUsersRef().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        postViewHolder.bindViews(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
    }

    private FirebaseRecyclerAdapter<Comment, ViewHolderComments> getCommentsAdapter(final Query query) {
        return new FirebaseRecyclerAdapter<Comment, ViewHolderComments>(Comment.class, R.layout.recycler_feed_post_comments, ViewHolderComments.class, query) {
            @Override
            public void populateViewHolder(final ViewHolderComments viewHolder, final Comment comment, final int position) {
                final FirebaseRecyclerAdapter adapter = this;
                FirebaseUtil.getAllUsersRef().child(comment.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        comment.setUsers(user);
                        viewHolder.bindViews(comment, adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
    }

    public static class ViewHolderComments extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Comment comment;
        private CircleImageView circleImage;
        private TextView txtName;
        private TextView txtComment;
        private TextView txtTimeStamp;
        private ImageView imageMenu;
        private FirebaseRecyclerAdapter mAdapter;

        public ViewHolderComments(View itemView) {
            super(itemView);
            circleImage = itemView.findViewById(R.id.circleImage);
            txtName = itemView.findViewById(R.id.name);
            txtComment = itemView.findViewById(R.id.comment);
            txtTimeStamp = itemView.findViewById(R.id.timeStamp);
            imageMenu = itemView.findViewById(R.id.imageMenu);
        }

        void bindViews(Comment comment, FirebaseRecyclerAdapter adapter) {
            this.mAdapter = adapter;
            this.comment = comment;
            txtName.setText(comment.getUsers().getDisplayName());
            txtComment.setText(comment.getText());
            txtTimeStamp.setText(DateUtils.getRelativeTimeSpanString((long) comment.getTimestamp()).toString());
            imageMenu.setVisibility(comment.getUid().equalsIgnoreCase(FirebaseUtil.getUser().getUid()) ? View.VISIBLE : View.GONE);
            Picasso.with(itemView.getContext())
                    .load(comment.getUsers().getImageUrl())
                    .error(LayoutUtil.getDrawableMutate(itemView.getContext(), R.drawable.ic_account_circle_white, R.color.greyIconNormal))
                    .into(circleImage);
            imageMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == imageMenu) {
                PopupMenu popup = new PopupMenu(view.getContext(), view, GravityCompat.START);
                popup.getMenuInflater().inflate(R.menu.menu_feed, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String postKey = mAdapter.getRef(getAdapterPosition()).getKey();
                        FirebaseUtil.getPostCommentsRef(comment.getCurrentPostKey()).child(postKey).removeValue();
                        return true;
                    }
                });
                popup.show();
            }
        }
    }

    public static class ViewHolderLikes extends RecyclerView.ViewHolder {
        private CircleImageView circleImage;
        private TextView name;

        public ViewHolderLikes(View itemView) {
            super(itemView);
            circleImage = itemView.findViewById(R.id.circleImage);
            name = itemView.findViewById(R.id.name);
        }

        void bindViews(User user) {
            name.setText(user.getDisplayName());
            Picasso.with(itemView.getContext())
                    .load(user.getImageUrl())
                    .error(LayoutUtil.getDrawableMutate(itemView.getContext(), R.drawable.ic_account_circle_white, R.color.greyIconNormal))
                    .into(circleImage);
        }
    }
}
