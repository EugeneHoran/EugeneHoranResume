package com.resume.horan.eugene.eugenehoranresume.fingerprint;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.mattprecious.swirl.SwirlView;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.ActivityFingerprintBinding;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class FingerprintViewModel extends BaseObservable implements FingerPrintAuthCallback {
    private static final int STATUS_OK = 0;
    private static final int STATUS_WARNING = 1;

    private FingerPrintAuthHelper mFingerPrintAuthHelper;
    private Activity mActivity;
    private ActivityFingerprintBinding mBinding;
    private SharedPreferences mSharedPref;

    public FingerprintViewModel(Activity activity, ActivityFingerprintBinding binding) {
        mActivity = activity;
        mBinding = binding;
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
    }

    public void onResume() {
        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(mActivity, this);
        mFingerPrintAuthHelper.startAuth();
        setFingerprintMessage("Scan your fingerprint");
        setStatusColor(STATUS_OK);
    }

    public void onPause() {
        if (mFingerPrintAuthHelper.isScanning()) {
            Log.e("Testing", "stopAuth");
            mFingerPrintAuthHelper.stopAuth();
        }
    }

    /**
     * Fingerprint Text
     */
    private String mFingerprintMessage;
    private int textColor = STATUS_OK;

    @Bindable
    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        notifyPropertyChanged(BR.textColor);
    }


    @Bindable
    public String getFingerprintMessage() {
        return mFingerprintMessage;
    }

    public void setFingerprintMessage(String fingerprintMessage) {
        this.mFingerprintMessage = fingerprintMessage;
        notifyPropertyChanged(BR.fingerprintMessage);
    }

    public void setStatusColor(int status) {
        switch (status) {
            case STATUS_OK:
                mBinding.swirlFinger.setState(SwirlView.State.ON);
                setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                break;
            case STATUS_WARNING:
                mBinding.swirlFinger.setState(SwirlView.State.ERROR);
                setTextColor(ContextCompat.getColor(mActivity, R.color.redError));
                break;
            default:
                setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                break;
        }
    }

    /**
     * Fingerprint Error
     */
    private void fingerprintNotSupported(String reason) {
        mSharedPref.edit().putBoolean(mActivity.getString(R.string.pref_key_fingerprint_oauth), false).apply();
        Intent returnIntent = new Intent();
        mActivity.setResult(Activity.RESULT_CANCELED, returnIntent);
        returnIntent.putExtra(Common.ARG_FINGERPRINT_RETURN_ERROR, reason);
        mActivity.finish();
    }

    /**
     * Fingerprint Auth Callbacks
     */
    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        setFingerprintMessage("Success!!");
        setStatusColor(STATUS_OK);
        Intent returnIntent = new Intent();
        mActivity.setResult(Activity.RESULT_OK, returnIntent);
        mActivity.finish();
    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                setFingerprintMessage("Cannot recognize your finger print. Please try again.");
                setStatusColor(STATUS_WARNING);
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:
                setFingerprintMessage(errorMessage);
                setStatusColor(STATUS_WARNING);
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                fingerprintNotSupported("Cannot initialize finger print authentication.");
                setStatusColor(STATUS_WARNING);
                break;
        }
    }


    // Not Supported
    @Override
    public void onBelowMarshmallow() {
        fingerprintNotSupported("You are running older version of android that does not support finger print authentication.");
    }

    @Override
    public void onNoFingerPrintHardwareFound() {
        fingerprintNotSupported("Your device does not have finger print scanner.");
    }

    @Override
    public void onNoFingerPrintRegistered() {
        fingerprintNotSupported("There are no finger prints registered on this device. Please register your finger from settings.");
    }

}
