package com.resume.horan.eugene.eugenehoranresume.start;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.ActivityStartBinding;
import com.resume.horan.eugene.eugenehoranresume.fingerprint.FingerprintActivity;
import com.resume.horan.eugene.eugenehoranresume.login.LoginRequestDataBSFragment;
import com.resume.horan.eugene.eugenehoranresume.main.MainActivity;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.LayoutUtil;
import com.resume.horan.eugene.eugenehoranresume.util.MultiTextWatcher;

import java.util.Arrays;

public class StartActivity extends AppCompatActivity implements
        StartContract.View,
        View.OnClickListener,
        LoginRequestDataBSFragment.Listener,
        ViewTreeObserver.OnGlobalLayoutListener {
    private StartContract.Presenter mPresenter;

    @Override
    public void setPresenter(StartContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    private boolean mCreateAccountVisible = false;
    private boolean mResetPasswordVisible = false;
    private ActivityStartBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        binding.btnFacebookSignIn.setReadPermissions(Arrays.asList("email", "public_profile"));
        new MultiTextWatcher()
                .registerEditText(binding.editEmail)
                .registerEditText(binding.editPassword)
                .setCallback(textWatcherInterface);
        binding.viewRoot.getViewTreeObserver().addOnGlobalLayoutListener(this);
        binding.editPassword.setOnEditorActionListener(onEditorActionListener);
        binding.toolbar.setNavigationOnClickListener(this);
        binding.btnEmailSignIn.setOnClickListener(this);
        binding.btnEmailCreateAccount.setOnClickListener(this);
        binding.btnGoogleSignIn.setOnClickListener(this);
        binding.btnEmailForgot.setOnClickListener(this);
        // Init Presenter
        new StartPresenter(this, this);
        mPresenter.onStart();
    }

    @Override
    public void onBackPressed() {
        if (mCreateAccountVisible || mResetPasswordVisible) {
            showLoginView();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        binding.viewRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        /* Sign In Email */
        if (view == binding.btnEmailSignIn) {
            attemptLoginFromEmail();
        }
        /* Sign In Google */
        else if (view == binding.btnGoogleSignIn) {
            mPresenter.launchSignInGoogleIntent();
        }
        /* Create Account */
        else if (view == binding.btnEmailCreateAccount) {
            if (mCreateAccountVisible) {
                attemptCreateAccountFromEmail();
            } else {
                showCreateAccountView();
            }
        }
        /* Forgot Password */
        else if (view == binding.btnEmailForgot) {
            if (mResetPasswordVisible) {
                mPresenter.resetEmail(binding.editEmail.getText().toString().trim());
            } else {
                showForgotPasswordView();
            }
        }
         /* Toolbar Nav */
        else {
            showLoginView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loginSuccessful() {
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showFingerprintView() {
        Intent intent = new Intent(StartActivity.this, FingerprintActivity.class);
        intent.putExtra(Common.ARG_FINGERPRINT_TYPE, Common.WHICH_FINGERPRINT_LOGIN);
        startActivityForResult(intent, Common.FINGERPRINT_RESULT);
    }

    @Override
    public void showLoginView() {
        onLayoutTypeChange();
        mCreateAccountVisible = false;
        mResetPasswordVisible = false;
        binding.toolbar.setTitle(null);
        setUpEnabled(false);
        binding.editDisplayName.setVisibility(View.GONE);
        binding.viewSocialLayout.setVisibility(View.VISIBLE);
        binding.editPassword.setVisibility(View.VISIBLE);
        binding.textTitle.setVisibility(View.VISIBLE);
        binding.btnEmailSignIn.setVisibility(View.VISIBLE);
        binding.btnEmailCreateAccount.setVisibility(View.VISIBLE);
        binding.btnEmailCreateAccount.setBackgroundResource(R.color.transparent);
        binding.btnEmailForgot.setVisibility(View.VISIBLE);
        binding.btnEmailForgot.setText(R.string.forgot_password);
        binding.btnEmailForgot.setBackgroundResource(R.color.transparent);
    }

    @Override
    public void showCreateAccountView() {
        onLayoutTypeChange();
        mCreateAccountVisible = true;
        mResetPasswordVisible = false;
        binding.toolbar.setTitle(R.string.create_account);
        setUpEnabled(true);
        binding.editDisplayName.setVisibility(View.VISIBLE);
        binding.editPassword.setVisibility(View.VISIBLE);
        binding.viewSocialLayout.setVisibility(View.VISIBLE);
        binding.textTitle.setVisibility(View.GONE);
        binding.btnEmailSignIn.setVisibility(View.GONE);
        binding.btnEmailCreateAccount.setBackgroundResource(R.drawable.button_normal);
        binding.btnEmailForgot.setVisibility(View.GONE);
        binding.btnEmailForgot.setText(R.string.forgot_password);
        binding.btnEmailForgot.setBackgroundResource(R.color.transparent);
    }

    public void showForgotPasswordView() {
        mCreateAccountVisible = false;
        mResetPasswordVisible = true;
        onLayoutTypeChange();
        setUpEnabled(true);
        binding.toolbar.setTitle(R.string.reset_password);
        binding.btnEmailForgot.setText(R.string.reset_password);
        binding.btnEmailForgot.setBackgroundResource(R.drawable.button_normal);
        binding.editDisplayName.setVisibility(View.GONE);
        binding.viewSocialLayout.setVisibility(View.GONE);
        binding.textTitle.setVisibility(View.GONE);
        binding.btnEmailSignIn.setVisibility(View.GONE);
        binding.btnEmailCreateAccount.setVisibility(View.GONE);
        binding.btnEmailForgot.setVisibility(View.VISIBLE);
        binding.editPassword.setVisibility(View.GONE);
    }

    @Override
    public void showEmailRequired() {
        LoginRequestDataBSFragment.newInstance().show(getSupportFragmentManager(), "DIALOG");
    }

    @Override
    public void updateUserEmail(String email) {
        mPresenter.updateUserEmail(email);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(StartActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorName(String errorMessage) {
        binding.inputDisplayName.setError(errorMessage);
        binding.editDisplayName.requestFocus();
    }

    @Override
    public void showErrorEmail(String errorMessage) {
        binding.inputEmail.setError(errorMessage);
        binding.editEmail.requestFocus();
    }

    @Override
    public void showErrorPassword(String errorMessage) {
        binding.inputPassword.setError(errorMessage);
        binding.editPassword.requestFocus();
    }

    @Override
    public void showLoading(boolean showLoading) {
        binding.viewLoginHolder.setVisibility(showLoading ? View.GONE : View.VISIBLE);
        binding.viewProgress.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLoginFromEmail() {
        LayoutUtil.hideKeyboard(binding.inputEmail);
        resetErrors();
        mPresenter.launchSignInEmail(binding.editEmail.getText().toString(), binding.editPassword.getText().toString());
    }

    private void attemptCreateAccountFromEmail() {
        LayoutUtil.hideKeyboard(binding.inputEmail);
        resetErrors();
        mPresenter.createAccountEmail(binding.editDisplayName.getText().toString().trim(), binding.editEmail.getText().toString().trim(), binding.editPassword.getText().toString().trim());
    }

    /**
     * handleViews
     */
    private void resetErrors() {
        if (binding.inputEmail.getError() != null) {
            binding.inputEmail.setError(null);
        }
        if (binding.inputPassword.getError() != null) {
            binding.inputPassword.setError(null);
        }
    }

    private void onLayoutTypeChange() {
        LayoutUtil.hideKeyboard(binding.inputEmail);
        binding.viewGetFocus.requestFocus();
        binding.inputDisplayName.setError(null);
        binding.inputEmail.setError(null);
        binding.inputPassword.setError(null);
        binding.inputEmail.clearFocus();
        binding.inputPassword.clearFocus();
        binding.inputDisplayName.clearFocus();
    }

    private MultiTextWatcher.MultiTextWatcherInterface textWatcherInterface = new MultiTextWatcher.MultiTextWatcherInterface() {
        @Override
        public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
            if (editText == binding.editEmail || editText == binding.editPassword) {
                resetErrors();
            }
        }
    };
    /**
     * On Keyboard Sign in Clicked
     */
    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLoginFromEmail();
                return true;
            }
            return false;
        }
    };

    /**
     * Hide title if keyboard is visible
     */
    @Override
    public void onGlobalLayout() {
        int heightDiff = binding.viewRoot.getRootView().getHeight() - binding.viewRoot.getHeight();
        if (heightDiff > LayoutUtil.dpToPx(StartActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
            binding.textTitle.setVisibility(View.GONE);
        } else {
            if (mCreateAccountVisible || mResetPasswordVisible) {
                binding.textTitle.setVisibility(View.GONE);
            } else {
                binding.textTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Hide or show Toolbar nav icon
     *
     * @param showHomeAsUp boolean
     */
    private void setUpEnabled(boolean showHomeAsUp) {
        binding.toolbar.setNavigationIcon(showHomeAsUp ? ContextCompat.getDrawable(this, R.drawable.ic_arrow_back) : null);
    }
}
