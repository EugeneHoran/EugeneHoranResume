package com.resume.horan.eugene.eugenehoranresume.ui.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.model.Location;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class ContactFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
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
        mLocation = mContact != null ? mContact.getLocation() : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    private Contact mContact;
    private Location mLocation;

    private View v;
    private Bundle savedInstanceState;
    private MapView mMapView;
    private View mViewAddress;
    private ImageView mImgCopyPrimary;
    private ImageView mImgMessagePrimary;
    private ImageView mImgPhonePrimary;
    private ImageView mImgCopySecondary;
    private ImageView mImgPhoneSecondary;
    private ImageView mImgCopyEmail;
    private ImageView mImgEmail;

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        this.v = v;
        this.savedInstanceState = savedInstanceState;
        mViewAddress = v.findViewById(R.id.viewAddress);
        TextView textAddress = v.findViewById(R.id.textAddress);
        TextView textPhonePrimary = v.findViewById(R.id.textPhonePrimary);
        TextView textPhoneSecondary = v.findViewById(R.id.textPhoneSecondary);
        TextView textEmail = v.findViewById(R.id.textEmail);
        mImgCopyPrimary = v.findViewById(R.id.imgCopyPrimary);
        mImgMessagePrimary = v.findViewById(R.id.imgMessagePrimary);
        mImgPhonePrimary = v.findViewById(R.id.imgPhonePrimary);
        mImgCopySecondary = v.findViewById(R.id.imgCopySecondary);
        mImgPhoneSecondary = v.findViewById(R.id.imgPhoneSecondary);
        mImgCopyEmail = v.findViewById(R.id.imgCopyEmail);
        mImgEmail = v.findViewById(R.id.imgEmail);
        mImgCopyPrimary.setOnClickListener(this);
        mImgMessagePrimary.setOnClickListener(this);
        mImgPhonePrimary.setOnClickListener(this);
        mImgCopySecondary.setOnClickListener(this);
        mImgPhoneSecondary.setOnClickListener(this);
        mImgCopyEmail.setOnClickListener(this);
        mImgEmail.setOnClickListener(this);

        textAddress.setText(mLocation.getStreet() + ", " + mLocation.getCity() + ", " + mLocation.getState() + " " + mLocation.getZip());
        textPhonePrimary.setText(String.format("%s (Cell)", mContact.getPhonePrimary()));
        textPhoneSecondary.setText(String.format("%s (Home)", mContact.getPhoneSecondary()));
        textEmail.setText(mContact.getEmail());
        delayMap();
    }

    private void delayMap() {
        if (savedInstanceState == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initMap();
                }
            }, 500);
        } else {
            initMap();
        }
    }

    public void initMap() {
        mMapView = v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(ContactFragment.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.setPadding(0, 300, 0, 0);
        mMapView.setClickable(false);
        LatLng home = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(home).title("Home"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        mMapView.setVisibility(View.VISIBLE);
        mViewAddress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view == mImgCopyPrimary) {
            copyToClipboard("Eugene's cell phone copied tp clipboard", mContact.getPhonePrimary());
        } else if (view == mImgMessagePrimary) {
            makeTextMessage(mContact.getPhonePrimary());
        } else if (view == mImgPhonePrimary) {
            makePhoneCall(mContact.getPhonePrimary());
        } else if (view == mImgCopySecondary) {
            copyToClipboard("Eugene's home phone copied tp clipboard", mContact.getPhonePrimary());
        } else if (view == mImgPhoneSecondary) {
            makePhoneCall(mContact.getPhoneSecondary());
        } else if (view == mImgCopyEmail) {
            copyToClipboard("Eugene's email copied tp clipboard", mContact.getEmail());
        } else if (view == mImgEmail) {
            makeEmail(mContact.getEmail());
        }
    }

    private void makeTextMessage(String phone) {
        Uri sms_uri = Uri.parse("smsto:" + phone);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        startActivity(sms_intent);
    }

    private void makeEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void makePhoneCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Snackbar.make(mImgCopyPrimary, label, Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), label, Toast.LENGTH_SHORT).show();
    }
}
