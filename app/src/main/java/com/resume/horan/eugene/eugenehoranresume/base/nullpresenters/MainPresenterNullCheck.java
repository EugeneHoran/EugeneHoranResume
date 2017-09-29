package com.resume.horan.eugene.eugenehoranresume.base.nullpresenters;

import com.resume.horan.eugene.eugenehoranresume.base.BasePresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.ui.main.MainContract;

public class MainPresenterNullCheck extends BasePresenterNullCheck<MainContract.View> {

    @Override
    public MainContract.View createNullView() {
        return new MainContract.View() {
            @Override
            public void showResumeFragment(ResumeExperienceObject experienceObject) {

            }

            @Override
            public void setPresenter(MainContract.Presenter presenter) {

            }

            @Override
            public void showLoading(boolean showLoading) {

            }
        };
    }
}
