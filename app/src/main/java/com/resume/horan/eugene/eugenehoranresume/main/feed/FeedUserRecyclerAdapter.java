package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.design.widget.BottomSheetDialog;
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

@SuppressLint("InflateParams")
public class FeedUserRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> mDataList = new ArrayList<>();

    FeedUserRecyclerAdapter() {
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onViewUserProfileClicked(String uid);
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
        Animation anim = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_set_fade_in_slide_up_recycler);
        anim.setStartOffset(200);
        holder.itemView.startAnimation(anim);
        holder.itemView.setTag(this);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        RecyclerUserListItemBinding binding;
        private User user;

        UserViewHolder(RecyclerUserListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            user = mDataList.get(getAdapterPosition());
            binding.setObject(user);
//            binding.setHolder(this);
            if (user.getDisplayName() != null) {
                binding.circleImage.setBorderColorResource(R.color.colorAccentBlue);
                binding.circleImage.setAlpha(1f);
            } else {
                binding.circleImage.setBorderColorResource(R.color.greyIconNormal);
                binding.circleImage.setAlpha(.3f);
            }
            if (user.getImageUrl().equalsIgnoreCase("null")) {
                binding.circleImage.setImageResource(R.drawable.ic_account_circle_grey);
            } else {
                Picasso.with(itemView.getContext()).load(user.getImageUrl()).error(R.drawable.ic_account_circle_grey).into(binding.circleImage);
            }
        }

        public void onUserClicked(View view) {
            if (user.getUid() == null) {
                return;
            }
            Activity activity = (Activity) view.getContext();
            final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
            final View v = activity.getLayoutInflater().inflate(R.layout.layout_bs_user, null);
            CircleImageView circleImage = v.findViewById(R.id.circleImage);
            TextView name = v.findViewById(R.id.name);
            TextView email = v.findViewById(R.id.email);
            TextView viewProfile = v.findViewById(R.id.viewProfile);
            viewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onViewUserProfileClicked(user.getUid());
                    }
                    mBottomSheetDialog.dismiss();
                }
            });
            Picasso.with(activity).load(user.getImageUrl()).error(R.drawable.ic_account_circle_grey).into(circleImage);
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            mBottomSheetDialog.setContentView(v);
            mBottomSheetDialog.show();
        }
    }


}