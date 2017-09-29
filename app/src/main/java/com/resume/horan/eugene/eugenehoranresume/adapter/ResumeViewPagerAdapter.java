package com.resume.horan.eugene.eugenehoranresume.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.ui.main.resume.EducationFragment;
import com.resume.horan.eugene.eugenehoranresume.ui.main.resume.ExperienceFragment;

import java.util.ArrayList;
import java.util.List;


public class ResumeViewPagerAdapter extends FragmentPagerAdapter {
    private final List<String> mFragmentTitleList = new ArrayList<>();

    private ResumeExperienceObject mExperienceObject;

    public ResumeViewPagerAdapter(FragmentManager manager, ResumeExperienceObject experienceObject) {
        super(manager);
        mExperienceObject = experienceObject;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ExperienceFragment.newInstance(mExperienceObject);
            case 1:
                return EducationFragment.newInstance();
            case 2:
                return EducationFragment.newInstance();
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