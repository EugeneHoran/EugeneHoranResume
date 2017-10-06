package com.resume.horan.eugene.eugenehoranresume.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.resume.horan.eugene.eugenehoranresume.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected FirebaseUser mFirebaseUser;

    private void initFirebaseUser() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void initMainActivity() {
        initFirebaseUser();
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void replaceFragment(Fragment fragment, String TAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_set_fade_in_slide_up_recycler, 0);
        transaction.replace(R.id.container, fragment, TAG).commit();
    }
}
