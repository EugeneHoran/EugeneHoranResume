package com.resume.horan.eugene.eugenehoranresume.ui.main.resume;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.adapter.ResumeExperienceRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

import java.util.List;

public class ExperienceFragment extends Fragment {

    public ExperienceFragment() {
        // Required empty public constructor
    }

    public static ExperienceFragment newInstance(ResumeExperienceObject experienceObject) {
        ExperienceFragment fragment = new ExperienceFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_RESUME_EXPERIENCE, experienceObject);
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mHost;
    private List<Object> mExperienceList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getParentFragment().getActivity();
        ResumeExperienceObject resumeExperienceObject = null;
        if (getArguments() != null) {
            resumeExperienceObject = getArguments().getParcelable(Common.ARG_RESUME_EXPERIENCE);
        }
        if (resumeExperienceObject != null) {
            mExperienceList = resumeExperienceObject.getExperienceList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        RecyclerView mRecycler = v.findViewById(R.id.recycler);
        ResumeExperienceRecyclerAdapter mAdapter = new ResumeExperienceRecyclerAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setItems(mExperienceList);
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
    }
}
