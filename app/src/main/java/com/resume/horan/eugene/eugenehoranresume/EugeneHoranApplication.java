package com.resume.horan.eugene.eugenehoranresume;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.firebase.database.FirebaseDatabase;
import com.resume.horan.eugene.eugenehoranresume.util.Prefs;


public class EugeneHoranApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
