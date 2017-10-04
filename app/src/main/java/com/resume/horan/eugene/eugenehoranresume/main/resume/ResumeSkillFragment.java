package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentSkillBinding;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class ResumeSkillFragment extends ResumeBaseInfoFragment {
    public ResumeSkillFragment() {
        // Required empty public constructor
    }

    public static ResumeSkillFragment newInstance(ResumeBaseObject skillObject) {
        ResumeSkillFragment fragment = new ResumeSkillFragment();
        Bundle args = new Bundle();
        args.putInt(Common.ARG_WHICH_FRAGMENT, Common.WHICH_SKILL_FRAGMENT);
        args.putParcelable(Common.ARG_RESUME_SKILL, skillObject);
        fragment.setArguments(args);
        return fragment;
    }

    private ResumeBaseObject mSkillObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSkillObject = getArguments().getParcelable(Common.ARG_RESUME_SKILL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSkillBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_skill, container, false);
        binding.setAdapter(new ResumeSkillRecyclerAdapter());
        binding.setObject(mSkillObject);
        return binding.getRoot();
    }

    @Override
    public String getTitle(Resources resources) {
        return resources.getString(R.string.resume_skills);
    }
}
