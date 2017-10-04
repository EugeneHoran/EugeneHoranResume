package com.resume.horan.eugene.eugenehoranresume.main.about;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.AboutObject;
import com.resume.horan.eugene.eugenehoranresume.model.AlbumImage;
import com.resume.horan.eugene.eugenehoranresume.ui.viewimage.ViewImageActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {

    public static AboutFragment newInstance(AboutObject aboutObject) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_ABOUT, aboutObject);
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mHost;
    private List<Object> mAboutList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getActivity();
        AboutObject mAboutObject = null;
        if (getArguments() != null) {
            mAboutObject = getArguments().getParcelable(Common.ARG_ABOUT);
        }
        mAboutList = mAboutObject != null ? mAboutObject.getmObjectList() : new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    private AboutRecyclerAdapter mAboutAdapter;

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        RecyclerView mRecyclerAbout = v.findViewById(R.id.recycler);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAboutAdapter.getItemViewType(position)) {
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
        mRecyclerAbout.setLayoutManager(mGridLayoutManager);
        mAboutAdapter = new AboutRecyclerAdapter();
        mRecyclerAbout.setAdapter(mAboutAdapter);
        mAboutAdapter.setItems(mAboutList);
        mAboutAdapter.setListener(new AboutRecyclerAdapter.Listener() {
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
                Pair<View, String> p3 = Pair.create(card, "back");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p2
//                        , p3
                );
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, "image");
                startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
