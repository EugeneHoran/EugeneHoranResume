package com.resume.horan.eugene.eugenehoranresume.main.resume;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerBulletEducationBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerEducationBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Education;
import com.resume.horan.eugene.eugenehoranresume.model.EducationActivity;

import java.util.ArrayList;
import java.util.List;

public class ResumeEducationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_EDUCATION = 1;
    private static final int HOLDER_EDUCATION_BULLET = 2;

    private List<Object> mObjectList = new ArrayList<>();

    ResumeEducationRecyclerAdapter() {
    }

    void setItems(List<Object> objectList) {
        mObjectList.clear();
        mObjectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectList.get(position) instanceof Education) {
            return HOLDER_EDUCATION;
        } else if (mObjectList.get(position) instanceof EducationActivity) {
            return HOLDER_EDUCATION_BULLET;
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
            case HOLDER_EDUCATION:
                return new ViewHolderEducation(RecyclerEducationBinding.inflate(layoutInflater, parent, false));
            case HOLDER_EDUCATION_BULLET:
                return new ViewHolderBullet(RecyclerBulletEducationBinding.inflate(layoutInflater, parent, false));
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
            mHolder.bindItems();
        } else if (holder instanceof ViewHolderBullet) {
            ViewHolderBullet mHolder = (ViewHolderBullet) holder;
            mHolder.initItems();
        }
        holder.itemView.setTag(this);
    }

    private class ViewHolderEducation extends RecyclerView.ViewHolder {
        private RecyclerEducationBinding binding;

        ViewHolderEducation(RecyclerEducationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItems() {
            Education object = (Education) mObjectList.get(getAdapterPosition());
            object.setPosition(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }

    private class ViewHolderBullet extends RecyclerView.ViewHolder {
        private RecyclerBulletEducationBinding binding;

        ViewHolderBullet(RecyclerBulletEducationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void initItems() {
            EducationActivity object = (EducationActivity) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }
}

