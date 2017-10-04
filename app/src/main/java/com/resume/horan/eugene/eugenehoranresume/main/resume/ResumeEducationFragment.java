package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentRecyclerviewBinding;
import com.resume.horan.eugene.eugenehoranresume.util.Common;


public class ResumeEducationFragment extends ResumeBaseInfoFragment {
    public ResumeEducationFragment() {
        // Required empty public constructor
    }

    public static ResumeEducationFragment newInstance(ResumeBaseObject skillObject) {
        ResumeEducationFragment fragment = new ResumeEducationFragment();
        Bundle args = new Bundle();
        args.putInt(Common.ARG_WHICH_FRAGMENT, Common.WHICH_EDUCATION_FRAGMENT);
        args.putParcelable(Common.ARG_RESUME_EDUCATION, skillObject);
        fragment.setArguments(args);
        return fragment;
    }

    private ResumeBaseObject mEducationObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEducationObject = getArguments().getParcelable(Common.ARG_RESUME_EDUCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentRecyclerviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false);
        binding.setAdapter(new ResumeEducationRecyclerAdapter());
        binding.setObject(mEducationObject);
        return binding.getRoot();
    }

    @Override
    public String getTitle(Resources resources) {
        return resources.getString(R.string.resume_education);
    }
}
