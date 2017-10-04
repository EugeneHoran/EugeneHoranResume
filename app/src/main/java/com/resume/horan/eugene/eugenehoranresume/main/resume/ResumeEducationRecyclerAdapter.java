package com.resume.horan.eugene.eugenehoranresume.main.resume;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.Education;
import com.resume.horan.eugene.eugenehoranresume.model.EducationActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ResumeEducationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_EDUCATION = 1;
    private static final int HOLDER_ACTIVITY = 2;

    private List<Object> mObjectList = new ArrayList<>();

    public ResumeEducationRecyclerAdapter() {
    }

    public void setItems(List<Object> objectList) {
        mObjectList.clear();
        mObjectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectList.get(position) instanceof Education) {
            return HOLDER_EDUCATION;
        } else if (mObjectList.get(position) instanceof EducationActivity) {
            return HOLDER_ACTIVITY;
        } else {
            return HOLDER_ERROR;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HOLDER_ERROR:
                return null;
            case HOLDER_EDUCATION:
                return new ViewHolderEducation(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_education, parent, false));
            case HOLDER_ACTIVITY:
                return new ViewHolderBullet(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_bullet_education, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderEducation) {
            ViewHolderEducation mHolder = (ViewHolderEducation) holder;
            mHolder.initItems();
        } else if (holder instanceof ViewHolderBullet) {
            ViewHolderBullet mHolder = (ViewHolderBullet) holder;
            mHolder.initItems();
        }
    }

    private class ViewHolderEducation extends RecyclerView.ViewHolder {
        private ImageView mImageLogo;
        private TextView mTextUniversity;
        private TextView mTextLocation;
        private TextView mTextDateRange;
        private TextView mTextMajor;
        private View mViewLine;

        ViewHolderEducation(View v) {
            super(v);
            mImageLogo = v.findViewById(R.id.imageLogo);
            mTextUniversity = v.findViewById(R.id.textUniversity);
            mTextLocation = v.findViewById(R.id.textLocation);
            mTextDateRange = v.findViewById(R.id.textDateRange);
            mTextMajor = v.findViewById(R.id.textMajor);
            mViewLine = v.findViewById(R.id.viewLine);
        }

        void initItems() {
            Education object = (Education) mObjectList.get(getAdapterPosition());
            Picasso.with(itemView.getContext()).load(object.getLogoUrl()).into(mImageLogo);
            mTextUniversity.setText(object.getUniversity());
            mTextLocation.setText(object.getLocation());
            mTextDateRange.setText(object.getDateRange());
            mTextMajor.setText(object.getMajor());
            mViewLine.setVisibility(getAdapterPosition() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    private class ViewHolderBullet extends RecyclerView.ViewHolder {
        private TextView mTextBullet;

        ViewHolderBullet(View v) {
            super(v);
            mTextBullet = v.findViewById(R.id.textBullet);
        }

        void initItems() {
            EducationActivity mExperience = (EducationActivity) mObjectList.get(getAdapterPosition());
            mTextBullet.setText(mExperience.getDescription());
        }
    }
}

