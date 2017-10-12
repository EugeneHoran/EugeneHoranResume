package com.resume.horan.eugene.eugenehoranresume.start;

import android.content.Intent;

import com.resume.horan.eugene.eugenehoranresume.base.BasePresenter;
import com.resume.horan.eugene.eugenehoranresume.base.BaseView;


public interface StartContract {

    interface View extends BaseView<StartContract.Presenter> {

        void loginSuccessful();

        void showLoginView();

        void showCreateAccountView();

        void showEmailRequired();

        void showFingerprintView();

        void showToast(String message);

        void showErrorName(String errorMessage);

        void showErrorEmail(String errorMessage);

        void showErrorPassword(String errorMessage);
    }

    interface Presenter extends BasePresenter {

        void launchSignInGoogleIntent();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        // Email
        void createAccountEmail(String displayName, String email, String password);

        void updateUserEmail(String email);

        void launchSignInEmail(String email, String password);

        void resetEmail(String email);
    }
}
