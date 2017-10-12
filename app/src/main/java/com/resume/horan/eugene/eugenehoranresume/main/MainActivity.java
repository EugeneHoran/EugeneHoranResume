package com.resume.horan.eugene.eugenehoranresume.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.ActivityMainBinding;
import com.resume.horan.eugene.eugenehoranresume.main.about.AboutFragment;
import com.resume.horan.eugene.eugenehoranresume.main.contact.ContactFragment;
import com.resume.horan.eugene.eugenehoranresume.main.feed.FeedFragment;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumeBaseObject;
import com.resume.horan.eugene.eugenehoranresume.main.resume.ResumePagerFragment;
import com.resume.horan.eugene.eugenehoranresume.model.Contact;
import com.resume.horan.eugene.eugenehoranresume.settings.SettingsActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements MainActivityContract.View, View.OnClickListener {


    private static final String STATE_FRAGMENT_POSITION = "saved_state_fragment_fragment_positions";
    private static final String TAG_RESUME_PARENT_FRAGMENT = "tag_resume_fragment";
    private static final String TAG_CONTACT_FRAGMENT = "tag_contact_fragment";
    private static final String TAG_ABOUT_FRAGMENT = "tag_about_fragment";
    private static final String TAG_FEED_FRAGMENT = "tag_feed_fragment";

    @Override
    public void setPresenter(@NonNull MainActivityContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private ActivityMainBinding binding;
    private MainActivityContract.Presenter presenter;
    private FragmentManager fm;

    private boolean mEnableBN = false;
    private int mIntFragPos;

    @Override
    protected void onCreate(Bundle saveState) {
        super.onCreate(saveState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        new MainActivityPresenter(this);
        fm = getSupportFragmentManager();
        binding.imageUserProfile.setOnClickListener(this);
        binding.bottomBar.setOnTabSelectListener(onTabSelectListener);
        mIntFragPos = saveState == null ? Common.WHICH_RESUME_FRAGMENT : saveState.getInt(STATE_FRAGMENT_POSITION, Common.WHICH_RESUME_FRAGMENT);
        presenter.start(mIntFragPos);
        initProfileImage();
        mEnableBN = true;
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
        saveState.putInt(STATE_FRAGMENT_POSITION, mIntFragPos);
    }

    /**
     * Navigation Handling
     * <p>
     * Bottom nav {@link OnTabSelectListener}
     * Return int nav position and set (int mIntFragPos)
     * Frag {@link ResumePagerFragment}
     * Frag {@link AboutFragment}
     * Frag {@link ContactFragment}
     * Frag {@link FeedFragment}
     * replaceFragment(Fragment fragment, String TAG)
     */
    private OnTabSelectListener onTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {
            if (mEnableBN) {
                if (tabId == R.id.action_resume) {
                    presenter.start(Common.WHICH_RESUME_FRAGMENT);
                } else if (tabId == R.id.action_about_me) {
                    presenter.start(Common.WHICH_ABOUT_FRAGMENT);
                } else if (tabId == R.id.action_contact) {
                    presenter.start(Common.WHICH_CONTACT_FRAGMENT);
                } else if (tabId == R.id.action_feed) {
                    presenter.start(Common.WHICH_FEED_FRAGMENT);
                }
            }
        }
    };

    @Override
    public void setFragmentPosition(int fragmentPosition) {
        mIntFragPos = fragmentPosition;
    }

    @Override
    public void showResumeFragment(@NonNull ResumeBaseObject experienceObject, @NonNull ResumeBaseObject resumeSkillObject, @NonNull ResumeBaseObject resumeEducationObject) {
        ResumePagerFragment fragment = (ResumePagerFragment) fm.findFragmentByTag(TAG_RESUME_PARENT_FRAGMENT);
        if (fragment == null) {
            fragment = ResumePagerFragment.newInstance(experienceObject, resumeSkillObject, resumeEducationObject);
            replaceFragment(fragment, TAG_RESUME_PARENT_FRAGMENT);
        }
    }

    @Override
    public void showAboutFragment(@NonNull ResumeBaseObject aboutObject) {
        AboutFragment fragment = (AboutFragment) fm.findFragmentByTag(TAG_ABOUT_FRAGMENT);
        if (fragment == null) {
            fragment = AboutFragment.newInstance(aboutObject);
            replaceFragment(fragment, TAG_ABOUT_FRAGMENT);
        }
    }

    @Override
    public void showContactFragment(@NonNull Contact contact) {
        ContactFragment fragment = (ContactFragment) fm.findFragmentByTag(TAG_CONTACT_FRAGMENT);
        if (fragment == null) {
            fragment = ContactFragment.newInstance(contact);
            replaceFragment(fragment, TAG_CONTACT_FRAGMENT);
        }
    }

    @Override
    public void showFeedFragment() {
        FeedFragment fragment = (FeedFragment) fm.findFragmentByTag(TAG_FEED_FRAGMENT);
        if (fragment == null) {
            fragment = FeedFragment.newInstance();
            replaceFragment(fragment, TAG_FEED_FRAGMENT);
        }
    }

    public void replaceFragment(Fragment fragment, String TAG) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_set_fade_in_slide_up_recycler, 0);
        transaction.replace(R.id.container, fragment, TAG).commit();
    }

    /**
     * Toolbar Functions
     * <p>
     * Toolbar Title
     * Expand AppBar
     * Enable Tabs
     * Load Profile Image inside of Toolbar
     * Scroll Flags
     */
    @Override
    public void setToolbarTitle(@Nullable String title) {
        setTitle(title);
    }

    @Override
    public void expandAppbar() {
        binding.appBar.setExpanded(true);
    }

    @Override
    public void showTabs(boolean show) {
        binding.tabs.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void initProfileImage() {
        String profileUrl = FirebaseUtil.getUser().getPhotoUrl() != null ? String.valueOf(FirebaseUtil.getUser().getPhotoUrl()) : "null";
        Picasso.with(MainActivity.this)
                .load(profileUrl)
                .error(R.drawable.ic_account_circle_white)
                .noFade()
                .into(binding.imageUserProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.imageUserProfile.setVisibility(View.VISIBLE);
                        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_set_fade_in_slide_up_recycler);
                        fadeIn.setDuration(350);
                        binding.imageUserProfile.startAnimation(fadeIn);
                    }

                    @Override
                    public void onError() {
                        binding.imageUserProfile.setBorderColorResource(R.color.transparent);
                        binding.imageUserProfile.setVisibility(View.VISIBLE);
                        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_set_fade_in_slide_up_recycler);
                        fadeIn.setDuration(350);
                        binding.imageUserProfile.startAnimation(fadeIn);
                    }
                });
    }

    @Override
    public void setScrollFlags(int fragmentPosition) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.toolbar.getLayoutParams();
        switch (fragmentPosition) {
            case Common.WHICH_RESUME_FRAGMENT:
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                binding.toolbar.setLayoutParams(params);
                break;
            case Common.WHICH_ABOUT_FRAGMENT:
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                break;
            case Common.WHICH_CONTACT_FRAGMENT:
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                break;
            case Common.WHICH_FEED_FRAGMENT:
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                break;
        }
        binding.toolbar.setLayoutParams(params);
    }

    /**
     * Loading Functions
     */
    @Override
    public void showLoading(boolean showLoading) {
//        mFrameContainer.setVisibility(showLoading ? View.GONE : View.VISIBLE);
//        mProgressBar.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoadingError() {
        Snackbar.make(binding.container, "Error loading data", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onStart();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.imageUserProfile) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_nothing);
        }
    }

    @Override
    public void onBackPressed() {
        if (mIntFragPos != Common.WHICH_EXPERIENCE_FRAGMENT) {
            binding.bottomBar.selectTabAtPosition(Common.WHICH_EXPERIENCE_FRAGMENT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
