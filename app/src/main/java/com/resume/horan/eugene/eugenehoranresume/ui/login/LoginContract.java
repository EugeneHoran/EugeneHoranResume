package com.resume.horan.eugene.eugenehoranresume.ui.login;


import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.resume.horan.eugene.eugenehoranresume.base.BasePresenter;
import com.resume.horan.eugene.eugenehoranresume.base.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void loginSuccessful();

        void loginSuccessfulFingerprint();

        void showToast(String message);

        void showEmailError(String error);

        void showPasswordError(String error);

        void showLogin();

        void showCreateAccount();

        void showFingerprint();

        void showFingerprintMessage(String message, boolean isError);

        void showEmailRequired();

        void fingerprintAuthError();
    }

    interface Presenter extends BasePresenter {

        void createGoogleClient(LoginActivity loginView);

        void createFacebookClient();

        void signInGoogle(LoginActivity loginView);

        void signInEmail(LoginActivity loginView, String email, String password);

        void createAccountEmail(LoginActivity loginView, String email, String password);

        void userEmailUpdated(String email);

        void initFingerprint(LoginActivity loginView);

        void onActivityResult(LoginActivity loginView, int requestCode, int resultCode, Intent data);
    }
}
