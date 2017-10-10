package com.resume.horan.eugene.eugenehoranresume.main.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentContactBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class ContactFragment extends Fragment {
    public static ContactFragment newInstance(Contact contact) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putParcelable(Common.ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContact = getArguments().getParcelable(Common.ARG_CONTACT);
        }
    }

    private Contact mContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentContactBinding mContactBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        ContactViewModel mContactViewModel = new ContactViewModel(getActivity(), mContact);
        mContactBinding.setContactViewModel(mContactViewModel);
        return mContactBinding.getRoot();
    }
}
