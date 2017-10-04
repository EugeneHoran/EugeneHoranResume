package com.resume.horan.eugene.eugenehoranresume.main.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.MarkerOptions;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.FragmentContactBinding;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class ContactFragment extends Fragment implements OnMapReadyCallback {
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
    private ContactViewModel mContactViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentContactBinding mContactBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        mContactViewModel = new ContactViewModel(getActivity(), mContact);
        mContactBinding.setContactViewModel(mContactViewModel);
        mContactBinding.mapView.onCreate(savedInstanceState);
        mContactBinding.mapView.getMapAsync(ContactFragment.this);
        return mContactBinding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(false);
        settings.setMyLocationButtonEnabled(false);
        settings.setAllGesturesEnabled(false);
        googleMap.setPadding(0, 300, 0, 0);
        googleMap.addMarker(new MarkerOptions().position(mContact.getLocation().getLatLng()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mContact.getLocation().getLatLng()));
        mContactViewModel.setMapReady(true);
    }
}
