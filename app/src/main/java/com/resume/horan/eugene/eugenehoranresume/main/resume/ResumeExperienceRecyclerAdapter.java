package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerAccountBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerBulletBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerDividerBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerExperienceBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerExperiencePdfBinding;
import com.resume.horan.eugene.eugenehoranresume.databinding.RecyclerHeaderBinding;
import com.resume.horan.eugene.eugenehoranresume.main.MainActivity;
import com.resume.horan.eugene.eugenehoranresume.model.Account;
import com.resume.horan.eugene.eugenehoranresume.model.Bullet;
import com.resume.horan.eugene.eugenehoranresume.model.DividerFiller;
import com.resume.horan.eugene.eugenehoranresume.model.Experience;
import com.resume.horan.eugene.eugenehoranresume.model.Header;
import com.resume.horan.eugene.eugenehoranresume.model.ResumePdf;
import com.resume.horan.eugene.eugenehoranresume.util.FileResumeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

@SuppressWarnings("unused")
public class ResumeExperienceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_HEADER = 1;
    private static final int HOLDER_ACCOUNT = 2;
    private static final int HOLDER_EXPERIENCE = 3;
    private static final int HOLDER_BULLET = 4;
    private static final int HOLDER_DIVIDER = 5;
    private static final int HOLDER_PDF = 6;

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

    private void openFile(Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/" + "EugeneFolder/Resume.pdf");
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Open File");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectList.get(position) instanceof Header) {
            return HOLDER_HEADER;
        } else if (mObjectList.get(position) instanceof ResumePdf) {
            return HOLDER_PDF;
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
            case HOLDER_PDF:
                return new ViewHolderPDF(RecyclerExperiencePdfBinding.inflate(layoutInflater, parent, false));
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
        } else if (holder instanceof ViewHolderPDF) {
            ViewHolderPDF mHolder = (ViewHolderPDF) holder;
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
            setAnimator();
        }

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

    private static final String[] writePermission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int RC_WRITE_PERMISSIONS = 104;

    public class ViewHolderPDF extends RecyclerView.ViewHolder {
        private RecyclerExperiencePdfBinding binding;
        FileResumeUtil fileResumeUtil;
        private MainActivity mainActivity;

        ViewHolderPDF(RecyclerExperiencePdfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            mainActivity = (MainActivity) binding.getRoot().getContext();
            fileResumeUtil = new FileResumeUtil(binding.getRoot().getContext());
            fileResumeUtil.setListener(new FileResumeUtil.Listener() {
                @Override
                public void onFileSaved() {
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        void bindItems() {
            ResumePdf object = (ResumePdf) mObjectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.setHolder(this);
            if (fileResumeUtil.doesFileExist()) {
                binding.txtPdf.setText(R.string.view_resume);
                binding.txtPdf.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pdf, 0, R.drawable.ic_visibility, 0);
            } else {
                binding.txtPdf.setText(R.string.download_resume);
                binding.txtPdf.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pdf, 0, R.drawable.ic_file_download, 0);
            }
            setAnimator();
            binding.executePendingBindings();
        }

        private boolean doesFileExist(File file) {
            return file.exists() && new File(file, "Resume.pdf").exists();
        }

        public void saveFile(final View view) {
            if (!EasyPermissions.hasPermissions(mainActivity, writePermission)) {
                EasyPermissions.requestPermissions(mainActivity, "Permission is required to save file", RC_WRITE_PERMISSIONS, writePermission);
                fileResumeUtil.setListener(null);
                fileResumeUtil = null;
                return;
            }
            fileResumeUtil = new FileResumeUtil(binding.getRoot().getContext());
            fileResumeUtil.setListener(new FileResumeUtil.Listener() {
                @Override
                public void onFileSaved() {
                    notifyItemChanged(getAdapterPosition());
                }
            });
            fileResumeUtil.saveFile();
        }


        private void setAnimator() {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(binding.getRoot().getContext(), R.drawable.anim_touch_elevate);
            binding.txtPdf.setStateListAnimator(sla);
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
