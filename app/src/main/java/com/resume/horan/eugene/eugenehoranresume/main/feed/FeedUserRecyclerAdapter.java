package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerUserListItemBinding;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeedUserRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> mDataList = new ArrayList<>();

    public FeedUserRecyclerAdapter() {
    }

    public void setItems(List<User> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public void setData(List<User> data) {
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(RecyclerUserListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        UserViewHolder holder = (UserViewHolder) rawHolder;
        holder.bind();
        holder.itemView.setTag(position);
        Animation anim = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_set_fade_in_slide_up_recycler);
        holder.itemView.startAnimation(anim);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        RecyclerUserListItemBinding binding;
        private User user;

        public UserViewHolder(RecyclerUserListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            user = mDataList.get(getAdapterPosition());
            binding.setObject(user);
            binding.setHolder(this);
            binding.circleImage.setBorderColorResource(R.color.colorAccentBlue);
            if (user.getDisplayName() != null) {
                binding.circleImage.setAlpha(1f);
            } else {
                binding.circleImage.setAlpha(.3f);
            }
            Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_account_circle);
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.colorAccentBlue), PorterDuff.Mode.MULTIPLY));
            drawable.mutate();
            Picasso.with(itemView.getContext()).load(user.getImageUrl()).error(drawable).into(binding.circleImage);
        }

        public void onUserClicked(View view) {
            if (user.getEmail() == null) {
                return;
            }
            Activity activity = (Activity) view.getContext();
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.ic_account_circle);
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(activity, R.color.colorAccentBlue), PorterDuff.Mode.MULTIPLY));
            drawable.mutate();
            BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
            View v = activity.getLayoutInflater().inflate(R.layout.layout_bs_user, null);
            CircleImageView circleImage = v.findViewById(R.id.circleImage);
            TextView name = v.findViewById(R.id.name);
            TextView email = v.findViewById(R.id.email);
            Picasso.with(activity).load(user.getImageUrl()).error(drawable).into(circleImage);
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            mBottomSheetDialog.setContentView(v);
            mBottomSheetDialog.show();
        }
    }
}