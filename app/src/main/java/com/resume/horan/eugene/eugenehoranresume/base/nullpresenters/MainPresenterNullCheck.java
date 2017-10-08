package com.resume.horan.eugene.eugenehoranresume.base.nullpresenters;

import com.resume.horan.eugene.eugenehoranresume.main.MainActivityContract;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeBaseObject;
import com.resume.horan.eugene.eugenehoranresume.model.AboutObject;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;

public class MainPresenterNullCheck extends BasePresenterNullCheck<MainActivityContract.View> {

    @Override
    public MainActivityContract.View createNullView() {
        return new MainActivityContract.View() {

            @Override
            public void setPresenter(MainActivityContract.Presenter presenter) {

            }

            @Override
            public void showLoading(boolean showLoading) {

            }

            @Override
            public void showResumeFragment(ResumeBaseObject experienceObject, ResumeBaseObject resumeSkillObject, ResumeBaseObject resumeEducationObject) {

            }

            @Override
            public void showTabs(boolean show) {

            }

            @Override
            public void showContactFragment(Contact contact) {

            }

            @Override
            public void setToolbarTitle(String title) {

            }

            @Override
            public void expandAppbar() {

            }

            @Override
            public void setFragmentPosition(int which) {

            }

            @Override
            public void showAboutFragment(ResumeBaseObject aboutObject) {

            }

            @Override
            public void showLoadingError() {

            }

            @Override
            public void showFeedFragment() {

            }
        };
    }
}
