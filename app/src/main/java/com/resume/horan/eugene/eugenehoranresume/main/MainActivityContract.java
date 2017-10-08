package com.resume.horan.eugene.eugenehoranresume.main;

import com.resume.horan.eugene.eugenehoranresume.base.BasePresenter;
import com.resume.horan.eugene.eugenehoranresume.base.BaseView;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeBaseObject;
import com.resume.horan.eugene.eugenehoranresume.model.AboutObject;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;

public interface MainActivityContract {

    interface View extends BaseView<Presenter> {
        void showTabs(boolean show);

        void setToolbarTitle(String title);

        void expandAppbar();

        void setFragmentPosition(int fragmentPosition);

        void showResumeFragment(ResumeBaseObject experienceObject, ResumeBaseObject resumeSkillObject, ResumeBaseObject resumeEducationObject);

        void showContactFragment(Contact contact);

        void showAboutFragment(ResumeBaseObject aboutObject);

        void showFeedFragment();

        void showLoadingError();
    }

    interface Presenter extends BasePresenter {
        void start(int fragmentPosition);

        void loadMainData();
    }

}
