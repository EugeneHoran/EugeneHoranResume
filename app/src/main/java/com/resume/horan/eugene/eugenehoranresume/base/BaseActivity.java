package com.resume.horan.eugene.eugenehoranresume.base;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.resume.horan.eugene.eugenehoranresume.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected FirebaseUser mFirebaseUser;

    private void initFirebaseUser() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    protected DrawerLayout mDrawer;
    protected NavigationView mNavigationView;

    public void initMainActivity() {
        initFirebaseUser();
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView = findViewById(R.id.navView);
        TextView mTextEmail = mNavigationView.getHeaderView(0).findViewById(R.id.textEmail);
        mTextEmail.setText(!TextUtils.isEmpty(mFirebaseUser.getEmail()) ? mFirebaseUser.getEmail() : "null");
    }

    public void replaceFragment(Fragment fragment, String TAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.anim_set_fade_in_slide_up_recycler, R.anim.anim_fade_out);
        transaction.replace(R.id.container, fragment, TAG).commit();
    }

    public void addFragment(Fragment fragment, String TAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_set_fade_in_slide_up_recycler, R.anim.anim_fade_out, R.anim.anim_set_fade_in_slide_up_recycler, R.anim.anim_fade_out);
        transaction.add(R.id.container, fragment, TAG).commit();
    }
}
