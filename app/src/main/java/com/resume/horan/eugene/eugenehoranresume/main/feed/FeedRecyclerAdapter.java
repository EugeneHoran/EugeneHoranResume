package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.base.BasePostViewHolder;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedImageBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedMessageBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedMessageImageBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedNewPostBinding;
import com.resume.horan.eugene.eugenehoranresume.model.FeedNewPost;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_NEW_POST = 2;
    private static final int HOLDER_POST_PHOTO = 3;
    private static final int HOLDER_POST_MESSAGE = 4;
    private static final int HOLDER_POST_MESSAGE_PHOTO = 5;
    private ViewHolderPostImage mHolderImage;
    private ViewHolderPostMessage mHolderMessage;
    private ViewHolderPostMessageImage mHolderImageMessage;

    private List<Object> objectList = new ArrayList<>();
    private Listener listener;

    public List<Object> getObjectList() {
        return objectList;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onAddImageClick();

        void onLikedClicked(Post post);

        void onShowLikesClicked(Post post);

        void onShowCommentsClicked(Post post);

        void onAddCommentClicked(Post post);
    }

    public void setItems(List<Object> objectList) {
        this.objectList.clear();
        this.objectList.addAll(objectList);
        notifyDataSetChanged();
    }

    public void addItems(List<Object> objectList) {
        this.objectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof FeedNewPost) {
            return HOLDER_NEW_POST;
        } else if (objectList.get(position) instanceof Post) {
            Post post = (Post) objectList.get(position);
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
                return new ViewHolderNewPost(RecyclerFeedNewPostBinding.inflate(layoutInflater, parent, false));
            case HOLDER_POST_MESSAGE:
                return new ViewHolderPostMessage(RecyclerFeedMessageBinding.inflate(layoutInflater, parent, false));
            case HOLDER_POST_PHOTO:
                return new ViewHolderPostImage(RecyclerFeedImageBinding.inflate(layoutInflater, parent, false));
            case HOLDER_POST_MESSAGE_PHOTO:
                return new ViewHolderPostMessageImage(RecyclerFeedMessageImageBinding.inflate(layoutInflater, parent, false));
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
            mHolderImage = (ViewHolderPostImage) holder;
            mHolderImage.bindItem();
        } else if (holder instanceof ViewHolderPostMessage) {
            mHolderMessage = (ViewHolderPostMessage) holder;
            mHolderMessage.bindItem();
        } else if (holder instanceof ViewHolderPostMessageImage) {
            mHolderImageMessage = (ViewHolderPostMessageImage) holder;
            mHolderImageMessage.bindItem();
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    public void onDestroy() {
        if (mHolderImage != null) {
            mHolderImage.cancelRef();
        }
        if (mHolderMessage != null) {
            mHolderMessage.cancelRef();
        }
        if (mHolderImageMessage != null) {
            mHolderImageMessage.cancelRef();
        }
    }

    /**
     * ViewHolders
     */
    public class ViewHolderNewPost extends RecyclerView.ViewHolder {
        private RecyclerFeedNewPostBinding binding;

        ViewHolderNewPost(RecyclerFeedNewPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            FeedNewPost object = (FeedNewPost) objectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.setHandler(this);
        }

        @SuppressWarnings("unused")
        public void onNewPostClicked(View view) {
            if (listener != null) {
                listener.onAddImageClick();
            }
        }
    }

    private class ViewHolderPostMessage extends BasePostViewHolder {
        private RecyclerFeedMessageBinding binding;

        ViewHolderPostMessage(RecyclerFeedMessageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        void bindItem() {
            Post object = (Post) objectList.get(getAdapterPosition());
            bindBaseViews(listener, object);
            binding.setObject(object);
            binding.setHolderParent(this);
            binding.executePendingBindings();
        }
    }

    private class ViewHolderPostImage extends BasePostViewHolder {
        private RecyclerFeedImageBinding binding;

        ViewHolderPostImage(RecyclerFeedImageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        void bindItem() {
            Post object = (Post) objectList.get(getAdapterPosition());
            bindBaseViews(listener, object);
            binding.setHolderParent(this);
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }

    private class ViewHolderPostMessageImage extends BasePostViewHolder {
        private RecyclerFeedMessageImageBinding binding;

        ViewHolderPostMessageImage(RecyclerFeedMessageImageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        void bindItem() {
            Post object = (Post) objectList.get(getAdapterPosition());
            bindBaseViews(listener, object);
            binding.setHolderParent(this);
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }
}
