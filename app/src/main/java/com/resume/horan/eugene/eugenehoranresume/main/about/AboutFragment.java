package com.resume.horan.eugene.eugenehoranresume.main.about;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentAboutBinding;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeBaseObject;
import com.resume.horan.eugene.eugenehoranresume.model.AboutObject;
import com.resume.horan.eugene.eugenehoranresume.model.AlbumImage;
import com.resume.horan.eugene.eugenehoranresume.ui.viewimage.ViewImageActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class AboutFragment extends Fragment {

    public static AboutFragment newInstance(ResumeBaseObject aboutObject) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_ABOUT, aboutObject);
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mHost;
    private ResumeBaseObject mAboutObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getActivity();

        if (getArguments() != null) {
            mAboutObject = getArguments().getParcelable(Common.ARG_ABOUT);
        }
    }

    private AboutRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAboutBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mAdapter = new AboutRecyclerAdapter();
        binding.recycler.setLayoutManager(mGridLayoutManager);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case AboutRecyclerAdapter.HOLDER_HEADER:
                        return 2;
                    case AboutRecyclerAdapter.HOLDER_DIVIDER:
                        return 2;
                    case AboutRecyclerAdapter.HOLDER_LOADER:
                        return 2;
                    case AboutRecyclerAdapter.HOLDER_GOAL:
                        return 2;
                    case AboutRecyclerAdapter.HOLDER_SOCIAL_MEDIA:
                        return 2;
                    case AboutRecyclerAdapter.HOLDER_IMAGES_LARGE:
                        return 2;
                    case AboutRecyclerAdapter.HOLDER_IMAGES:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        binding.setAdapter(mAdapter);
        binding.setObject(mAboutObject);
        mAdapter.setListener(new AboutRecyclerAdapter.Listener() {
            @Override
            public void onLinkClicked(String url) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(mHost, R.color.colorPrimary));
                builder.addDefaultShareMenuItem();
                builder.setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mHost, Uri.parse(url));
            }

            @Override
            public void onImageClicked(AlbumImage albumImage, View image, View card) {
                Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra(Common.ARG_IMAGE, albumImage);
                Pair<View, String> p2 = Pair.create(image, "image");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p2);
                startActivity(intent, options.toBundle());
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
