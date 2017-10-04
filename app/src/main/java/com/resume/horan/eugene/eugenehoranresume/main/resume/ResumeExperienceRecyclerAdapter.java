package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.Account;
import com.resume.horan.eugene.eugenehoranresume.model.Bullet;
import com.resume.horan.eugene.eugenehoranresume.model.DividerFiller;
import com.resume.horan.eugene.eugenehoranresume.model.Experience;
import com.resume.horan.eugene.eugenehoranresume.model.Header;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

import java.util.ArrayList;
import java.util.List;


public class ResumeExperienceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_HEADER = 1;
    private static final int HOLDER_ACCOUNT = 2;
    private static final int HOLDER_EXPERIENCE = 3;
    private static final int HOLDER_BULLET = 4;
    private static final int HOLDER_DIVIDER = 5;

    private List<Object> mObjectList = new ArrayList<>();
    private Listener mListener;

    public interface Listener {
        void onItemClicked(String url);
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
        } else if (mObjectList.get(position) instanceof Account) {
            return HOLDER_ACCOUNT;
        } else if (mObjectList.get(position) instanceof Experience) {
            return HOLDER_EXPERIENCE;
        } else if (mObjectList.get(position) instanceof Bullet) {
            return HOLDER_BULLET;
        } else if (mObjectList.get(position) instanceof DividerFiller) {
            return HOLDER_DIVIDER;
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
            case HOLDER_ACCOUNT:
                return new ViewHolderAccount(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_account, parent, false));
            case HOLDER_EXPERIENCE:
                return new ViewHolderExperience(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_experience, parent, false));
            case HOLDER_BULLET:
                return new ViewHolderBullet(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_bullet, parent, false));
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
        } else if (holder instanceof ViewHolderAccount) {
            ViewHolderAccount mHolder = (ViewHolderAccount) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderExperience) {
            ViewHolderExperience mHolder = (ViewHolderExperience) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderBullet) {
            ViewHolderBullet mHolder = (ViewHolderBullet) holder;
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

    private class ViewHolderAccount extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextAccount;

        ViewHolderAccount(View v) {
            super(v);
            mTextAccount = v.findViewById(R.id.textAccount);
            mTextAccount.setOnClickListener(this);
        }

        private Account mAccount;

        void initItems() {
            mAccount = (Account) mObjectList.get(getAdapterPosition());
            mTextAccount.setText(mAccount.getName());
            mTextAccount.setCompoundDrawablesWithIntrinsicBounds(mAccount.getImage(mAccount.getType()), 0, R.drawable.ic_chevron_right, 0);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClicked(mAccount.getUrl());
            }
        }
    }


    private class ViewHolderExperience extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextPosition;
        private TextView mTextExperience;
        private TextView mTextDateRange;
        private ImageView mImageLink;

        ViewHolderExperience(View v) {
            super(v);
            mTextPosition = v.findViewById(R.id.textPosition);
            mTextExperience = v.findViewById(R.id.textExperience);
            mTextDateRange = v.findViewById(R.id.textDateRange);
            mImageLink = v.findViewById(R.id.imageLink);
            mImageLink.setOnClickListener(this);
        }

        private Experience mExperience;

        void initItems() {
            mExperience = (Experience) mObjectList.get(getAdapterPosition());
            mTextPosition.setText(mExperience.getPosition());
            mTextExperience.setText(mExperience.getPositionFormatted());
            mTextDateRange.setText(mExperience.getDateRange());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClicked(mExperience.getLinkApp());
            }
        }
    }

    private class ViewHolderBullet extends RecyclerView.ViewHolder {
        private TextView mTextBullet;

        ViewHolderBullet(View v) {
            super(v);
            mTextBullet = v.findViewById(R.id.textBullet);
        }

        void initItems() {
            Bullet mExperience = (Bullet) mObjectList.get(getAdapterPosition());
            mTextBullet.setText(mExperience.getBullet());
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
            DividerFiller mDividerFiller = (DividerFiller) mObjectList.get(getAdapterPosition());
            if (mDividerFiller.getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_NO_SPACE)) {
                mSpace.setVisibility(View.GONE);
                mLine.setVisibility(View.VISIBLE);
            } else if (mDividerFiller.getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_WITH_SPACE)) {
                mSpace.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.VISIBLE);
            } else if (mDividerFiller.getFillerBreak().equalsIgnoreCase(Common.DIVIDER_NO_LINE_WITH_SPACE)) {
                mSpace.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.GONE);
            } else {
                mSpace.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.VISIBLE);
            }
        }
    }
}
