package com.resume.horan.eugene.eugenehoranresume.main.resume;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.main.MainActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class ResumePagerFragment extends Fragment {
    private static final String INFO_TAB_FRAGMENTS_TAGS = "resume_tab_fragments_tags";

    private static final String CURRENT_INFO_TAB_FRAGMENT_POSITION = "current_single_day_fragments_position";

    private int mCurrentPage;


    public static ResumePagerFragment newInstance(
            ResumeBaseObject experienceObject,
            ResumeBaseObject resumeSkillObject,
            ResumeBaseObject resumeEducationObject) {
        ResumePagerFragment fragment = new ResumePagerFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_RESUME_EXPERIENCE, experienceObject);
        args.putParcelable(Common.ARG_RESUME_SKILL, resumeSkillObject);
        args.putParcelable(Common.ARG_RESUME_EDUCATION, resumeEducationObject);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivity mHost;
    private ResumeBaseObject experienceObject;
    private ResumeBaseObject resumeSkillObject;
    private ResumeBaseObject resumeEducationObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = (MainActivity) getActivity();
        if (getArguments() != null) {
            experienceObject = getArguments().getParcelable(Common.ARG_RESUME_EXPERIENCE);
            resumeSkillObject = getArguments().getParcelable(Common.ARG_RESUME_SKILL);
            resumeEducationObject = getArguments().getParcelable(Common.ARG_RESUME_EDUCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    private ResumePagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        String[] infoTabFragmentTags = null;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INFO_TAB_FRAGMENTS_TAGS)) {
                infoTabFragmentTags = savedInstanceState.getStringArray(INFO_TAB_FRAGMENTS_TAGS);
            }
            if (savedInstanceState.containsKey(CURRENT_INFO_TAB_FRAGMENT_POSITION)) {
                mCurrentPage = savedInstanceState.getInt(CURRENT_INFO_TAB_FRAGMENT_POSITION);
            }
        }
        mViewPager = v.findViewById(R.id.viewpager);
        mPagerAdapter = new ResumePagerAdapter(
                getActivity(),
                getChildFragmentManager(),
                experienceObject,
                resumeSkillObject,
                resumeEducationObject);
        mPagerAdapter.setRetainedFragmentsTags(infoTabFragmentTags);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout mTabLayout = mHost.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setCurrentPage();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPagerAdapter != null && mPagerAdapter.getFragments() != null) {
            ResumeBaseInfoFragment[] infoFragments = mPagerAdapter.getFragments();
            String[] tags = new String[infoFragments.length];
            for (int i = 0; i < tags.length; i++) {
                tags[i] = infoFragments[i].getTag();
            }
            outState.putStringArray(INFO_TAB_FRAGMENTS_TAGS, tags);
            outState.putInt(CURRENT_INFO_TAB_FRAGMENT_POSITION, mViewPager.getCurrentItem());
        }
    }

    private void setCurrentPage() {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(mCurrentPage);
        }
    }
}
