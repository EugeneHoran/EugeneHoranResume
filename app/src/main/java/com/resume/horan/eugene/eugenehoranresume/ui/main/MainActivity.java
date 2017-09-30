package com.resume.horan.eugene.eugenehoranresume.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.ui.login.LoginActivity;
import com.resume.horan.eugene.eugenehoranresume.ui.main.fragment.ContactFragment;
import com.resume.horan.eugene.eugenehoranresume.ui.main.fragment.ResumeParentFragment;
import com.resume.horan.eugene.eugenehoranresume.ui.settings.SettingsActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.Prefs;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        MainContract.View,
        ResumeParentFragment.ResumeParentInterface,
        ContactFragment.ContactInterface {

    private static final String TAG_RESUME_PARENT_FRAGMENT = "tag_resume_fragment";
    private static final String TAG_CONTACT_FRAGMENT = "tag_contact_fragment";

    private MainContract.Presenter mPresenter;

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    private FirebaseAuth mAuth;
    // UI references.
    private DrawerLayout mDrawer;
    private FrameLayout mFrameContainer;
    private ProgressBar mProgressBar;
    private TextView mTextSettings;
    private TextView mTextLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDrawer = findViewById(R.id.drawer_layout);
        mTextSettings = findViewById(R.id.textSettings);
        mTextLogout = findViewById(R.id.textLogout);
        mFrameContainer = findViewById(R.id.container);
        mProgressBar = findViewById(R.id.viewProgress);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView mNavigationView = findViewById(R.id.navView);
        mNavigationView.setNavigationItemSelectedListener(this);
        TextView mTextEmail = mNavigationView.getHeaderView(0).findViewById(R.id.textEmail);
        mTextEmail.setText(!TextUtils.isEmpty(mFirebaseUser.getEmail()) ? mFirebaseUser.getEmail() : "null");

        mTextSettings.setOnClickListener(this);
        mTextLogout.setOnClickListener(this);

        setTitle("Eugene Horan Resume");
        new MainPresenter(this);
    }


    @Override
    public void whichFragment(int which) {
        switch (which) {
            case Common.WHICH_RESUME_FRAGMENT:
                setTitle("Eugene Horan Resume");
                break;
            case Common.WHICH_CONTACT_FRAGMENT:
                setTitle("Contact Information");
                showTabs(false);
                break;
        }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawer.closeDrawers();
        if (!item.isChecked()) {
            switch (item.getItemId()) {
                case R.id.action_resume:
                    mPresenter.onStart();
                    return true;
                case R.id.action_contact:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.anim_fade_in_slide_up, R.anim.anim_fade_out, R.anim.anim_fade_in_slide_up, R.anim.anim_fade_out)
                            .replace(R.id.container, ContactFragment.newInstance(), TAG_CONTACT_FRAGMENT)
                            .commit();
                    return true;
                case R.id.action_about:

                    break;
            }
        }
        return false;
    }

    @Override
    public void showResumeFragment(ResumeExperienceObject experienceObject, ResumeSkillObject resumeSkillObject, ResumeEducationObject resumeEducationObject) {
        ResumeParentFragment resumeParentFragment = (ResumeParentFragment) getSupportFragmentManager().findFragmentByTag(TAG_RESUME_PARENT_FRAGMENT);
        if (resumeParentFragment == null) {
            resumeParentFragment = ResumeParentFragment.newInstance(experienceObject, resumeSkillObject, resumeEducationObject);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.anim_fade_in_slide_up, R.anim.anim_fade_out, R.anim.anim_fade_in_slide_up, R.anim.anim_fade_out)
                    .replace(R.id.container, resumeParentFragment, TAG_RESUME_PARENT_FRAGMENT)
                    .commit();
        }
    }


    @Override
    public void showTabs(boolean show) {
        findViewById(R.id.tabs).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoading(boolean showLoading) {
        mFrameContainer.setVisibility(showLoading ? View.INVISIBLE : View.VISIBLE);
        mProgressBar.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }
}
