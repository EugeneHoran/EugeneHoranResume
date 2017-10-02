package com.resume.horan.eugene.eugenehoranresume.base.nullpresenters;

import com.resume.horan.eugene.eugenehoranresume.model.AboutObject;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.ui.main.MainActivityContract;

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
            public void showResumeFragment(ResumeExperienceObject experienceObject, ResumeSkillObject resumeSkillObject, ResumeEducationObject resumeEducationObject) {

            }

            @Override
            public void showTabs(boolean show) {

            }

            @Override
            public void showContactFragment(Contact contact) {

            }

            @Override
            public void setToolbarTitle(String title, boolean showExpandedImage) {

            }

            @Override
            public void setFragmentPosition(int which) {

            }

            @Override
            public void showAboutFragment(AboutObject aboutObject) {

            }

            @Override
            public void showLoadingError() {

            }
        };
    }
}
