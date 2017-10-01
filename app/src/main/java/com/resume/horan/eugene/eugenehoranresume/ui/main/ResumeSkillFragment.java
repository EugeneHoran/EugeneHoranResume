package com.resume.horan.eugene.eugenehoranresume.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.adapter.ResumeSkillRecyclerAdapter;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;


public class ResumeSkillFragment extends Fragment {

    public ResumeSkillFragment() {
        // Required empty public constructor
    }

    public static ResumeSkillFragment newInstance(ResumeSkillObject skillObject) {
        ResumeSkillFragment fragment = new ResumeSkillFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_RESUME_SKILL, skillObject);
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mHost;
    private ResumeSkillObject mSkillObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getParentFragment().getActivity();
        if (getArguments() != null) {
            mSkillObject = getArguments().getParcelable(Common.ARG_RESUME_SKILL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_skill, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        RecyclerView mRecycler = v.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new FlowLayoutManager());
        ResumeSkillRecyclerAdapter mAdapter = new ResumeSkillRecyclerAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setItems(mSkillObject.getSkillObjectList());
    }
}
