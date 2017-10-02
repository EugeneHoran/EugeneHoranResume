package com.resume.horan.eugene.eugenehoranresume.ui.main;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.base.nullpresenters.MainPresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.model.About;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.model.EugeneHoran;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

class MainActivityPresenter extends MainPresenterNullCheck implements MainActivityContract.Presenter {

    private static final String TAG = "MainActivityPresenter";

    private DatabaseReference myResumeReference;
    private DatabaseReference myContactReference;
    private DatabaseReference myAboutReference;

    private int mFragmentPosition = Common.WHICH_RESUME_FRAGMENT;

    MainActivityPresenter(MainActivityContract.View view) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        myResumeReference = mDatabase.getReference(Common.FB_REF_EUGENE_HORAN);
        myContactReference = mDatabase.getReference(Common.FB_REF_CONTACT);
        myAboutReference = mDatabase.getReference(Common.FB_REF_ABOUT);
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
        onDetachView();
    }

    @Override
    public void onStart() {
        getView().setFragmentPosition(mFragmentPosition);
        switch (mFragmentPosition) {
            case Common.WHICH_RESUME_FRAGMENT:
                getView().showLoading(true);
                getView().setToolbarTitle("Eugene Horan's Resume");
                getView().showAppBarExpanded(false, null);
                break;
            case Common.WHICH_CONTACT_FRAGMENT:
                getView().showLoading(true);
                getView().showTabs(false);
                getView().setToolbarTitle("Eugene J. Horan");
                getView().showAppBarExpanded(false, null);
                break;
            case Common.WHICH_ABOUT_FRAGMENT:
                getView().showLoading(true);
                getView().showTabs(false);
                getView().setToolbarTitle("About Eugene");
                getView().showAppBarExpanded(true, null);
                break;
        }
        loadMainData();
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
                    mEugeneValue.getFilteredExperiences(),
                    mEugeneValue.getFilteredSkills(),
                    mEugeneValue.getFilteredEducations());
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
            getView().showAboutFragment(mAboutValue.getFilteredAbout());
            getView().showLoading(false);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            getView().showLoadingError();
        }
    };
}
