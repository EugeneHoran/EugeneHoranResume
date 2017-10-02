package com.resume.horan.eugene.eugenehoranresume.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.AlbumImage;
import com.resume.horan.eugene.eugenehoranresume.model.DividerFiller;
import com.resume.horan.eugene.eugenehoranresume.model.Goals;
import com.resume.horan.eugene.eugenehoranresume.model.Header;
import com.resume.horan.eugene.eugenehoranresume.model.LoaderObject;
import com.resume.horan.eugene.eugenehoranresume.model.SocialMedia;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AboutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    public static final int HOLDER_HEADER = 1;
    public static final int HOLDER_GOAL = 2;
    public static final int HOLDER_SOCIAL_MEDIA = 3;
    public static final int HOLDER_IMAGES = 4;
    public static final int HOLDER_IMAGES_LARGE = 5;
    public static final int HOLDER_LOADER = 6;
    public static final int HOLDER_DIVIDER = 7;

    private List<Object> mObjectList = new ArrayList<>();

    private Listener mListener;

    public interface Listener {
        void onLinkClicked(String url);

        void onImageClicked(AlbumImage albumImage);
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
        switch (viewType) {
            case HOLDER_ERROR:
                return null;
            case HOLDER_HEADER:
                return new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_header, parent, false));
            case HOLDER_GOAL:
                return new ViewHolderGoals(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_about_goals, parent, false));
            case HOLDER_SOCIAL_MEDIA:
                return new ViewHolderSocialMedia(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_about_social_media, parent, false));
            case HOLDER_IMAGES_LARGE:
                return new ViewHolderImageLarge(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_about_images_large, parent, false));
            case HOLDER_IMAGES:
                return new ViewHolderImages(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_about_images_normal, parent, false));
            case HOLDER_LOADER:
                return new ViewHolderLoader(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_loader, parent, false));
            case HOLDER_DIVIDER:
                return new ViewHolderDivider(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_divider, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader mHolder = (ViewHolderHeader) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderGoals) {
            ViewHolderGoals mHolder = (ViewHolderGoals) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderSocialMedia) {
            ViewHolderSocialMedia mHolder = (ViewHolderSocialMedia) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderImageLarge) {
            ViewHolderImageLarge mHolder = (ViewHolderImageLarge) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderImages) {
            ViewHolderImages mHolder = (ViewHolderImages) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderLoader) {
            ViewHolderLoader mHolder = (ViewHolderLoader) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderDivider) {
            ViewHolderDivider mHolder = (ViewHolderDivider) holder;
            mHolder.initItems();
        }
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }

    /**
     * ViewHolders
     */

    private class ViewHolderLoader extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextLoadMore;
        private LoaderObject object;

        ViewHolderLoader(View v) {
            super(v);
            mTextLoadMore = v.findViewById(R.id.textLoadMore);
            mTextLoadMore.setOnClickListener(this);
        }

        void initItems() {
            object = (LoaderObject) mObjectList.get(getAdapterPosition());
            mTextLoadMore.setText(object.isExpanded() ? R.string.show_less : R.string.show_more);
            mTextLoadMore.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    !object.isExpanded()
                            ? ContextCompat.getDrawable(mTextLoadMore.getContext(), R.drawable.ic_expand_more)
                            : ContextCompat.getDrawable(mTextLoadMore.getContext(), R.drawable.ic_expand_less),
                    null);
        }

        @Override
        public void onClick(View view) {
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

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        private TextView mTextHeader;

        ViewHolderHeader(View v) {
            super(v);
            mTextHeader = v.findViewById(R.id.textHeader);
        }

        void initItems() {
            Header object = (Header) mObjectList.get(getAdapterPosition());
            mTextHeader.setText(object.getHeaderTitle());
        }
    }

    private class ViewHolderGoals extends RecyclerView.ViewHolder {
        private TextView mTextGoal;

        ViewHolderGoals(View v) {
            super(v);
            mTextGoal = v.findViewById(R.id.textGoal);
        }

        void initItems() {
            Goals object = (Goals) mObjectList.get(getAdapterPosition());
            mTextGoal.setText(object.getGoal());
        }
    }


    private class ViewHolderSocialMedia extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SocialMedia object;
        private View mViewSocialMediaHolder;
        private TextView mTextSocialMedia;
        private ImageView mImageLink;

        ViewHolderSocialMedia(View v) {
            super(v);
            mViewSocialMediaHolder = v.findViewById(R.id.viewSocialMediaHolder);
            mTextSocialMedia = v.findViewById(R.id.textSocialMedia);
            mImageLink = v.findViewById(R.id.imageLink);
            mViewSocialMediaHolder.setOnClickListener(this);
            mImageLink.setOnClickListener(this);
        }

        void initItems() {
            object = (SocialMedia) mObjectList.get(getAdapterPosition());
            mTextSocialMedia.setText(object.getAccount());
            mTextSocialMedia.setCompoundDrawablesWithIntrinsicBounds(object.getLogo(), 0, 0, 0);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onLinkClicked(object.getUrl());
            }
        }
    }

    private class ViewHolderImageLarge extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AlbumImage object;
        private CardView mCardImageHolder;
        private ImageView mImagePhoto;
        private TextView mTextImageTitle;


        ViewHolderImageLarge(View v) {
            super(v);
            mCardImageHolder = v.findViewById(R.id.cardImageHolder);
            mImagePhoto = v.findViewById(R.id.imagePhoto);
            mTextImageTitle = v.findViewById(R.id.textImageTitle);
            mImagePhoto.setOnClickListener(this);
        }

        void initItems() {
            object = (AlbumImage) mObjectList.get(getAdapterPosition());
            mTextImageTitle.setText(object.getImageName());
            mCardImageHolder.setCardBackgroundColor(Color.parseColor(object.getImageColorHEX()));
            Picasso.with(mImagePhoto.getContext())
                    .load(object.getImageUrl())
                    .rotate(object.getRotate())
                    .resize(object.getFormattedWidth(), object.getFormattedHeight())
                    .onlyScaleDown()
                    .centerInside()
                    .into(mImagePhoto);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onImageClicked(object);
            }
        }
    }

    private class ViewHolderImages extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AlbumImage object;
        private CardView mCardImageHolder;
        private ImageView mImagePhoto;


        ViewHolderImages(View v) {
            super(v);
            mCardImageHolder = v.findViewById(R.id.cardImageHolder);
            mImagePhoto = v.findViewById(R.id.imagePhoto);
            mImagePhoto.setOnClickListener(this);
        }

        void initItems() {
            object = (AlbumImage) mObjectList.get(getAdapterPosition());
            mCardImageHolder.setCardBackgroundColor(Color.parseColor(object.getImageColorHEX()));
            Picasso.with(mImagePhoto.getContext())
                    .load(object.getImageUrl())
                    .rotate(object.getRotate())
                    .resize(object.getFormattedWidth(), object.getFormattedHeight())
                    .onlyScaleDown()
                    .centerInside()
                    .into(mImagePhoto);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onImageClicked(object);
            }
        }
    }

    private class ViewHolderDivider extends RecyclerView.ViewHolder {
        private View mSpace;
        private View mLine;

        ViewHolderDivider(View v) {
            super(v);
            mSpace = v.findViewById(R.id.space);
            mLine = v.findViewById(R.id.line);
        }

        void initItems() {
            DividerFiller object = (DividerFiller) mObjectList.get(getAdapterPosition());
            if (object.getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_NO_SPACE)) {
                mSpace.setVisibility(View.GONE);
                mLine.setVisibility(View.VISIBLE);
            } else if (object.getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_WITH_SPACE)) {
                mSpace.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.VISIBLE);
            } else if (object.getFillerBreak().equalsIgnoreCase(Common.DIVIDER_NO_LINE_WITH_SPACE)) {
                mSpace.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.GONE);
            } else {
                mSpace.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.VISIBLE);
            }
        }
    }
}
