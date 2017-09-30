package com.resume.horan.eugene.eugenehoranresume.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.adapter.ResumeEducationRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class ResumeEducationFragment extends Fragment {

    public ResumeEducationFragment() {
        // Required empty public constructor
    }


    public static ResumeEducationFragment newInstance(ResumeEducationObject skillObject) {
        ResumeEducationFragment fragment = new ResumeEducationFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_RESUME_EDUCATION, skillObject);
        fragment.setArguments(args);
        return fragment;
    }

    private ResumeEducationObject mEducationObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mRecycler.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        ResumeEducationRecyclerAdapter mAdapter = new ResumeEducationRecyclerAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setItems(mEducationObject.getEducationList());
    }
}
