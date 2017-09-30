package com.resume.horan.eugene.eugenehoranresume.ui.main.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.adapter.ResumeViewPagerAdapter;
import com.resume.horan.eugene.eugenehoranresume.base.BaseInterface;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.ui.main.MainActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class ResumeParentFragment extends Fragment {
    public static ResumeParentFragment newInstance(
            ResumeExperienceObject experienceObject,
            ResumeSkillObject resumeSkillObject,
            ResumeEducationObject resumeEducationObject) {
        ResumeParentFragment fragment = new ResumeParentFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_RESUME_EXPERIENCE, experienceObject);
        args.putParcelable(Common.ARG_RESUME_SKILL, resumeSkillObject);
        args.putParcelable(Common.ARG_RESUME_EDUCATION, resumeEducationObject);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivity mHost;
    private ResumeExperienceObject mExperienceObject;
    private ResumeSkillObject mSkillObject;
    private ResumeEducationObject mEducationObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = (MainActivity) getActivity();
        if (getArguments() != null) {
            mExperienceObject = getArguments().getParcelable(Common.ARG_RESUME_EXPERIENCE);
            mSkillObject = getArguments().getParcelable(Common.ARG_RESUME_SKILL);
            mEducationObject = getArguments().getParcelable(Common.ARG_RESUME_EDUCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mCallback.whichFragment(Common.WHICH_RESUME_FRAGMENT);
        ViewPager viewPager = v.findViewById(R.id.viewpager);
        TabLayout tabLayout = mHost.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        mCallback.showTabs(false);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ResumeViewPagerAdapter adapter = new ResumeViewPagerAdapter(
                getChildFragmentManager(),
                mExperienceObject,
                mSkillObject,
                mEducationObject);
        adapter.addFragment("Experience");
        adapter.addFragment("Skills");
        adapter.addFragment("Education");
        viewPager.setAdapter(adapter);
        mCallback.showTabs(true);
    }

    private ResumeParentInterface mCallback;

    public interface ResumeParentInterface extends BaseInterface {
        void showTabs(boolean show);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResumeParentInterface) {
            mCallback = (ResumeParentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
