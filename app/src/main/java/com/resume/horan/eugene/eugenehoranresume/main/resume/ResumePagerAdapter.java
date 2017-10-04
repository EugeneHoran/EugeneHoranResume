package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public class ResumePagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = ResumePagerAdapter.class.getName();

    private final static int NUM_PAGES = 3;

    private final static int EXPERIENCE_INDEX = 0;
    private final static int SKILLS_INDEX = 1;
    private final static int EDUCATION_INDEX = 2;

    private ResumeBaseInfoFragment[] mFragments;
    private Context mContext;
    private FragmentManager mFragmentManager;

    private ResumeBaseObject experienceObject;
    private ResumeBaseObject resumeSkillObject;
    private ResumeBaseObject resumeEducationObject;


    public ResumePagerAdapter(
            Context context,
            FragmentManager manager,
            ResumeBaseObject experienceObject,
            ResumeBaseObject resumeSkillObject,
            ResumeBaseObject resumeEducationObject) {
        super(manager);
        mContext = context;
        mFragmentManager = manager;
        this.experienceObject = experienceObject;
        this.resumeSkillObject = resumeSkillObject;
        this.resumeEducationObject = resumeEducationObject;
    }

    @Override
    public ResumeBaseInfoFragment getItem(int position) {
        // Reuse cached fragment if present
        if (mFragments != null && mFragments.length > position && mFragments[position] != null) {
            return mFragments[position];
        }
        if (mFragments == null) {
            mFragments = new ResumeBaseInfoFragment[getCount()];
        }
        switch (position) {
            case EXPERIENCE_INDEX:
                mFragments[position] = ResumeExperienceFragment.newInstance(experienceObject);
                break;
            case SKILLS_INDEX:
                mFragments[position] = ResumeSkillFragment.newInstance(resumeSkillObject);
                break;
            case EDUCATION_INDEX:
                mFragments[position] = ResumeEducationFragment.newInstance(resumeEducationObject);
                break;
        }
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getTitle(mContext.getResources());
    }

    public ResumeBaseInfoFragment[] getFragments() {
        if (mFragments == null) {
            // Force creating the fragments
            int count = getCount();
            for (int i = 0; i < count; i++) {
                getItem(i);
            }
        }
        return mFragments;
    }


    /**
     * When the device changes orientation, the {@link ResumeBaseInfoFragment}s are recreated
     * by the system, and they have the same tag ids as the ones previously used. Therefore, this
     * sets the cached fragments to the ones recreated by the system. This must be called before any
     * call to {@link #getItem(int)} or {@link #getFragments()} (note that when fragments are
     * recreated after orientation change, the {@link FragmentPagerAdapter} doesn't call {@link
     * #getItem(int)}.)
     *
     * @param tags the tags of the retained {@link ResumeBaseInfoFragment}s. Ignored if null
     *             or empty.
     */
    public void setRetainedFragmentsTags(String[] tags) {
        if (tags != null && tags.length > 0) {
            mFragments = new ResumeBaseInfoFragment[tags.length];
            for (int i = 0; i < tags.length; i++) {
                ResumeBaseInfoFragment fragment = (ResumeBaseInfoFragment) mFragmentManager.findFragmentByTag(tags[i]);
                mFragments[i] = fragment;
                if (fragment == null) {
                    Log.e(TAG, "Fragment with existing tag " + tags[i] + " not found!");
                    // No retained fragment (this happens if the fragment hadn't been shown before,
                    // because the tag on it would have been null in that case), so instantiate it
                    getItem(i);
                }
            }
        }
    }
}