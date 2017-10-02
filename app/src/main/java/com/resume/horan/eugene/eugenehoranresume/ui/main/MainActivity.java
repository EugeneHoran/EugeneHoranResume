package com.resume.horan.eugene.eugenehoranresume.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.BaseActivity;
import com.resume.horan.eugene.eugenehoranresume.model.AboutObject;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.ui.login.LoginActivity;
import com.resume.horan.eugene.eugenehoranresume.ui.settings.SettingsActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.Prefs;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        MainActivityContract.View,
        ResumeParentFragment.ResumeInteraction,
        AppBarLayout.OnOffsetChangedListener {

    private static final String STATE_FRAGMENT_POSITION = "saved_state_fragment_fragment_position";
    private int mFragmentPosition = Common.WHICH_RESUME_FRAGMENT;

    private static final String TAG_RESUME_PARENT_FRAGMENT = "tag_resume_fragment";
    private static final String TAG_CONTACT_FRAGMENT = "tag_contact_fragment";
    private static final String TAG_ABOUT_FRAGMENT = "tag_about_fragment";

    private MainActivityContract.Presenter mPresenter;

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start(mFragmentPosition);
        mAppBar.setExpanded(false);
    }

    private FragmentManager mFragmentManager;
    private FirebaseAuth mAuth;
    // UI references.

    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppBar;
    private CircleImageView mImageProfile;
    private View mExpandedView;

    private FrameLayout mFrameContainer;
    private TabLayout mTabLayout;
    private ProgressBar mProgressBar;
    private TextView mTextSettings;
    private TextView mTextLogout;

    private Animation mAnimFadeInSlideUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainActivity();
        mAuth = FirebaseAuth.getInstance();
        mFragmentManager = getSupportFragmentManager();
        mAppBar = findViewById(R.id.app_bar);
        mImageProfile = findViewById(R.id.imageProfile);
        mExpandedView = findViewById(R.id.expandedView);
        mTextSettings = findViewById(R.id.textSettings);
        mTextLogout = findViewById(R.id.textLogout);
        mFrameContainer = findViewById(R.id.container);
        mTabLayout = findViewById(R.id.tabs);
        mProgressBar = findViewById(R.id.viewProgress);
        mCollapsingToolbar = findViewById(R.id.collapsingToolbar);
        mCollapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));

        mAnimFadeInSlideUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_set_fade_in_slide_up_recycler);
        mAnimFadeInSlideUp.setStartOffset(400);
        mAnimFadeInSlideUp.setFillAfter(false);

        mNavigationView.setNavigationItemSelectedListener(this);
        mTextSettings.setOnClickListener(this);
        mTextLogout.setOnClickListener(this);
        loadProfileImage();
        if (savedInstanceState != null) {
            mFragmentPosition = savedInstanceState.getInt(STATE_FRAGMENT_POSITION, Common.WHICH_RESUME_FRAGMENT);
        }
        new MainActivityPresenter(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(STATE_FRAGMENT_POSITION, mFragmentPosition);
    }

    @Override
    public void setFragmentPosition(int fragmentPosition) {
        mFragmentPosition = fragmentPosition;
    }

    /**
     * Start Working on Fragment Control
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawer.closeDrawers();
        if (!item.isChecked()) {
            switch (item.getItemId()) {
                case R.id.action_resume:
                    mPresenter.start(Common.WHICH_RESUME_FRAGMENT);
                    item.setChecked(true);
                    return true;
                case R.id.action_contact:
                    mPresenter.start(Common.WHICH_CONTACT_FRAGMENT);
                    item.setChecked(true);
                    return true;
                case R.id.action_about_me:
                    mPresenter.start(Common.WHICH_ABOUT_FRAGMENT);
                    item.setChecked(true);
                    break;
            }
        }
        return false;
    }

    @Override
    public void showResumeFragment(ResumeExperienceObject experienceObject, ResumeSkillObject resumeSkillObject, ResumeEducationObject resumeEducationObject) {
        ResumeParentFragment resumeParentFragment = (ResumeParentFragment) mFragmentManager.findFragmentByTag(TAG_RESUME_PARENT_FRAGMENT);
        if (resumeParentFragment == null) {
            resumeParentFragment = ResumeParentFragment.newInstance(experienceObject, resumeSkillObject, resumeEducationObject);
            replaceFragment(resumeParentFragment, TAG_RESUME_PARENT_FRAGMENT);
        }
    }

    @Override
    public void showContactFragment(Contact contact) {
        ContactFragment contactFragment = (ContactFragment) mFragmentManager.findFragmentByTag(TAG_CONTACT_FRAGMENT);
        if (contactFragment == null) {
            contactFragment = ContactFragment.newInstance(contact);
            replaceFragment(contactFragment, TAG_CONTACT_FRAGMENT);
        }
    }


    @Override
    public void showAboutFragment(AboutObject aboutObject) {
        AboutFragment aboutFragment = (AboutFragment) mFragmentManager.findFragmentByTag(TAG_ABOUT_FRAGMENT);
        if (aboutFragment == null) {
            aboutFragment = AboutFragment.newInstance(aboutObject);
            replaceFragment(aboutFragment, TAG_ABOUT_FRAGMENT);
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        setTitle(title);
    }

    @Override
    public void showAppBarExpanded(boolean showAppbarExpanded, String title) {
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setTitleEnabled(showAppbarExpanded);
        mExpandedView.setVisibility(showAppbarExpanded ? View.VISIBLE : View.GONE);
        mCollapsingToolbar.setTitleEnabled(showAppbarExpanded);
        mAppBar.setExpanded(showAppbarExpanded, false);
        findViewById(R.id.viewAnchor).setVisibility(showAppbarExpanded ? View.VISIBLE : View.GONE);
        mImageProfile.setVisibility(showAppbarExpanded ? View.VISIBLE : View.GONE);
        if (showAppbarExpanded) {
            mImageProfile.startAnimation(mAnimFadeInSlideUp);
            mAppBar.addOnOffsetChangedListener(this);
        } else {
            mAnimFadeInSlideUp.cancel();
            mAppBar.removeOnOffsetChangedListener(this);
        }
        mMaxScrollSize = mAppBar.getTotalScrollRange();
    }

    private void loadProfileImage() {
        Picasso.with(MainActivity.this)
                .load(Common.IMG_PROFILE)
                .into(mImageProfile);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAppBar.setExpanded(false);
    }

    @Override
    public void showTabs(boolean show) {
        mTabLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoading(boolean showLoading) {
        mFrameContainer.setVisibility(showLoading ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoadingError() {
        Snackbar.make(mFrameContainer, "Error loading data", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onStart();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mTextSettings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (v == mTextLogout) {
            Prefs.putBoolean(Common.PREF_FINGERPRINT, false);
            mAuth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentPosition != Common.WHICH_EXPERIENCE_FRAGMENT) {
            mPresenter.start(Common.WHICH_EXPERIENCE_FRAGMENT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }


    /**
     * Collapsing Toolbar Listener
     */
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private int mMaxScrollSize;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();
        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;
        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            mImageProfile.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(100)
                    .start();
        }
        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;
            mImageProfile.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }
}
