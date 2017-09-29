package com.resume.horan.eugene.eugenehoranresume.ui.main;

import com.resume.horan.eugene.eugenehoranresume.base.BasePresenter;
import com.resume.horan.eugene.eugenehoranresume.base.BaseView;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void showResumeFragment(ResumeExperienceObject experienceObject);
    }

    interface Presenter extends BasePresenter {
        void loadResumeData();
    }

}
