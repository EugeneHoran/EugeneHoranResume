package com.resume.horan.eugene.eugenehoranresume.ui.main.resume;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;

public class EducationFragment extends Fragment {

    public EducationFragment() {
        // Required empty public constructor
    }

    public static EducationFragment newInstance() {
        return new EducationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }
}
