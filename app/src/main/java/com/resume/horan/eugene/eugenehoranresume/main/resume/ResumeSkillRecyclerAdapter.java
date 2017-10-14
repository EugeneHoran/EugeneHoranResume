package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerHeaderSkillBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerSkillsBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Skill;
import com.resume.horan.eugene.eugenehoranresume.model.SkillItem;

import java.util.ArrayList;
import java.util.List;

public class ResumeSkillRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_HEADER = 1;
    private static final int HOLDER_SKILL = 2;

    private List<Object> mObjectList = new ArrayList<>();

    public ResumeSkillRecyclerAdapter() {
    }

    public void setItems(List<Object> objectList) {
        mObjectList.clear();
        mObjectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectList.get(position) instanceof Skill) {
            return HOLDER_HEADER;
        } else if (mObjectList.get(position) instanceof SkillItem) {
            return HOLDER_SKILL;
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
                return new ViewHolderSkillHeader(RecyclerHeaderSkillBinding.inflate(layoutInflater, parent, false));
            case HOLDER_SKILL:
                return new ViewHolderSkill(RecyclerSkillsBinding.inflate(layoutInflater, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderSkillHeader) {
            ViewHolderSkillHeader mHolder = (ViewHolderSkillHeader) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderSkill) {
            ViewHolderSkill mHolder = (ViewHolderSkill) holder;
            mHolder.initItems();
        }
        Animation anim = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_set_fade_in_slide_up_recycler);
        holder.itemView.startAnimation(anim);
        holder.itemView.setTag(this);
    }

    private class ViewHolderSkillHeader extends RecyclerView.ViewHolder {
        private RecyclerHeaderSkillBinding binding;

        ViewHolderSkillHeader(RecyclerHeaderSkillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            Skill object = (Skill) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }

    private class ViewHolderSkill extends RecyclerView.ViewHolder {

        private RecyclerSkillsBinding binding;

        ViewHolderSkill(RecyclerSkillsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void initItems() {
            SkillItem object = (SkillItem) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }
}
