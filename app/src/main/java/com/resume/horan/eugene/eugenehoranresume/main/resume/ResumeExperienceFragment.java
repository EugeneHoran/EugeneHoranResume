package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentRecyclerviewBinding;
import com.resume.horan.eugene.eugenehoranresume.util.Common;


public class ResumeExperienceFragment extends ResumeBaseInfoFragment {
    public ResumeExperienceFragment() {
        // Required empty public constructor
    }

    public static ResumeExperienceFragment newInstance(ResumeBaseObject experienceObject) {
        ResumeExperienceFragment fragment = new ResumeExperienceFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_RESUME_EXPERIENCE, experienceObject);
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mHost;
    private ResumeBaseObject mObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getParentFragment().getActivity();
        if (getArguments() != null) {
            mObject = getArguments().getParcelable(Common.ARG_RESUME_EXPERIENCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentRecyclerviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false);
        ResumeExperienceRecyclerAdapter mAdapter = new ResumeExperienceRecyclerAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(mHost));
        binding.setAdapter(mAdapter);
        binding.setObject(mObject);
        mAdapter.setListener(new ResumeExperienceRecyclerAdapter.Listener() {
            @Override
            public void onItemClicked(String url) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(mHost, R.color.colorPrimary));
                builder.addDefaultShareMenuItem();
                builder.setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mHost, Uri.parse(url));
            }
        });
        return binding.getRoot();
    }

    @Override
    public String getTitle(Resources resources) {
        return resources.getString(R.string.resume_experience);
    }
}