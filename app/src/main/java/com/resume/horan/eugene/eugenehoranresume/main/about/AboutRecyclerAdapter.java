package com.resume.horan.eugene.eugenehoranresume.main.about;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerAboutGoalsBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerAboutImagesLargeBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerAboutImagesSmallBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerAboutSocialMediaBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerDividerBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerHeaderBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerLoaderBinding;
import com.resume.horan.eugene.eugenehoranresume.model.AlbumImage;
import com.resume.horan.eugene.eugenehoranresume.model.DividerFiller;
import com.resume.horan.eugene.eugenehoranresume.model.Goals;
import com.resume.horan.eugene.eugenehoranresume.model.Header;
import com.resume.horan.eugene.eugenehoranresume.model.LoaderObject;
import com.resume.horan.eugene.eugenehoranresume.model.SocialMedia;
import com.resume.horan.eugene.eugenehoranresume.util.ui.LayoutUtil;

import java.util.ArrayList;
import java.util.List;

public class AboutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    static final int HOLDER_HEADER = 1;
    static final int HOLDER_GOAL = 2;
    static final int HOLDER_SOCIAL_MEDIA = 3;
    static final int HOLDER_IMAGES = 4;
    static final int HOLDER_IMAGES_LARGE = 5;
    static final int HOLDER_LOADER = 6;
    static final int HOLDER_DIVIDER = 7;

    private List<Object> mObjectList = new ArrayList<>();

    private Listener mListener;

    public interface Listener {
        void onLinkClicked(String url);

        void onImageClicked(AlbumImage albumImage, View image, View card);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setItems(List<Object> objectList) {
        mObjectList.clear();
        mObjectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectList.get(position) instanceof Header) {
            return HOLDER_HEADER;
        } else if (mObjectList.get(position) instanceof Goals) {
            return HOLDER_GOAL;
        } else if (mObjectList.get(position) instanceof SocialMedia) {
            return HOLDER_SOCIAL_MEDIA;
        } else if (mObjectList.get(position) instanceof AlbumImage) {
            AlbumImage albumImage = (AlbumImage) mObjectList.get(position);
            if (albumImage.getImageType() == 0) {
                return HOLDER_IMAGES_LARGE;
            } else {
                return HOLDER_IMAGES;
            }
        } else if (mObjectList.get(position) instanceof DividerFiller) {
            return HOLDER_DIVIDER;
        } else if (mObjectList.get(position) instanceof LoaderObject) {
            return HOLDER_LOADER;
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
            case HOLDER_HEADER:
                return new ViewHolderHeader(RecyclerHeaderBinding.inflate(layoutInflater, parent, false));
            case HOLDER_GOAL:
                return new ViewHolderGoals(RecyclerAboutGoalsBinding.inflate(layoutInflater, parent, false));
            case HOLDER_SOCIAL_MEDIA:
                return new ViewHolderSocialMedia(RecyclerAboutSocialMediaBinding.inflate(layoutInflater, parent, false));
            case HOLDER_IMAGES_LARGE:
                return new ViewHolderImageLarge(RecyclerAboutImagesLargeBinding.inflate(layoutInflater, parent, false));
            case HOLDER_IMAGES:
                return new ViewHolderImageSmall(RecyclerAboutImagesSmallBinding.inflate(layoutInflater, parent, false));
            case HOLDER_LOADER:
                return new ViewHolderLoader(RecyclerLoaderBinding.inflate(layoutInflater, parent, false));
            case HOLDER_DIVIDER:
                return new ViewHolderDivider(RecyclerDividerBinding.inflate(layoutInflater, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader mHolder = (ViewHolderHeader) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderGoals) {
            ViewHolderGoals mHolder = (ViewHolderGoals) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderSocialMedia) {
            ViewHolderSocialMedia mHolder = (ViewHolderSocialMedia) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderImageLarge) {
            ViewHolderImageLarge mHolder = (ViewHolderImageLarge) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderImageSmall) {
            ViewHolderImageSmall mHolder = (ViewHolderImageSmall) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderLoader) {
            ViewHolderLoader mHolder = (ViewHolderLoader) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderDivider) {
            ViewHolderDivider mHolder = (ViewHolderDivider) holder;
            mHolder.bindItem();
        }
        holder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }

    /**
     * ViewHolders
     */
    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        private RecyclerHeaderBinding binding;

        ViewHolderHeader(RecyclerHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            Header object = (Header) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
        }
    }

    public class ViewHolderLoader extends RecyclerView.ViewHolder {
        private RecyclerLoaderBinding binding;
        private LoaderObject object;

        ViewHolderLoader(RecyclerLoaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            object = (LoaderObject) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.setHolder(this);
            binding.executePendingBindings();
        }

        public void onExpandClicked(View view) {
            if (object.isExpanded()) {
                mObjectList.removeAll(object.getObjectList());
                notifyItemRangeRemoved(getAdapterPosition() - object.getObjectList().size() - 1, object.getObjectList().size());
                object.setExpanded(false);
            } else {
                mObjectList.addAll(getAdapterPosition() - 1, object.getObjectList());
                notifyItemRangeInserted(getAdapterPosition() - 1, object.getObjectList().size());
                object.setExpanded(true);
            }
            notifyItemChanged(getAdapterPosition());
        }
    }

    private class ViewHolderGoals extends RecyclerView.ViewHolder {
        private RecyclerAboutGoalsBinding binding;

        ViewHolderGoals(RecyclerAboutGoalsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            Goals object = (Goals) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }


    public class ViewHolderSocialMedia extends RecyclerView.ViewHolder {
        private RecyclerAboutSocialMediaBinding binding;
        private SocialMedia object;

        ViewHolderSocialMedia(RecyclerAboutSocialMediaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            object = (SocialMedia) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.setHandler(this);
            binding.executePendingBindings();
            if (LayoutUtil.isL()) {
                setAnimator(binding.viewSocialMediaHolder);
            }
        }

        @TargetApi(21)
        private void setAnimator(View view) {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(binding.getRoot().getContext(), R.drawable.anim_touch_elevate);
            view.setStateListAnimator(sla);
        }

        public void onLinkClicked(View view) {
            if (mListener != null) {
                mListener.onLinkClicked(object.getUrl());
            }
        }
    }

    public class ViewHolderImageLarge extends RecyclerView.ViewHolder {
        private RecyclerAboutImagesLargeBinding binding;
        private AlbumImage object;

        ViewHolderImageLarge(RecyclerAboutImagesLargeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            object = (AlbumImage) mObjectList.get(getAdapterPosition());
            binding.setHandler(this);
            binding.setObject(object);
            binding.executePendingBindings();
        }

        public void onImageClicked(View view) {
            if (mListener != null) {
                mListener.onImageClicked(object, binding.imagePhoto, binding.cardImageHolder);
            }
        }
    }

    public class ViewHolderImageSmall extends RecyclerView.ViewHolder {
        private RecyclerAboutImagesSmallBinding binding;
        private AlbumImage object;

        ViewHolderImageSmall(RecyclerAboutImagesSmallBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            object = (AlbumImage) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.setHandler(this);
            binding.executePendingBindings();
        }


        public void onImageClicked(View view) {
            if (mListener != null) {
                mListener.onImageClicked(object, binding.imagePhoto, binding.cardImageHolder);
            }
        }
    }

    private class ViewHolderDivider extends RecyclerView.ViewHolder {
        private RecyclerDividerBinding binding;

        ViewHolderDivider(RecyclerDividerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            DividerFiller object = (DividerFiller) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }
}
