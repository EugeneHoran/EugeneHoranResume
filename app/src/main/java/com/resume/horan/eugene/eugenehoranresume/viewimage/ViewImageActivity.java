package com.resume.horan.eugene.eugenehoranresume.viewimage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.ActivityViewImageBinding;
import com.resume.horan.eugene.eugenehoranresume.model.AlbumImage;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.ui.PullBackLayout;

public class ViewImageActivity extends AppCompatActivity implements PullBackLayout.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityViewImageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_image);
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
        if (getIntent().getParcelableExtra(Common.ARG_IMAGE) != null) {
            AlbumImage albumImage = getIntent().getParcelableExtra(Common.ARG_IMAGE);
            binding.setHolder(new ViewImageViewHolder(albumImage));
        } else if (getIntent().getStringExtra(Common.ARG_IMAGE_STRING) != null) {
            String imageUrl = getIntent().getStringExtra(Common.ARG_IMAGE_STRING);
            binding.setHolder(new ViewImageViewHolder(imageUrl));
        }
        supportPostponeEnterTransition();
        binding.puller.setCallback(this);
        binding.toolbar.bringToFront();
    }

    @Override
    public void onPullStart() {

    }

    @Override
    public void onPull(float progress) {

    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }
}

