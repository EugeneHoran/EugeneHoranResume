package com.resume.horan.eugene.eugenehoranresume.ui.main;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.adapter.ResumeEducationRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.adapter.ResumeExperienceRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.adapter.ResumeSkillRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.List;


public class ResumeChildFragment extends Fragment {
    public ResumeChildFragment() {
        // Required empty public constructor
    }

    public static ResumeChildFragment newInstance(ResumeExperienceObject experienceObject) {
        ResumeChildFragment fragment = new ResumeChildFragment();
        Bundle args = new Bundle();
        args.putInt(Common.ARG_WHICH_FRAGMENT, Common.WHICH_EXPERIENCE_FRAGMENT);
        args.putParcelable(Common.ARG_RESUME_EXPERIENCE, experienceObject);
        fragment.setArguments(args);
        return fragment;
    }

    public static ResumeChildFragment newInstance(ResumeSkillObject skillObject) {
        ResumeChildFragment fragment = new ResumeChildFragment();
        Bundle args = new Bundle();
        args.putInt(Common.ARG_WHICH_FRAGMENT, Common.WHICH_SKILL_FRAGMENT);
        args.putParcelable(Common.ARG_RESUME_SKILL, skillObject);
        fragment.setArguments(args);
        return fragment;
    }

    public static ResumeChildFragment newInstance(ResumeEducationObject skillObject) {
        ResumeChildFragment fragment = new ResumeChildFragment();
        Bundle args = new Bundle();
        args.putInt(Common.ARG_WHICH_FRAGMENT, Common.WHICH_EDUCATION_FRAGMENT);
        args.putParcelable(Common.ARG_RESUME_EDUCATION, skillObject);
        fragment.setArguments(args);
        return fragment;
    }


    private Activity mHost;
    private int mWhichFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getParentFragment().getActivity();
        if (getArguments() != null) {
            mWhichFragment = getArguments().getInt(Common.ARG_WHICH_FRAGMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mWhichFragment == Common.WHICH_SKILL_FRAGMENT) {
            return inflater.inflate(R.layout.fragment_skill, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_recyclerview, container, false);
        }
    }

    private RecyclerView mRecycler;

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mRecycler = v.findViewById(R.id.recycler);
        switch (mWhichFragment) {
            case Common.WHICH_EXPERIENCE_FRAGMENT:
                initExperienceAdapter();
                break;
            case Common.WHICH_SKILL_FRAGMENT:
                initSkillAdapter();
                break;
            case Common.WHICH_EDUCATION_FRAGMENT:
                initEducationAdapter();
                break;
            default:
                break;
        }
    }

    private void initExperienceAdapter() {
        ResumeExperienceObject resumeExperienceObject = getArguments().getParcelable(Common.ARG_RESUME_EXPERIENCE);
        List<Object> mExperienceList = resumeExperienceObject != null ? resumeExperienceObject.getExperienceList() : null;
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

    private void initSkillAdapter() {
        ResumeSkillObject mSkillObject = getArguments().getParcelable(Common.ARG_RESUME_SKILL);
        mRecycler.setLayoutManager(new FlowLayoutManager());
        ResumeSkillRecyclerAdapter mAdapter = new ResumeSkillRecyclerAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setItems(mSkillObject != null ? mSkillObject.getSkillObjectList() : null);
    }

    private void initEducationAdapter() {
        ResumeEducationObject mEducationObject = getArguments().getParcelable(Common.ARG_RESUME_EDUCATION);
        mRecycler.setLayoutManager(new LinearLayoutManager(mHost));
        ResumeEducationRecyclerAdapter mAdapter = new ResumeEducationRecyclerAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setItems(mEducationObject != null ? mEducationObject.getEducationList() : null);
    }
}
