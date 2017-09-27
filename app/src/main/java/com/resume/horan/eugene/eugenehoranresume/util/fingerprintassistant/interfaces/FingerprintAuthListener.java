package com.resume.horan.eugene.eugenehoranresume.util.fingerprintassistant.interfaces;

import android.hardware.fingerprint.FingerprintManager;


public interface FingerprintAuthListener {
    public void onAuthentication(int helpOrErrorCode, CharSequence infoString, FingerprintManager.AuthenticationResult authenticationResult, int authCode);
}
