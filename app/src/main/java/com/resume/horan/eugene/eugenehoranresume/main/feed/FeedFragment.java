package com.resume.horan.eugene.eugenehoranresume.main.feed;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentFeedBinding;
import com.resume.horan.eugene.eugenehoranresume.post.NewPostActivity;

public class FeedFragment extends Fragment {
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FeedViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFeedBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        FeedRecyclerAdapter adapter = new FeedRecyclerAdapter();
        binding.setAdapter(adapter);
        adapter.setListener(new FeedRecyclerAdapter.Listener() {
            @Override
            public void onAddImageClick() {
                Intent i = new Intent(getActivity(), NewPostActivity.class);
                startActivity(i);
            }
        });
        model = new FeedViewModel();
        binding.setModel(model);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        model.initItems();
        model.filterItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}