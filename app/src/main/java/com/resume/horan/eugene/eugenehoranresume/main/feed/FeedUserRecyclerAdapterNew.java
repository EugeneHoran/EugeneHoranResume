package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.app.Activity;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerFeedRecyclerDividerBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerUserListItemBinding;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeedUserRecyclerAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_USER = 1;
    private static final int HOLDER_DIVIDER = 2;

    private List<Object> objectList = new ArrayList<>();

    FeedUserRecyclerAdapterNew() {

    }

    public void setItems(List<Object> objectList) {
        this.objectList.clear();
        this.objectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof User) {
            return HOLDER_USER;
        } else if (objectList.get(position) instanceof String) {
            return HOLDER_DIVIDER;
        } else {
            return HOLDER_ERROR;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HOLDER_ERROR:
                return null;
            case HOLDER_USER:
                return new UserViewHolder(RecyclerUserListItemBinding.inflate(layoutInflater, parent, false));
            case HOLDER_DIVIDER:
                return new DividerViewHolder(RecyclerFeedRecyclerDividerBinding.inflate(layoutInflater, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserViewHolder) {
            UserViewHolder mHolder = (UserViewHolder) holder;
            mHolder.bindItem();
        } else if (holder instanceof DividerViewHolder) {
            DividerViewHolder mHolder = (DividerViewHolder) holder;
            mHolder.bindItem();
        }
        holder.itemView.setTag(this);
    }

    public List<Object> getObjectList() {
        return objectList;
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }


    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onViewUserProfileClicked(String uid);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        RecyclerUserListItemBinding binding;
        private User user;

        UserViewHolder(RecyclerUserListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

         void bindItem() {
            user = (User) objectList.get(getAdapterPosition());
            binding.setObject(user);
            binding.setHolder(this);
            if (user.getDisplayName() != null) {
                binding.circleImage.setBorderColorResource(R.color.colorAccentBlue);
                binding.circleImage.setAlpha(1f);
            } else {
                binding.circleImage.setBorderColorResource(R.color.greyIconNormal);
                binding.circleImage.setAlpha(.3f);
            }
            Picasso.with(itemView.getContext()).load(user.getImageUrl()).error(R.drawable.ic_account_circle_grey).into(binding.circleImage);
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

     class DividerViewHolder extends RecyclerView.ViewHolder {

        DividerViewHolder(RecyclerFeedRecyclerDividerBinding binding) {
            super(binding.getRoot());
        }

         void bindItem() {
        }
    }
}
