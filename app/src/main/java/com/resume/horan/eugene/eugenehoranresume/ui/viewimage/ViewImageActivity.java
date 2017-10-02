package com.resume.horan.eugene.eugenehoranresume.ui.viewimage;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.AlbumImage;
import com.resume.horan.eugene.eugenehoranresume.ui.main.MainActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.ui.PullBackLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity implements PullBackLayout.Callback {

    private RelativeLayout holder;
    private PullBackLayout mPuller;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        supportPostponeEnterTransition();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        AlbumImage albumImage = getIntent().getParcelableExtra(Common.ARG_IMAGE);
        holder = findViewById(R.id.holder);
        mPuller = findViewById(R.id.puller);
        mImageView = findViewById(R.id.imageView);

        mPuller.setBackgroundColor(Color.parseColor(albumImage.getImageColorHEX()));
        mPuller.setCallback(this);
        Picasso.with(this)
                .load(albumImage.getImageUrl())
                .rotate(albumImage.getRotate())
                .resize(albumImage.getFormattedWidth(), albumImage.getFormattedHeight())
                .onlyScaleDown()
                .into(mImageView,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                supportStartPostponedEnterTransition();
                            }

                            @Override
                            public void onError() {
                                supportStartPostponedEnterTransition();
                            }
                        });
        toolbar.bringToFront();
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

