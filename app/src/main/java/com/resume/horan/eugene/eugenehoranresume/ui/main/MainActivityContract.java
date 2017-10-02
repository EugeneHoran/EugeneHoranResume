package com.resume.horan.eugene.eugenehoranresume.ui.main;

import com.resume.horan.eugene.eugenehoranresume.base.BasePresenter;
import com.resume.horan.eugene.eugenehoranresume.base.BaseView;
import com.resume.horan.eugene.eugenehoranresume.model.AboutObject;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;

public interface MainActivityContract {

    interface View extends BaseView<Presenter> {
        void showTabs(boolean show);

        void setToolbarTitle(String title);

        void showAppBarExpanded(boolean showAppbarExpanded, String title);

        void setFragmentPosition(int fragmentPosition);

        void showResumeFragment(ResumeExperienceObject experienceObject, ResumeSkillObject resumeSkillObject, ResumeEducationObject resumeEducationObject);

        void showContactFragment(Contact contact);

        void showAboutFragment(AboutObject aboutObject);

        void showLoadingError();
    }

    interface Presenter extends BasePresenter {
        void start(int fragmentPosition);

        void loadMainData();
    }

}
