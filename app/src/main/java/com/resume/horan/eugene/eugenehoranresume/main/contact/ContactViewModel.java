package com.resume.horan.eugene.eugenehoranresume.main.contact;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.model.Location;
import com.squareup.picasso.Picasso;


public class ContactViewModel extends BaseObservable {

    private Context mContext;
    private Contact mContact;
    private Location mLocation;
    private boolean mOnMapReady = false;

    public ContactViewModel(Context context, Contact contact) {
        this.mContext = context;
        this.mContact = contact;
        this.mLocation = this.mContact.getLocation();
    }

    public Location getLocation() {
        return mLocation;
    }

    public String getAddress() {
        return mLocation.getStreet() + ", " + mLocation.getCity() + ", " + mLocation.getState() + " " + mLocation.getZip();
    }

    public String getPhonePrimary() {
        return mContact.getPhonePrimary();
    }

    public String getPhonePrimaryFormatted() {
        return String.format("%s (Cell)", mContact.getPhonePrimary());
    }

    public String getPhoneSecondary() {
        return mContact.getPhoneSecondary();
    }

    public String getPhoneSecondaryFormatted() {
        return String.format("%s (Home)", mContact.getPhoneSecondary());
    }

    public String getEmail() {
        return mContact.getEmail();
    }

    void setMapReady(boolean mapReady) {
        this.mOnMapReady = mapReady;
        notifyChange();
    }

    public boolean getMapReady() {
        return mOnMapReady;
    }

    public void onClipboardClicked(String text) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(text, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(mContext, text + " Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    public void onMessageClicked(String phone) {
        Uri sms_uri = Uri.parse("smsto:" + phone);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        mContext.startActivity(sms_intent);
    }


    public void onCallClicked(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        mContext.startActivity(intent);
    }

    public void onEmailClicked(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        mContext.startActivity(Intent.createChooser(intent, "Send Email"));
    }


    @BindingAdapter("load_image")
    public static void loadImage(final ImageView view, Location object) {
        Picasso.with(view.getContext())
                .load(object.getMapUrlString())
                .into(view);
    }

}
