package com.resume.horan.eugene.eugenehoranresume.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
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
