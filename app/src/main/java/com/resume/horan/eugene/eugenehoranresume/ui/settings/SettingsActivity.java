package com.resume.horan.eugene.eugenehoranresume.ui.settings;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.fingerprint.FingerprintActivity;
import com.resume.horan.eugene.eugenehoranresume.login.LoginActivity;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.resume.horan.eugene.eugenehoranresume.util.LayoutUtil;


public class SettingsActivity extends AppCompatPreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        GeneralPreferenceFragment generalPreferenceFragment = new GeneralPreferenceFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, generalPreferenceFragment).commit();
    }


    public static class GeneralPreferenceFragment extends PreferenceFragment {
        private SwitchPreference fingerPrintSwitch;
        private Preference userEmail;
        private DatabaseReference mUserReference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            userEmail = findPreference("user_email");
            fingerPrintSwitch = (SwitchPreference) findPreference(getString(R.string.pref_key_fingerprint_oauth));
            if (FirebaseUtil.getUser() != null) {
                mUserReference = FirebaseUtil.getCurrentUserRef();
                mUserReference.addListenerForSingleValueEvent(mUserEventListener);
            } else {
                userEmail.setSummary("null");
            }
            if (LayoutUtil.isM()) {
                initFingerprint();
            } else {
                fingerPrintSwitch.setEnabled(false);
                fingerPrintSwitch.setChecked(false);
                fingerPrintSwitch.setDefaultValue(false);
            }
            initFingerprintSummary();
            initSignOut();
        }

        @Override
        public void onDestroy() {
            mUserReference.removeEventListener(mUserEventListener);
            super.onDestroy();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            /* Fingerprint Handler */
            if (requestCode == Common.FINGERPRINT_RESULT) {
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getActivity(), "Fingerprint enabled", Toast.LENGTH_SHORT).show();
                    fingerPrintSwitch.setChecked(true);
                    initFingerprintSummary();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    if (data != null) {
                        String reason = data.getExtras().getString(Common.ARG_FINGERPRINT_RETURN_ERROR);
                        Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();
                    }
                    fingerPrintSwitch.setChecked(false);
                    initFingerprintSummary();
                }
            }
        }

        private ValueEventListener mUserEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);
                if (mUser != null) {
                    userEmail.setSummary(mUser.email);
                } else {
                    userEmail.setSummary("null");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                userEmail.setSummary("null");
            }
        };

        private void initSignOut() {
            Preference button = findPreference(getString(R.string.pref_key_logout));
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Sign Out");
                    alertDialog.setMessage("Are you sure you want to sign out?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sign Out",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    fingerPrintSwitch.setChecked(false);
                                    FirebaseAuth.getInstance().signOut();
                                    LoginManager.getInstance().logOut();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return true;
                }
            });
        }

        private void initFingerprintSummary() {
            if (fingerPrintSwitch.isChecked()) {
                fingerPrintSwitch.setSummary("Fingerprint Enabled");
            } else {
                fingerPrintSwitch.setSummary("Fingerprint Disabled");
            }
        }

        private void initFingerprint() {
            fingerPrintSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if (o.equals(true)) {
                        Intent intent = new Intent(getActivity(), FingerprintActivity.class);
                        intent.putExtra(Common.ARG_FINGERPRINT_TYPE, Common.WHICH_FINGERPRINT_SETTINGS);
                        startActivityForResult(intent, Common.FINGERPRINT_RESULT);
                        fingerPrintSwitch.setChecked(false);
                    } else {
                        fingerPrintSwitch.setSummary("Fingerprint Disabled");
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.anim_nothing, R.anim.anim_slide_down);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_nothing, R.anim.anim_slide_down);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }
}
