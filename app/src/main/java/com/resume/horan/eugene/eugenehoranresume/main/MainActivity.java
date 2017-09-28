package com.resume.horan.eugene.eugenehoranresume.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
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
import com.resume.horan.eugene.eugenehoranresume.login.LoginActivity;
import com.resume.horan.eugene.eugenehoranresume.model.EugeneHoran;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.Prefs;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    private CheckBox mCheckboxFingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView generic = findViewById(R.id.generic);

        if (user != null) {
            if (!TextUtils.isEmpty(user.getDisplayName())) {
                generic.setText(user.getDisplayName());
            } else if (!TextUtils.isEmpty(user.getEmail())) {
                generic.setText(user.getEmail());
            } else {
                generic.setText(user.getProviderId());
            }
        } else {
            generic.setText("No Identifier");
        }

        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef = database.getReference("EugeneHoran");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EugeneHoran value = dataSnapshot.getValue(EugeneHoran.class);
                Toast.makeText(MainActivity.this, value.getLocation().getCountry(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        Button mBtnChangeData = findViewById(R.id.btnChangeData);
        mBtnChangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putBoolean(Common.PREF_FINGERPRINT, false);
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        mCheckboxFingerprint = findViewById(R.id.checkboxFingerprint);
        mCheckboxFingerprint.setChecked(Prefs.getBoolean(Common.PREF_FINGERPRINT, false));
        mCheckboxFingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Prefs.putBoolean(Common.PREF_FINGERPRINT, b);
            }
        });
    }
}
