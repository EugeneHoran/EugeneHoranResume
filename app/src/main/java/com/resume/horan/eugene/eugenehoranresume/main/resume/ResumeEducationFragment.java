package com.resume.horan.eugene.eugenehoranresume.main.resume;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
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

    private Activity mHost;
    private ResumeBaseObject mEducationObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getParentFragment().getActivity();
        if (getArguments() != null) {
            mEducationObject = getArguments().getParcelable(Common.ARG_RESUME_EDUCATION);
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
        mRecycler.setLayoutManager(new LinearLayoutManager(mHost));
        ResumeEducationRecyclerAdapter mAdapter = new ResumeEducationRecyclerAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setItems(mEducationObject.getObjectList());
    }

    @Override
    public String getTitle(Resources resources) {
        return resources.getString(R.string.resume_education);
    }
}
