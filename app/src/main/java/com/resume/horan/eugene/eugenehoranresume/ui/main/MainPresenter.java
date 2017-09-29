package com.resume.horan.eugene.eugenehoranresume.ui.main;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.base.nullpresenters.MainPresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.model.Bullet;
import com.resume.horan.eugene.eugenehoranresume.model.DividerFiller;
import com.resume.horan.eugene.eugenehoranresume.model.EugeneHoran;
import com.resume.horan.eugene.eugenehoranresume.model.Experience;
import com.resume.horan.eugene.eugenehoranresume.model.Header;
import com.resume.horan.eugene.eugenehoranresume.model.Resume;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;

import java.util.ArrayList;
import java.util.List;

class MainPresenter extends MainPresenterNullCheck implements MainContract.Presenter {
    private static final String TAG = "MainPresenter";

    MainPresenter(MainContract.View view) {
        onAttachView(view);
        getView().setPresenter(this);
    }

    @Override
    public void onStart() {
        getView().showLoading(true);
        loadResumeData();
    }

    @Override
    public void onDestroy() {
        onDetachView();
    }

    @Override
    public void loadResumeData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("eugeneHoran");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EugeneHoran value = dataSnapshot.getValue(EugeneHoran.class);
                filterList(value.getResume());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void filterList(Resume resume) {
        List<Experience> experienceList = resume.getExperience();
        List<Object> mObjectList = new ArrayList<>();
        mObjectList.add(new Header("Public Accounts"));
        mObjectList.addAll(resume.getAccount());
        mObjectList.add(2, new DividerFiller("divider_no_space"));
        mObjectList.add(new Header("Resume"));
        Log.e("Testing", mObjectList.size() + "");
        for (int i = 0; i < experienceList.size(); i++) {
            if (mObjectList.size() > 5) {
                mObjectList.add(new DividerFiller("divider_space"));
            }
            Experience experience = experienceList.get(i);
            List<Bullet> bullets = new ArrayList<>();
            bullets.addAll(experience.getBullets());
            experience.setBullets(null);
            mObjectList.add(experience);
            mObjectList.addAll(bullets);
        }
        mObjectList.add(new DividerFiller("footer"));
        getView().showLoading(false);
        getView().showResumeFragment(new ResumeExperienceObject(mObjectList));
    }
}
