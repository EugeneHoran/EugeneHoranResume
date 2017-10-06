package com.resume.horan.eugene.eugenehoranresume;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.database.FirebaseDatabase;


public class EugeneHoranApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
