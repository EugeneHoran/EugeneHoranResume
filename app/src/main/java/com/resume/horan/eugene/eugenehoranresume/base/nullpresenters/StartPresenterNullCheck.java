package com.resume.horan.eugene.eugenehoranresume.base.nullpresenters;

import com.resume.horan.eugene.eugenehoranresume.start.StartContract;

public class StartPresenterNullCheck extends BasePresenterNullCheck<StartContract.View> {

    @Override
    public StartContract.View createNullView() {
        return new StartContract.View() {

            @Override
            public void loginSuccessful() {

            }

            @Override
            public void showCreateAccountView() {

            }

            @Override
            public void setPresenter(StartContract.Presenter presenter) {
            }

            @Override
            public void showFingerprintView() {

            }

            @Override
            public void showLoginView() {

            }

            @Override
            public void showEmailRequired() {

            }

            @Override
            public void showToast(String message) {

            }

            @Override
            public void showErrorName(String errorMessage) {

            }

            @Override
            public void showErrorEmail(String errorMessage) {

            }

            @Override
            public void showErrorPassword(String errorMessage) {

            }
        };
    }
}
