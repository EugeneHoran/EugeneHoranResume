package com.resume.horan.eugene.eugenehoranresume.main.resume;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentViewPagerBinding;
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

    private FragmentViewPagerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewPagerBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private ResumePagerAdapter mPagerAdapter;

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
        mPagerAdapter = new ResumePagerAdapter(
                getActivity(),
                getChildFragmentManager(),
                experienceObject,
                resumeSkillObject,
                resumeEducationObject);
        mPagerAdapter.setRetainedFragmentsTags(infoTabFragmentTags);
        binding.viewpager.setAdapter(mPagerAdapter);
        TabLayout mTabLayout = mHost.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(binding.viewpager);
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
            outState.putInt(CURRENT_INFO_TAB_FRAGMENT_POSITION, binding.viewpager.getCurrentItem());
        }
    }

    private void setCurrentPage() {
        if (binding.viewpager != null) {
            binding.viewpager.setCurrentItem(mCurrentPage);
        }
    }
}
