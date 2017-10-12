package com.resume.horan.eugene.eugenehoranresume.start;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.util.ui.Verify;

public class StartBSDataFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    public static StartBSDataFragment newInstance() {
        return new StartBSDataFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_bs_request_data, container, false);
    }

    private Button mBtnComplete;
    private EditText mEditUserEmail;

    private boolean mBtnClicked = false;

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        mBtnComplete = v.findViewById(R.id.btnComplete);
        mEditUserEmail = v.findViewById(R.id.editEmail);
        initViews();
    }

    private void initViews() {
        mBtnComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnComplete) {
            if (fieldsVerified(getActivity(), mEditUserEmail.getText().toString())) {
                mBtnClicked = true;
                mCallback.updateUserEmail(mEditUserEmail.getText().toString());
                getDialog().dismiss();
            } else {
                mBtnClicked = false;
            }
        }
    }

    private boolean fieldsVerified(Activity loginView, String email) {
        if (TextUtils.isEmpty(email)) {
            Log.e("Testing", "email isEmpty");
            showEmailError(loginView.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isEmailValid(email)) {
            Log.e("Testing", "isEmailValid");
            showEmailError(loginView.getString(R.string.error_invalid_email));
            return false;
        } else {
            return true;
        }
    }

    private void showEmailError(String error) {
        Toast.makeText(mEditUserEmail.getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mBtnClicked) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
        }
    }

    /**
     * Callbacks
     */

    private Listener mCallback;

    public interface Listener {
        void updateUserEmail(String email);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mCallback = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
