package com.resume.horan.eugene.eugenehoranresume.base.nullpresenters;

import com.resume.horan.eugene.eugenehoranresume.base.BasePresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.ui.main.MainContract;

public class MainPresenterNullCheck extends BasePresenterNullCheck<MainContract.View> {

    @Override
    public MainContract.View createNullView() {
        return new MainContract.View() {

            @Override
            public void setPresenter(MainContract.Presenter presenter) {

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
        };
    }
}
