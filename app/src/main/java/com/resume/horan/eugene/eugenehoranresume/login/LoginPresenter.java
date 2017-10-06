package com.resume.horan.eugene.eugenehoranresume.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.nullpresenters.LoginPresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.resume.horan.eugene.eugenehoranresume.util.Verify;

import java.util.HashMap;
import java.util.Map;

class LoginPresenter extends LoginPresenterNullCheck implements
        LoginContract.Presenter,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN_G = 133;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFacebookCallbackManager;
    private FirebaseAuth mAuth;
    private Context mContext;
    private SharedPreferences mSharedPref;

    LoginPresenter(Context context, LoginContract.View view) {
        mContext = context;
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        onAttachView(view);
        getView().setPresenter(this);
    }

    @Override
    public void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onDestroy() {
        onDetachView();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Create Login Clients
     */
    @Override
    public void createGoogleClient(LoginActivity loginView) {
        GoogleSignInOptions mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("142506979309-i9ugc7p696e97vl6c5ats62j8f0t4ep3.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(loginView)
                .enableAutoManage(loginView /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void createFacebookClient() {
        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken mFacebookAccessToken = loginResult.getAccessToken();
                AuthCredential credential = FacebookAuthProvider.getCredential(mFacebookAccessToken.getToken());
                mAuth.signInWithCredential(credential);
            }

            @Override
            public void onCancel() {
                getView().showLoading(false);
                getView().showToast("Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                getView().showLoading(false);
                getView().showToast("Error");
            }
        });
    }

    /**
     * Sign In Methods
     * <p>
     * Facebook SignInButton handles the login
     */

    @Override
    public void signInGoogle(LoginActivity loginView) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        loginView.startActivityForResult(signInIntent, RC_SIGN_IN_G);
        loginView.overridePendingTransition(0, 0);
    }

    @Override
    public void signInEmail(LoginActivity loginView, String email, String password) {
        if (fieldsVerified(loginView, email, password)) {
            getView().showLoading(true);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        getView().showLoading(false);
                        getView().showToast(task.getException().getMessage());
                    }
                }
            });
        }
    }


    @Override
    public void createAccountEmail(LoginActivity loginView, String email, String password) {
        if (fieldsVerified(loginView, email, password)) {
            getView().showLoading(true);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        getView().showLoading(false);
                        getView().showToast(task.getException().getMessage());
                    }
                }
            });
        }
    }

    /**
     * Verify Fields
     * return boolean
     */
    private boolean fieldsVerified(LoginActivity loginView, String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Log.e("Testing", "email isEmpty");
            getView().showEmailError(loginView.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isEmailValid(email)) {
            Log.e("Testing", "isEmailValid");
            getView().showEmailError(loginView.getString(R.string.error_invalid_email));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Log.e("Testing", "password isEmpty");
            getView().showPasswordError(loginView.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isPasswordValid(password)) {
            Log.e("Testing", "isPasswordValid");
            getView().showPasswordError(loginView.getString(R.string.error_incorrect_password));
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyEmail(LoginActivity loginView, String email) {
        if (TextUtils.isEmpty(email)) {
            Log.e("Testing", "email isEmpty");
            getView().showEmailError(loginView.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isEmailValid(email)) {
            Log.e("Testing", "isEmailValid");
            getView().showEmailError(loginView.getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }

    /**
     * returns the login status
     * <p>
     * Google
     * Facebook
     */
    @Override
    public void onActivityResult(LoginActivity loginView, int requestCode, int resultCode, Intent data) {
        getView().showLoading(true);
         /* Google Handler */
        if (requestCode == RC_SIGN_IN_G) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) { // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount mAccount = result.getSignInAccount();
                AuthCredential mAuthCredential = GoogleAuthProvider.getCredential(mAccount != null ? mAccount.getIdToken() : null, null);
                mAuth.signInWithCredential(mAuthCredential);
            } else {
                getView().showLoading(false);
                getView().showToast("Signed Out or Failure to Login");
            }
        }
         /* Facebook Handler */
        else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
            loginView.overridePendingTransition(0, 0);
        }
        /* Fingerprint Handler */
        else if (requestCode == Common.FINGERPRINT_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                getView().loginSuccessful();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                mSharedPref.edit().putBoolean(mContext.getString(R.string.pref_key_fingerprint_oauth), false).apply();
                mAuth.signOut();
                if (data != null) {
                    String reason = data.getExtras().getString(Common.ARG_FINGERPRINT_RETURN_ERROR);
                    getView().showToast(reason);
                }
                LoginManager.getInstance().logOut();
                getView().showLogin();
                getView().showLoading(false);
            }
        } else {
            getView().showLoading(false);
        }
    }

    /**
     * Listen for Firebase User changes
     */
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser mUser = firebaseAuth.getCurrentUser();
            if (mUser != null) {
                if (TextUtils.isEmpty(mUser.getEmail())) {
                    getView().showEmailRequired();
                    getView().showLoading(false);
                } else {
                    getView().showLoading(true);
                    writeNewUserIfNeeded();
                }
            }
        }
    };

    @Override
    public void resetEmail(LoginActivity loginView, String email) {
        if (verifyEmail(loginView, email)) {
            getView().showLoading(true);
            FirebaseUtil.getAuth().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            getView().showLoading(false);
                            if (task.isSuccessful()) {
                                getView().showLogin();
                                getView().showToast("Check your email");
                            } else {
                                getView().showLogin();
                                getView().showToast("There was an error");
                            }
                        }
                    });
        }
    }

    @Override
    public void userEmailUpdated(final String email) {
        getView().showLoading(true);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        writeNewUserIfNeeded();
                    } else {
                        getView().showToast("Problem Updating email");
                        getView().showLoading(false);
                    }
                }
            });
        else {
            getView().showLoading(false);
            getView().showToast("Problem with data");
        }
    }

    private void writeNewUserIfNeeded() {
        FirebaseUser firebaseUser = FirebaseUtil.getUser();
        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put("email", firebaseUser.getEmail() != null ? firebaseUser.getEmail() : "no_email");
        updateValues.put("displayName", !TextUtils.isEmpty(firebaseUser.getDisplayName()) ? firebaseUser.getDisplayName() : "Anonymous");
        updateValues.put("imageUrl", firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null);
        FirebaseUtil.getCurrentUserRef().updateChildren(updateValues, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    getView().showToast("Couldn't save user data: " + databaseError.getMessage());
                    getView().showLoading(false);
                    getView().showLogin();
                } else {
                    if (!mSharedPref.getBoolean(mContext.getString(R.string.pref_key_fingerprint_oauth), false)) {
                        getView().loginSuccessful();
                    } else {
                        getView().showFingerprint();
                        getView().showLoading(false);
                    }
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getView().showToast("Google Play Services Error");
        getView().showLoading(false);
    }


}
