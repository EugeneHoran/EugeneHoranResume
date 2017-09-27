package com.resume.horan.eugene.eugenehoranresume.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.main.MainActivity;
import com.resume.horan.eugene.eugenehoranresume.util.MultiTextWatcher;
import com.resume.horan.eugene.eugenehoranresume.util.TextInputView;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, OnClickListener {

    private static final List<String> mFacebookPermissionList = Arrays.asList("email", "public_profile");
    private boolean mCreateAccountVisible = false;

    private LoginContract.Presenter mPresenter;

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
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mCreateAccountVisible) {
            showLogin();
        } else {
            super.onBackPressed();
        }
    }

    // UI references.
    private TextInputView mEmailInputView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextInputView mPasswordInputView;
    private Button mEmailSignInButton;
    private Button mEmailCreateAccountButton;
    private SignInButton mGoogleSignIn;
    private LoginButton mFacebookSignIn;
    private View mMainHolder, mFingerprintHolder;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(this);

        mEmailView = findViewById(R.id.email);
        mEmailInputView = findViewById(R.id.emailInput);
        mPasswordView = findViewById(R.id.password);
        mPasswordInputView = findViewById(R.id.passwordInput);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailCreateAccountButton = findViewById(R.id.email_create_account_button);
        mGoogleSignIn = findViewById(R.id.googleSignIn);
        mFacebookSignIn = findViewById(R.id.facebookSignIn);
        mFacebookSignIn.setReadPermissions(mFacebookPermissionList);
        mMainHolder = findViewById(R.id.mainHolder);
        mFingerprintHolder = findViewById(R.id.fingerprintHolder);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        new MultiTextWatcher()
                .registerEditText(mEmailView)
                .registerEditText(mPasswordView)
                .setCallback(textWatcherInterface);
        mPasswordView.setOnEditorActionListener(onEditorActionListener);
        mEmailSignInButton.setOnClickListener(this);
        mEmailCreateAccountButton.setOnClickListener(this);
        mGoogleSignIn.setOnClickListener(this);

        new LoginPresenter(this);
        mPresenter.createGoogleClient(this);
        mPresenter.createFacebookClient();
    }

    @Override
    public void onClick(View v) {
        if (v == mEmailSignInButton) {
            attemptLoginFromEmail();
        } else if (v == mEmailCreateAccountButton) {
            if (mCreateAccountVisible) {
                attemptCreateAccountFromEmail();
            } else {
                showCreateAccount();
            }
        } else if (v == mGoogleSignIn) {
            mPresenter.signInGoogle(this);
        } else {
            showLogin();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void showLogin() {
        mCreateAccountVisible = false;
        setTitle("Login or Create Account");
        mPasswordInputView.setError(null);
        mEmailInputView.setError(null);
        setUpEnabled(false);
        mGoogleSignIn.setVisibility(View.VISIBLE);
        mFacebookSignIn.setVisibility(View.VISIBLE);
        mEmailSignInButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCreateAccount() {
        mCreateAccountVisible = true;
        setTitle("Create Account");
        mPasswordInputView.setError(null);
        mEmailInputView.setError(null);
        setUpEnabled(true);
        mGoogleSignIn.setVisibility(View.GONE);
        mFacebookSignIn.setVisibility(View.GONE);
        mEmailSignInButton.setVisibility(View.GONE);
    }

    @Override
    public void showFingerprint() {
        mPresenter.initFingerprint(this);
        mMainHolder.setVisibility(View.GONE);
        mFingerprintHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginSuccessful() {
        mPresenter.onDestroy();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }


    @Override
    public void showEmailError(String error) {
        mEmailInputView.setError(error);
        mEmailView.requestFocus();
    }

    @Override
    public void showPasswordError(String error) {
        mPasswordInputView.setError(error);
        mPasswordView.requestFocus();
    }

    @Override
    public void showLoading(boolean showLoading) {
        mLoginFormView.setVisibility(showLoading ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(showLoading ? View.VISIBLE : View.GONE);
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPasswordInputView.getWindowToken(), 0);
        mEmailInputView.setError(null);
        mPasswordInputView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        mPresenter.signInEmail(this, email, password);
    }

    private void attemptCreateAccountFromEmail() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mPasswordInputView.getWindowToken(), 0);
        mEmailInputView.setError(null);
        mPasswordInputView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        mPresenter.createAccountEmail(this, email, password);
    }

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
     * Reset errors
     */
    private MultiTextWatcher.MultiTextWatcherInterface textWatcherInterface = new MultiTextWatcher.MultiTextWatcherInterface() {
        @Override
        public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
            if (editText == mEmailView || editText == mPasswordView) {
                mEmailInputView.setError(null);
                mPasswordInputView.setError(null);
            }
        }
    };

    private void setUpEnabled(boolean showHomeAsUp) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }
}

