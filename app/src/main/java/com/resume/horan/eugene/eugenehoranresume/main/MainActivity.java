package com.resume.horan.eugene.eugenehoranresume.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.BaseActivity;
import com.resume.horan.eugene.eugenehoranresume.main.about.AboutFragment;
import com.resume.horan.eugene.eugenehoranresume.main.contact.ContactFragment;
import com.resume.horan.eugene.eugenehoranresume.main.feed.FeedFragment;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeBaseObject;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumePagerFragment;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.ui.settings.SettingsActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements
        View.OnClickListener,
        MainActivityContract.View,
        ResumePagerFragment.ResumeInteraction {


    private static final String STATE_FRAGMENT_POSITION = "saved_state_fragment_fragment_positions";
    private int mFragmentPosition = Common.WHICH_RESUME_FRAGMENT;

    private static final String TAG_RESUME_PARENT_FRAGMENT = "tag_resume_fragment";
    private static final String TAG_CONTACT_FRAGMENT = "tag_contact_fragment";
    private static final String TAG_ABOUT_FRAGMENT = "tag_about_fragment";
    private static final String TAG_FEED_FRAGMENT = "tag_feed_fragment";

    private MainActivityContract.Presenter mPresenter;

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private Context mContext;
    private FragmentManager mFragmentManager;
    // UI references.
    private AppBarLayout mAppBar;
    private CircleImageView mImageUserProfile;
    private BottomBar mBottomBar;
    private FrameLayout mFrameContainer;
    private TabLayout mTabLayout;

    // Bottom Nav library selects the first view onCreate
    private boolean mBottomNavClickable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        initMainActivity();
        mFragmentManager = getSupportFragmentManager();
        mAppBar = findViewById(R.id.app_bar);
        mFrameContainer = findViewById(R.id.container);
        mTabLayout = findViewById(R.id.tabs);
        mImageUserProfile = findViewById(R.id.imageUserProfile);
        initProfileImage();
        mImageUserProfile.setOnClickListener(this);
        mBottomBar = findViewById(R.id.bottomBar);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (mBottomNavClickable) {
                    if (tabId == R.id.action_resume) {
                        mPresenter.start(Common.WHICH_RESUME_FRAGMENT);
                    } else if (tabId == R.id.action_about_me) {
                        mPresenter.start(Common.WHICH_ABOUT_FRAGMENT);
                    } else if (tabId == R.id.action_contact) {
                        mPresenter.start(Common.WHICH_CONTACT_FRAGMENT);
                    } else if (tabId == R.id.action_feed) {
                        mPresenter.start(Common.WHICH_FEED_FRAGMENT);
                    }
                }
            }
        });
        new MainActivityPresenter(this);
        if (savedInstanceState != null) {
            mFragmentPosition = savedInstanceState.getInt(STATE_FRAGMENT_POSITION, Common.WHICH_RESUME_FRAGMENT);
        }
        mPresenter.start(mFragmentPosition);
        mBottomNavClickable = true;
        BottomBarTab nearby = mBottomBar.getTabWithId(R.id.action_feed);
        nearby.setBadgeCount(2);
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

    @Override
    public void showResumeFragment(ResumeBaseObject experienceObject, ResumeBaseObject resumeSkillObject, ResumeBaseObject resumeEducationObject) {
        ResumePagerFragment resumePagerFragment = (ResumePagerFragment) mFragmentManager.findFragmentByTag(TAG_RESUME_PARENT_FRAGMENT);
        if (resumePagerFragment == null) {
            resumePagerFragment = ResumePagerFragment.newInstance(experienceObject, resumeSkillObject, resumeEducationObject);
            replaceFragment(resumePagerFragment, TAG_RESUME_PARENT_FRAGMENT);
        }
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS); // list other flags here by |
        mToolbar.setLayoutParams(params);

    }

    @Override
    public void showAboutFragment(ResumeBaseObject aboutObject) {
        AboutFragment aboutFragment = (AboutFragment) mFragmentManager.findFragmentByTag(TAG_ABOUT_FRAGMENT);
        if (aboutFragment == null) {
            aboutFragment = AboutFragment.newInstance(aboutObject);
            replaceFragment(aboutFragment, TAG_ABOUT_FRAGMENT);
        }
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS); // list other flags here by |
        mToolbar.setLayoutParams(params);
    }

    @Override
    public void showContactFragment(Contact contact) {
        ContactFragment contactFragment = (ContactFragment) mFragmentManager.findFragmentByTag(TAG_CONTACT_FRAGMENT);
        if (contactFragment == null) {
            contactFragment = ContactFragment.newInstance(contact);
            replaceFragment(contactFragment, TAG_CONTACT_FRAGMENT);
        }
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS); // list other flags here by |
        mToolbar.setLayoutParams(params);
    }

    @Override
    public void showFeedFragment() {
        FeedFragment feedFragment = (FeedFragment) mFragmentManager.findFragmentByTag(TAG_FEED_FRAGMENT);
        if (feedFragment == null) {
            feedFragment = FeedFragment.newInstance();
            replaceFragment(feedFragment, TAG_FEED_FRAGMENT);
        }
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        mToolbar.setLayoutParams(params);
    }

    @Override
    public void setToolbarTitle(String title) {
        setTitle(title);
    }

    @Override
    public void expandAppbar() {
        mAppBar.setExpanded(true);
    }

    @Override
    public void showTabs(boolean show) {
        mTabLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoading(boolean showLoading) {
//        mFrameContainer.setVisibility(showLoading ? View.GONE : View.VISIBLE);
//        mProgressBar.setVisibility(showLoading ? View.VISIBLE : View.GONE);
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

    private void initProfileImage() {
        String profileUrl = FirebaseUtil.getUser().getPhotoUrl() != null ? FirebaseUtil.getUser().getPhotoUrl().toString() : "null";
        Picasso.with(mContext)
                .load(profileUrl)
                .error(R.drawable.ic_account_circle)
                .noFade()
                .into(mImageUserProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        mImageUserProfile.setVisibility(View.VISIBLE);
                        Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_set_fade_in_slide_up_recycler);
                        fadeIn.setDuration(350);
                        mImageUserProfile.startAnimation(fadeIn);
                    }

                    @Override
                    public void onError() {
                        mImageUserProfile.setBorderColorResource(R.color.transparent);
                        mImageUserProfile.setVisibility(View.VISIBLE);
                        Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_set_fade_in_slide_up_recycler);
                        fadeIn.setDuration(350);
                        mImageUserProfile.startAnimation(fadeIn);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == mImageUserProfile) {
            startActivity(new Intent(mContext, SettingsActivity.class));
            overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_nothing);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentPosition != Common.WHICH_EXPERIENCE_FRAGMENT) {
            mBottomBar.selectTabAtPosition(Common.WHICH_EXPERIENCE_FRAGMENT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
