package com.resume.horan.eugene.eugenehoranresume.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.ui.main.ResumeChildFragment;

import java.util.ArrayList;
import java.util.List;


public class ResumeViewPagerAdapter extends FragmentPagerAdapter {
    private final List<String> mFragmentTitleList = new ArrayList<>();

    private ResumeExperienceObject mExperienceObject;
    private ResumeSkillObject mSkillObject;
    private ResumeEducationObject mEducationObject;

    public ResumeViewPagerAdapter(
            FragmentManager manager,
            ResumeExperienceObject experienceObject,
            ResumeSkillObject skillObject,
            ResumeEducationObject educationObject) {
        super(manager);
        mExperienceObject = experienceObject;
        mSkillObject = skillObject;
        mEducationObject = educationObject;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ResumeChildFragment.newInstance(mExperienceObject);
            case 1:
                return ResumeChildFragment.newInstance(mSkillObject);
            case 2:
                return ResumeChildFragment.newInstance(mEducationObject);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }

    public void addFragment(String title) {
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}