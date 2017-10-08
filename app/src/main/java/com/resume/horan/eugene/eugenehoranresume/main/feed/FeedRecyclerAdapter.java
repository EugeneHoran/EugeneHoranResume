package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerNewPostBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerPostImageBinding;
import com.resume.horan.eugene.eugenehoranresume.model.FeedNewPost;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.SocialMedia;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    public static final int HOLDER_NEW_POST = 1;
    public static final int HOLDER_POST_PHOTO = 2;
    public static final int HOLDER_POST_MESSAGE = 3;

    private List<Object> mObjectList = new ArrayList<>();

    private Listener mListener;

    public interface Listener {
        void onAddImageClick();
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
            return HOLDER_POST_PHOTO;
        } else if (mObjectList.get(position) instanceof SocialMedia) {
            return HOLDER_POST_MESSAGE;
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

    private class ViewHolderPostImage extends RecyclerView.ViewHolder {
        private RecyclerPostImageBinding binding;

        ViewHolderPostImage(RecyclerPostImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            Post object = (Post) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }
}
