package com.resume.horan.eugene.eugenehoranresume.base.nullpresenters;

import com.resume.horan.eugene.eugenehoranresume.ui.login.LoginContract;

public class LoginPresenterNullCheck extends BasePresenterNullCheck<LoginContract.View> {
    @Override
    public LoginContract.View createNullView() {
        return new LoginContract.View() {
            @Override
            public void setPresenter(LoginContract.Presenter presenter) {

            }

            @Override
            public void loginSuccessful() {

            }

            @Override
            public void showLoading(boolean showLoading) {

            }

            @Override
            public void showToast(String message) {

            }

            @Override
            public void showEmailError(String error) {

            }

            @Override
            public void showPasswordError(String error) {

            }

            @Override
            public void showLogin() {

            }

            @Override
            public void showCreateAccount() {

            }

            @Override
            public void showFingerprint() {

            }

            @Override
            public void showFingerprintMessage(String message, boolean isError) {

            }

            @Override
            public void fingerprintAuthError() {

            }

            @Override
            public void loginSuccessfulFingerprint() {

            }
        };
    }
}
