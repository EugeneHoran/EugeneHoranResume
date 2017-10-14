package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerAccountBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerBulletBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerDividerBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerExperienceBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerHeaderBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Account;
import com.resume.horan.eugene.eugenehoranresume.model.Bullet;
import com.resume.horan.eugene.eugenehoranresume.model.DividerFiller;
import com.resume.horan.eugene.eugenehoranresume.model.Experience;
import com.resume.horan.eugene.eugenehoranresume.model.Header;
import com.resume.horan.eugene.eugenehoranresume.util.ui.LayoutUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
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

    void setItems(List<Object> objectList) {
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HOLDER_ERROR:
                return null;
            case HOLDER_HEADER:
                return new ViewHolderHeader(RecyclerHeaderBinding.inflate(layoutInflater, parent, false));
            case HOLDER_ACCOUNT:
                return new ViewHolderAccount(RecyclerAccountBinding.inflate(layoutInflater, parent, false));
            case HOLDER_EXPERIENCE:
                return new ViewHolderExperience(RecyclerExperienceBinding.inflate(layoutInflater, parent, false));
            case HOLDER_BULLET:
                return new ViewHolderBullet(RecyclerBulletBinding.inflate(layoutInflater, parent, false));
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
            mHolder.bindItems();
        } else if (holder instanceof ViewHolderAccount) {
            ViewHolderAccount mHolder = (ViewHolderAccount) holder;
            mHolder.bindItems();
        } else if (holder instanceof ViewHolderExperience) {
            ViewHolderExperience mHolder = (ViewHolderExperience) holder;
            mHolder.bindItems();
        } else if (holder instanceof ViewHolderBullet) {
            ViewHolderBullet mHolder = (ViewHolderBullet) holder;
            mHolder.bindItems();
        } else if (holder instanceof ViewHolderDivider) {
            ViewHolderDivider mHolder = (ViewHolderDivider) holder;
            mHolder.bindItems();
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

        void bindItems() {
            Header object = (Header) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
        }
    }

    public class ViewHolderAccount extends RecyclerView.ViewHolder {
        private RecyclerAccountBinding binding;

        ViewHolderAccount(RecyclerAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            if (LayoutUtil.isL()) {
                setAnimator();
            }
        }

        @TargetApi(21)
        private void setAnimator() {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(binding.getRoot().getContext(), R.drawable.anim_touch_elevate);
            binding.txtAccount.setStateListAnimator(sla);
        }

        private Account mAccount;

        void bindItems() {
            mAccount = (Account) mObjectList.get(getAdapterPosition());
            binding.setObject(mAccount);
            binding.setHolder(this);
            binding.executePendingBindings();
        }

        public void onAccountClicked(View view) {
            if (mListener != null) {
                mListener.onItemClicked(mAccount.getUrl());
            }
        }
    }


    public class ViewHolderExperience extends RecyclerView.ViewHolder {
        private RecyclerExperienceBinding binding;

        ViewHolderExperience(RecyclerExperienceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private Experience mExperience;

        void bindItems() {
            mExperience = (Experience) mObjectList.get(getAdapterPosition());
            binding.setHolder(this);
            binding.setObject(mExperience);
            binding.executePendingBindings();
        }

        public void onLinkClicked(View view) {
            if (mListener != null) {
                mListener.onItemClicked(mExperience.getLinkApp());
            }
        }
    }

    private class ViewHolderBullet extends RecyclerView.ViewHolder {
        private RecyclerBulletBinding binding;

        ViewHolderBullet(RecyclerBulletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItems() {
            Bullet object = (Bullet) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }

    private class ViewHolderDivider extends RecyclerView.ViewHolder {
        private RecyclerDividerBinding binding;

        ViewHolderDivider(RecyclerDividerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItems() {
            DividerFiller object = (DividerFiller) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }
}
