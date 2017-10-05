package com.resume.horan.eugene.eugenehoranresume.ui.settings;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.ui.login.LoginActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.Prefs;
import com.resume.horan.eugene.eugenehoranresume.util.fingerprintassistant.helper.FingerprintHelper;
import com.resume.horan.eugene.eugenehoranresume.util.fingerprintassistant.util.FingerprintResponseCode;


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
        private Preference fingerPrintSwitch;
        private Preference userEmail;
        private SharedPreferences sharedPref;
        private DatabaseReference mUserReference;
        private FingerprintHelper fingerPrintHelper;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            userEmail = findPreference("user_email");
            fingerPrintSwitch = findPreference(getString(R.string.pref_key_fingerprint_oauth));
            if (mFirebaseUser != null && !TextUtils.isEmpty(mFirebaseUser.getUid())) {
                mUserReference = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid());
                mUserReference.addListenerForSingleValueEvent(mUserEventListener);
            } else {
                userEmail.setSummary("null");
            }
            initSignOut();
            registerForFingerprintService(getActivity());
        }

        public void enableFingerprint(boolean enable) {
            if (enable) {
                initFingerprint();
            } else {
                fingerPrintSwitch.setSummary("Not enabled");
                fingerPrintSwitch.setShouldDisableView(true);
                fingerPrintSwitch.setDefaultValue(false);
            }
        }

        @Override
        public void onDestroy() {
            mUserReference.removeEventListener(mUserEventListener);
            super.onDestroy();
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

        private void initFingerprint() {
            fingerPrintSwitch.setEnabled(true);
            boolean fingerPrintEnabled = sharedPref.getBoolean(getString(R.string.pref_key_fingerprint_oauth), false);
            fingerPrintSwitch.setShouldDisableView(false);
            if (fingerPrintEnabled) {
                fingerPrintSwitch.setSummary("Fingerprint Enabled");
            } else {
                fingerPrintSwitch.setSummary("Enable Fingerprint for login");
            }
            fingerPrintSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if (o.equals(true)) {
                        fingerPrintSwitch.setSummary("Fingerprint Enabled");
                    } else {
                        fingerPrintSwitch.setSummary("Enable Fingerprint for login");
                    }
                    return true;
                }
            });
        }


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
                                    Prefs.putBoolean(Common.PREF_FINGERPRINT, false);
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

        private void registerForFingerprintService(Context context) {
            fingerPrintHelper = new FingerprintHelper(context, Common.KEY_FINGERPRINT);
            switch (fingerPrintHelper.checkAndEnableFingerPrintService()) {
                case FingerprintResponseCode.FINGERPRINT_SERVICE_INITIALISATION_SUCCESS:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        enableFingerprint(true);
                    } else {
                        enableFingerprint(false);
                    }
                    break;
                case FingerprintResponseCode.OS_NOT_SUPPORTED:
                case FingerprintResponseCode.FINGER_PRINT_SENSOR_UNAVAILABLE:
                case FingerprintResponseCode.ENABLE_FINGER_PRINT_SENSOR_ACCESS:
                case FingerprintResponseCode.NO_FINGER_PRINTS_ARE_ENROLLED:
                case FingerprintResponseCode.FINGERPRINT_SERVICE_INITIALISATION_FAILED:
                case FingerprintResponseCode.DEVICE_NOT_KEY_GUARD_SECURED:
                    enableFingerprint(false);
                    break;
            }
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
