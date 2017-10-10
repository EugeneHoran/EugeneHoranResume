package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.BasePostViewHolder;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerNewPostBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerPostImageBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerPostMessageBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerPostMessageImageBinding;
import com.resume.horan.eugene.eugenehoranresume.model.FeedNewPost;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.ui.viewimage.ViewImageActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.LayoutUtil;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_NEW_POST = 2;
    private static final int HOLDER_POST_PHOTO = 3;
    private static final int HOLDER_POST_MESSAGE = 4;
    private static final int HOLDER_POST_MESSAGE_PHOTO = 5;
    private List<Object> mObjectList = new ArrayList<>();
    private Listener mListener;

    FeedRecyclerAdapter() {
    }

    public List<Object> getObjectList() {
        return mObjectList;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setItems(List<Object> objectList) {
        mObjectList.clear();
        mObjectList.addAll(objectList);
        notifyDataSetChanged();
    }

    public void addItems(List<Object> objectList) {
        mObjectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectList.get(position) instanceof FeedNewPost) {
            return HOLDER_NEW_POST;
        } else if (mObjectList.get(position) instanceof Post) {
            Post post = (Post) mObjectList.get(position);
            if (post.getType() == Common.TYPE_POST_IMAGE) {
                return HOLDER_POST_PHOTO;
            } else if (post.getType() == Common.TYPE_POST_MESSAGE) {
                return HOLDER_POST_MESSAGE;
            } else if (post.getType() == Common.TYPE_POST_MESSAGE_IMAGE) {
                return HOLDER_POST_MESSAGE_PHOTO;
            } else {
                return HOLDER_ERROR;
            }
        } else {
            return HOLDER_ERROR;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HOLDER_ERROR:
                return null;
            case HOLDER_NEW_POST:
                return new ViewHolderNewPost(RecyclerNewPostBinding.inflate(layoutInflater, parent, false));
            case HOLDER_POST_PHOTO:
                return new ViewHolderPostImage(RecyclerPostImageBinding.inflate(layoutInflater, parent, false));
            case HOLDER_POST_MESSAGE:
                return new ViewHolderPostMessage(RecyclerPostMessageBinding.inflate(layoutInflater, parent, false));
            case HOLDER_POST_MESSAGE_PHOTO:
                return new ViewHolderPostMessageImage(RecyclerPostMessageImageBinding.inflate(layoutInflater, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderNewPost) {
            ViewHolderNewPost mHolder = (ViewHolderNewPost) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderPostImage) {
            ViewHolderPostImage mHolder = (ViewHolderPostImage) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderPostMessage) {
            ViewHolderPostMessage mHolder = (ViewHolderPostMessage) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderPostMessageImage) {
            ViewHolderPostMessageImage mHolder = (ViewHolderPostMessageImage) holder;
            mHolder.bindItem();
        }
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }


    /**
     * ViewHolders
     */
    public class ViewHolderNewPost extends RecyclerView.ViewHolder {
        private RecyclerNewPostBinding binding;

        ViewHolderNewPost(RecyclerNewPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            FeedNewPost object = (FeedNewPost) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.setHandler(this);
        }

        public void onAddImageClick(View view) {
            if (mListener != null) {
                mListener.onAddImageClick();
            }
        }
    }

    private class ViewHolderPostMessage extends BasePostViewHolder implements View.OnClickListener {
        private RecyclerPostMessageBinding binding;
        private Post object;

        ViewHolderPostMessage(RecyclerPostMessageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        void bindItem() {
            object = (Post) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            setPost(object);
            binding.executePendingBindings();
            binding.footer.like.setOnClickListener(this);
            binding.header.imageMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == binding.footer.like) {
                mListener.onLikedClicked(object);
            } else if (v == binding.header.imageMenu) {
                onMenuClick(v, object);
            }
        }

        @Override
        public void setLikes(String likes) {
            binding.footer.likes.setText(likes);
        }

        @Override
        public void setLiked(boolean liked) {
            if (liked) {
                binding.footer.like.setImageResource(R.drawable.ic_heart_full);
            } else {
                binding.footer.like.setImageResource(R.drawable.ic_heart_empty);
            }
        }
    }

    private class ViewHolderPostImage extends BasePostViewHolder implements View.OnClickListener {
        private RecyclerPostImageBinding binding;
        private Post object;

        ViewHolderPostImage(RecyclerPostImageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        void bindItem() {
            object = (Post) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            setPost(object);
            binding.executePendingBindings();
            binding.image.setOnClickListener(this);
            binding.footer.like.setOnClickListener(this);
            binding.header.imageMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == binding.image) {
                imageClicked(v, object);
            } else if (v == binding.footer.like) {
                mListener.onLikedClicked(object);
            } else if (v == binding.header.imageMenu) {
                onMenuClick(v, object);
            }
        }

        @Override
        public void setLikes(String likes) {
            binding.footer.likes.setText(likes);
        }

        @Override
        public void setLiked(boolean liked) {
            if (liked) {
                binding.footer.like.setImageResource(R.drawable.ic_heart_full);
            } else {
                binding.footer.like.setImageResource(R.drawable.ic_heart_empty);
            }
        }
    }

    private class ViewHolderPostMessageImage extends BasePostViewHolder implements View.OnClickListener {
        private RecyclerPostMessageImageBinding binding;
        private Post object;

        ViewHolderPostMessageImage(RecyclerPostMessageImageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        void bindItem() {
            object = (Post) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            setPost(object);
            binding.executePendingBindings();
            binding.footer.like.setOnClickListener(this);
            binding.header.imageMenu.setOnClickListener(this);
            binding.image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == binding.image) {
                imageClicked(v, object);
            } else if (v == binding.footer.like) {
                mListener.onLikedClicked(object);
            } else if (v == binding.header.imageMenu) {
                onMenuClick(v, object);
            }
        }

        @Override
        public void setLikes(String likes) {
            binding.footer.likes.setText(likes);
        }

        @Override
        public void setLiked(boolean liked) {
            if (liked) {
                binding.footer.like.setImageResource(R.drawable.ic_heart_full);
            } else {
                binding.footer.like.setImageResource(R.drawable.ic_heart_empty);
            }
        }
    }

    /**
     * Handle Shared Functions
     */
    public interface Listener {
        void onAddImageClick();

        void onLikedClicked(Post post);
    }

    @SuppressWarnings("unchecked")
    private void imageClicked(View view, Post post) {
        Activity activity = (Activity) view.getContext();
        Intent intent = new Intent(activity, ViewImageActivity.class);
        intent.putExtra(Common.ARG_IMAGE_STRING, post.getFull_url());
        if (LayoutUtil.isM()) {
            Pair<View, String> p2 = Pair.create(view, "image");
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p2);
            activity.startActivity(intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }


    private void onMenuClick(final View view, final Post post) {
        PopupMenu popup = new PopupMenu(view.getContext(), view, GravityCompat.START);
        if (post.getAuthor().getUid().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            popup.getMenuInflater().inflate(R.menu.menu_feed, popup.getMenu());
        } else {
            popup.getMenuInflater().inflate(R.menu.menu_feed_report, popup.getMenu());
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("posts").child(post.getKey());
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
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
}
