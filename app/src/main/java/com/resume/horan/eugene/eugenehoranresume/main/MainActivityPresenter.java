package com.resume.horan.eugene.eugenehoranresume.main;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.base.nullpresenters.MainPresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.model.About;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.model.EugeneHoran;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;

class MainActivityPresenter extends MainPresenterNullCheck implements MainActivityContract.Presenter {
    private DatabaseReference myResumeReference;
    private DatabaseReference myContactReference;
    private DatabaseReference myAboutReference;

    private int mFragmentPosition = Common.WHICH_RESUME_FRAGMENT;

    MainActivityPresenter(MainActivityContract.View view) {
        myResumeReference = FirebaseUtil.getResumeRef();
        myContactReference = FirebaseUtil.getContactRef();
        myAboutReference = FirebaseUtil.getAboutRef();
        onAttachView(view);
        getView().setPresenter(this);
    }

    @Override
    public void start(int fragmentPosition) {
        mFragmentPosition = fragmentPosition;
        onStart();
    }

    @Override
    public void onDestroy() {
        myResumeReference.removeEventListener(mResumeEventListener);
        myContactReference.removeEventListener(mContactEventListener);
        myAboutReference.removeEventListener(mAboutEventListener);
        onDetachView();
    }

    @Override
    public void onStart() {
        loadMainData();
        getView().setFragmentPosition(mFragmentPosition);
        getView().expandAppbar();
        getView().showLoading(true);
        switch (mFragmentPosition) {
            case Common.WHICH_RESUME_FRAGMENT:
                getView().setToolbarTitle("Eugene Horan's Resume");
                break;
            case Common.WHICH_CONTACT_FRAGMENT:
                getView().showTabs(false);
                getView().setToolbarTitle("Eugene J. Horan");
                break;
            case Common.WHICH_ABOUT_FRAGMENT:
                getView().showTabs(false);
                getView().setToolbarTitle("About Eugene");
                break;
            case Common.WHICH_FEED_FRAGMENT:
                getView().showTabs(false);
                getView().setToolbarTitle("Social Feed");
                break;
        }
    }

    @Override
    public void loadMainData() {
        switch (mFragmentPosition) {
            case Common.WHICH_RESUME_FRAGMENT:
                myResumeReference.addListenerForSingleValueEvent(mResumeEventListener);
                break;
            case Common.WHICH_CONTACT_FRAGMENT:
                myContactReference.addListenerForSingleValueEvent(mContactEventListener);
                break;
            case Common.WHICH_ABOUT_FRAGMENT:
                myAboutReference.addListenerForSingleValueEvent(mAboutEventListener);
                break;
            case Common.WHICH_FEED_FRAGMENT:
                getView().showFeedFragment();
                break;
            default:
                break;
        }
    }

    private ValueEventListener mResumeEventListener = new ValueEventListener() {
        @Override
        @SuppressWarnings("all")
        public void onDataChange(DataSnapshot dataSnapshot) {
            EugeneHoran mEugeneValue = dataSnapshot.getValue(EugeneHoran.class);
            myResumeReference.keepSynced(true);
            getView().showResumeFragment(
                    mEugeneValue.getExperienceObject(),
                    mEugeneValue.getSkillsObject(),
                    mEugeneValue.getEducationObject());
            getView().showLoading(false);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            getView().showLoadingError();
        }
    };

    private ValueEventListener mContactEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Contact mContactValue = dataSnapshot.getValue(Contact.class);
            myContactReference.keepSynced(true);
            getView().showContactFragment(mContactValue);
            getView().showLoading(false);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            getView().showLoadingError();
        }
    };

    private ValueEventListener mAboutEventListener = new ValueEventListener() {
        @Override
        @SuppressWarnings("all")
        public void onDataChange(DataSnapshot dataSnapshot) {
            About mAboutValue = dataSnapshot.getValue(About.class);
            myAboutReference.keepSynced(true);
            getView().showAboutFragment(mAboutValue.getFilteredAboutList());
            getView().showLoading(false);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            getView().showLoadingError();
        }
    };
}
