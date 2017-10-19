package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.BasePostViewHolder;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedImageBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedMessageBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedMessageImageBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedNewPostBinding;
import com.resume.horan.eugene.eugenehoranresume.model.FeedNewPost;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.ui.LayoutUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all") // Getting warnings about public and private referenceso
public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HOLDER_ERROR = 0;
    public static final int HOLDER_NEW_POST = 2;
    public static final int HOLDER_POST_PHOTO = 3;
    public static final int HOLDER_POST_MESSAGE = 4;
    public static final int HOLDER_POST_MESSAGE_PHOTO = 5;

    private ViewHolderPostImage mHolderImage;
    private ViewHolderPostMessage mHolderMessage;
    private ViewHolderPostMessageImage mHolderImageMessage;

    private List<Object> objectList = new ArrayList<>();


    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onAddImageClick();

        void onLikedClicked(Post post);

        void onShowLikesClicked(Post post);

        void onShowCommentsClicked(Post post);

        void onAddCommentClicked(Post post);

        void onViewUserProfileClicked(String uid);
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
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    public List<Object> getObjectList() {
        return objectList;
    }

    @Override
    public int getItemCount() {
        return objectList.size();
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
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderNewPost) {
            ViewHolderNewPost mHolder = (ViewHolderNewPost) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderPostImage) {
            mHolderImage = (ViewHolderPostImage) holder;
            mHolderImage.bindItem((Post) objectList.get(position), listener);
        } else if (holder instanceof ViewHolderPostMessage) {
            mHolderMessage = (ViewHolderPostMessage) holder;
            mHolderMessage.bindItem((Post) objectList.get(position), listener);
        } else if (holder instanceof ViewHolderPostMessageImage) {
            mHolderImageMessage = (ViewHolderPostMessageImage) holder;
            mHolderImageMessage.bindItem((Post) objectList.get(position), listener);
        }
        holder.itemView.setTag(this);
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

    public static class ViewHolderPostMessage extends BasePostViewHolder {
        private RecyclerFeedMessageBinding binding;

        public ViewHolderPostMessage(RecyclerFeedMessageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        public void bindItem(Post object, Listener listener) {
            bindBaseViews(listener, object);
            binding.setObject(object);
            binding.setHolderParent(this);
            binding.executePendingBindings();
        }
    }

    public static class ViewHolderPostImage extends BasePostViewHolder {
        private RecyclerFeedImageBinding binding;

        public ViewHolderPostImage(RecyclerFeedImageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        public void bindItem(Post object, Listener listener) {
            bindBaseViews(listener, object);
            binding.setHolderParent(this);
            binding.setObject(object);
            binding.executePendingBindings();
            if (LayoutUtil.isL()) {
                setAnimator(binding.image);
            }
        }

        @TargetApi(21)
        private void setAnimator(View view) {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(binding.getRoot().getContext(), R.drawable.anim_touch_elevate);
            view.setStateListAnimator(sla);
        }
    }

    public static class ViewHolderPostMessageImage extends BasePostViewHolder {
        private RecyclerFeedMessageImageBinding binding;

        public ViewHolderPostMessageImage(RecyclerFeedMessageImageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        public void bindItem(Post object, Listener listener) {
            bindBaseViews(listener, object);
            binding.setHolderParent(this);
            binding.setObject(object);
            binding.executePendingBindings();
            if (LayoutUtil.isL()) {
                setAnimator(binding.image);
            }
        }

        @TargetApi(21)
        private void setAnimator(View view) {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(binding.getRoot().getContext(), R.drawable.anim_touch_elevate);
            view.setStateListAnimator(sla);
        }
    }
}
