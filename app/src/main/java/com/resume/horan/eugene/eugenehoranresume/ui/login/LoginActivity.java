package com.resume.horan.eugene.eugenehoranresume.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.mattprecious.swirl.SwirlView;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.ui.main.MainActivity;
import com.resume.horan.eugene.eugenehoranresume.ui.settings.SettingsActivity;
import com.resume.horan.eugene.eugenehoranresume.util.LayoutUtil;
import com.resume.horan.eugene.eugenehoranresume.util.MultiTextWatcher;
import com.resume.horan.eugene.eugenehoranresume.util.ui.TextInputView;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private LoginContract.Presenter mPresenter;
    private boolean mCreateAccountVisible = false;

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onDestroy() {
        mViewRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mCreateAccountVisible) {
            showLogin();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(this, requestCode, resultCode, data);
    }

    // UI references.
    private View mViewRoot;
    private Toolbar mToolbar;
    private View mViewGetFocus;
    private View mViewLoginForm;
    private View mViewMainHolder;
    private TextView mTextTitle;
    private TextInputView mInputEmail;
    private EditText mEditEmail;
    private TextInputView mInputPassword;
    private EditText mEditPassword;
    private Button mBtnEmailSignIn;
    private Button mBtnEmailCreateAccount;
    private SignInButton mBtnGoogleSignIn;
    private View mViewFingerprintHolder;
    private TextView mTextFingerprint;
    private View mViewProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI references.
        mViewRoot = findViewById(R.id.viewRoot);
        mToolbar = findViewById(R.id.toolbar);
        mViewGetFocus = findViewById(R.id.viewGetFocus);
        mViewLoginForm = findViewById(R.id.viewLoginHolder);
        mViewMainHolder = findViewById(R.id.viewMainHolder);
        mTextTitle = findViewById(R.id.textTitle);
        mInputEmail = findViewById(R.id.inputEmail);
        mEditEmail = findViewById(R.id.editEmail);
        mInputPassword = findViewById(R.id.inputPassword);
        mEditPassword = findViewById(R.id.editPassword);
        mBtnEmailSignIn = findViewById(R.id.btnEmailSignIn);
        mBtnEmailCreateAccount = findViewById(R.id.btnEmailCreateAccount);
        mBtnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        LoginButton mBtnFacebookSignIn = findViewById(R.id.btnFacebookSignIn);
        mBtnFacebookSignIn.setReadPermissions(Arrays.asList("email", "public_profile"));
        mViewFingerprintHolder = findViewById(R.id.viewFingerprintHolder);
        mTextFingerprint = findViewById(R.id.textFingerprint);
        mViewProgress = findViewById(R.id.viewProgress);

        // Init Listeners
        new MultiTextWatcher()
                .registerEditText(mEditEmail)
                .registerEditText(mEditPassword)
                .setCallback(textWatcherInterface);
        mViewRoot.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mEditPassword.setOnEditorActionListener(onEditorActionListener);
        mToolbar.setNavigationOnClickListener(this);
        mBtnEmailSignIn.setOnClickListener(this);
        mBtnEmailCreateAccount.setOnClickListener(this);
        mBtnGoogleSignIn.setOnClickListener(this);

        // Init Presenter
        new LoginPresenter(this);
        mPresenter.createGoogleClient(this);
        mPresenter.createFacebookClient();
    }

    /**
     * onClickListener
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        if (v == mBtnEmailSignIn) {
            attemptLoginFromEmail();
        } else if (v == mBtnEmailCreateAccount) {
            if (mCreateAccountVisible) {
                attemptCreateAccountFromEmail();
            } else {
                showCreateAccount();
            }
        } else if (v == mBtnGoogleSignIn) {
            mPresenter.signInGoogle(this);
        }
        // Toolbar Nav Click
        else {
            showLogin();
        }
    }

    @Override
    public void loginSuccessful() {
        mPresenter.onDestroy();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void loginSuccessfulFingerprint() {
        mPresenter.onDestroy();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void showEmailError(String error) {
        mInputEmail.setError(error);
        mEditEmail.requestFocus();
    }

    @Override
    public void showPasswordError(String error) {
        mInputPassword.setError(error);
        mEditPassword.requestFocus();
    }

    @Override
    public void showLogin() {
        onLayoutTypeChange();
        mCreateAccountVisible = false;
        mToolbar.setTitle(null);
        setUpEnabled(false);
        mTextTitle.setVisibility(View.VISIBLE);
        mBtnEmailSignIn.setVisibility(View.VISIBLE);
        mBtnEmailCreateAccount.setBackgroundResource(R.color.transparent);
    }


    @Override
    public void showCreateAccount() {
        onLayoutTypeChange();
        mCreateAccountVisible = true;
        mToolbar.setTitle("Create Account");
        setUpEnabled(true);
        mTextTitle.setVisibility(View.GONE);
        mBtnEmailSignIn.setVisibility(View.GONE);
        mBtnEmailCreateAccount.setBackgroundResource(R.drawable.button_normal);
    }

    private void onLayoutTypeChange() {
        LayoutUtil.hideKeyboard(mInputEmail);
        mViewGetFocus.requestFocus();
        mInputEmail.setError(null);
        mInputPassword.setError(null);
        mInputEmail.clearFocus();
        mInputPassword.clearFocus();
    }

    @Override
    public void showFingerprint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((SwirlView) findViewById(R.id.swirlFinger)).setState(SwirlView.State.ON);
            mPresenter.initFingerprint(this);
            mViewMainHolder.setVisibility(View.GONE);
            mViewFingerprintHolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fingerprintAuthError() {
        mViewMainHolder.setVisibility(View.VISIBLE);
        mViewFingerprintHolder.setVisibility(View.GONE);
    }

    @Override
    public void showFingerprintMessage(String message, boolean isError) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTextFingerprint.setText(message);
            mTextFingerprint.setTextColor(ContextCompat.getColor(LoginActivity.this, isError ? R.color.redError : R.color.white));
            ((SwirlView) findViewById(R.id.swirlFinger)).setState(isError ? SwirlView.State.ERROR : SwirlView.State.ON);
        }
    }

    @Override
    public void showLoading(boolean showLoading) {
        mViewLoginForm.setVisibility(showLoading ? View.GONE : View.VISIBLE);
        mViewProgress.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLoginFromEmail() {
        LayoutUtil.hideKeyboard(mInputEmail);
        resetErrors();
        String email = mEditEmail.getText().toString();
        String password = mEditPassword.getText().toString();
        mPresenter.signInEmail(this, email, password);
    }

    private void attemptCreateAccountFromEmail() {
        LayoutUtil.hideKeyboard(mInputEmail);
        resetErrors();
        String email = mEditEmail.getText().toString();
        String password = mEditPassword.getText().toString();
        mPresenter.createAccountEmail(this, email, password);
    }

    /**
     * Reset Errors
     */
    private void resetErrors() {
        if (mInputEmail.getError() != null) {
            mInputEmail.setError(null);
        }
        if (mInputPassword.getError() != null) {
            mInputPassword.setError(null);
        }
    }

    /**
     * Multi Text Watcher
     * Reset the errors if any
     */
    private MultiTextWatcher.MultiTextWatcherInterface textWatcherInterface = new MultiTextWatcher.MultiTextWatcherInterface() {
        @Override
        public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
            if (editText == mEditEmail || editText == mEditPassword) {
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
        int heightDiff = mViewRoot.getRootView().getHeight() - mViewRoot.getHeight();
        if (heightDiff > LayoutUtil.dpToPx(LoginActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
            mTextTitle.setVisibility(View.GONE);
        } else {
            if (mCreateAccountVisible) {
                mTextTitle.setVisibility(View.GONE);
            } else {
                mTextTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Hide or show Toolbar nav icon
     *
     * @param showHomeAsUp boolean
     */
    private void setUpEnabled(boolean showHomeAsUp) {
        mToolbar.setNavigationIcon(showHomeAsUp ? ContextCompat.getDrawable(this, R.drawable.ic_arrow_back) : null);
    }
}

