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
import com.resume.horan.eugene.eugenehoranresume.model.Education;
import com.resume.horan.eugene.eugenehoranresume.model.EducationActivity;
import com.resume.horan.eugene.eugenehoranresume.model.EugeneHoran;
import com.resume.horan.eugene.eugenehoranresume.model.Experience;
import com.resume.horan.eugene.eugenehoranresume.model.Header;
import com.resume.horan.eugene.eugenehoranresume.model.Resume;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeEducationObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeExperienceObject;
import com.resume.horan.eugene.eugenehoranresume.model.ResumeSkillObject;
import com.resume.horan.eugene.eugenehoranresume.model.Skill;
import com.resume.horan.eugene.eugenehoranresume.model.SkillItem;

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
        getView().showLoading(false);
        getView().showResumeFragment(
                getFilteredExperiences(resume),
                getFilteredSkills(resume),
                getFilteredEducations(resume));
    }

    private ResumeEducationObject getFilteredEducations(Resume resume) {
        List<Education> educationList = resume.getEducation();
        List<Object> mObjectList = new ArrayList<>();
        for (int i = 0; i < educationList.size(); i++) {
            Education education = educationList.get(i);
            List<EducationActivity> eductionActivity = new ArrayList<>();
            if (education.getEducationActivity() != null) {
                eductionActivity.addAll(education.getEducationActivity());
                education.setEducationActivity(null);
            }
            mObjectList.add(education);
            mObjectList.addAll(eductionActivity);
        }
        return new ResumeEducationObject(mObjectList);
    }

    private ResumeSkillObject getFilteredSkills(Resume resume) {
        List<Skill> skillList = resume.getSkill();
        List<Object> mObjectList = new ArrayList<>();
        for (int i = 0; i < skillList.size(); i++) {
            Skill skill = skillList.get(i);
            List<SkillItem> skillItemList = new ArrayList<>();
            skillItemList.addAll(skill.getSkillItem());
            skill.setSkillItem(null);
            mObjectList.add(skill);
            mObjectList.addAll(skillItemList);
        }
        return new ResumeSkillObject(mObjectList);
    }

    private ResumeExperienceObject getFilteredExperiences(Resume resume) {
        List<Experience> experienceList = resume.getExperience();
        List<Object> mObjectList = new ArrayList<>();
        mObjectList.add(new Header("Public Accounts"));
        mObjectList.addAll(resume.getAccount());
        mObjectList.add(2, new DividerFiller("divider_no_space"));
        mObjectList.add(new Header("Resume"));
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
        return new ResumeExperienceObject(mObjectList);
    }


}
