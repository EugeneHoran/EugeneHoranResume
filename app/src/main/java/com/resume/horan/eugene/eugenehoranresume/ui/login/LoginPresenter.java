package com.resume.horan.eugene.eugenehoranresume.ui.login;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.nullpresenters.LoginPresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.Prefs;
import com.resume.horan.eugene.eugenehoranresume.util.Verify;
import com.resume.horan.eugene.eugenehoranresume.util.fingerprintassistant.helper.FingerprintHelper;
import com.resume.horan.eugene.eugenehoranresume.util.fingerprintassistant.helper.FingerprintResultsHandler;
import com.resume.horan.eugene.eugenehoranresume.util.fingerprintassistant.interfaces.FingerprintAuthListener;
import com.resume.horan.eugene.eugenehoranresume.util.fingerprintassistant.util.FingerprintResponseCode;

class LoginPresenter extends LoginPresenterNullCheck implements
        LoginContract.Presenter,
        GoogleApiClient.OnConnectionFailedListener,
        FingerprintAuthListener {
    private static final int RC_SIGN_IN_G = 133;
    // Prevent mAuthListener being called twice (Only happens when NightMode = Yes)
    private boolean mAuthFlag = true;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFacebookCallbackManager;
    private FingerprintHelper fingerPrintHelper;
    private FingerprintResultsHandler fingerprintResultsHandler;

    private FirebaseAuth mAuth;

    LoginPresenter(LoginContract.View view) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fingerprintResultsHandler != null) {
                fingerprintResultsHandler.stopListening();
            }
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

    /**
     * returns the login status
     * <p>
     * Google
     * Facebook
     */
    @Override
    public void onActivityResult(LoginActivity loginView, int requestCode, int resultCode, Intent data) {
        getView().showLoading(true);
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
        } else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
            loginView.overridePendingTransition(0, 0);
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
            if (mUser != null && mAuthFlag) {
                getView().showLoading(true);
                writeNewUserIfNeeded(mUser.getUid(), "test@email.com", "Eugene Horan");
            }
        }
    };

    private void writeNewUserIfNeeded(final String userId, final String username, final String name) {
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(userId)) {
                    usersRef.child(userId).setValue(new User(username, name));
                }
                if (!Prefs.getBoolean(Common.PREF_FINGERPRINT, false)) {
                    getView().loginSuccessful();
                } else {
                    getView().showFingerprint();
                    getView().showLoading(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getView().showLoading(false);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getView().showToast("Google Play Services Error");
        getView().showLoading(false);
    }


    @Override
    public void initFingerprint(LoginActivity loginView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerForFingerprintService(loginView);
            if (fingerprintResultsHandler != null && !fingerprintResultsHandler.isAlreadyListening()) {
                fingerprintResultsHandler.startListening(fingerPrintHelper.getFingerprintManager(), fingerPrintHelper.getCryptoObject());
            }
        }
    }

    /**
     * Fingerprint
     */
    @Override
    public void onAuthentication(int helpOrErrorCode, CharSequence infoString, FingerprintManager.AuthenticationResult authenticationResult, int authCode) {
        switch (authCode) {
            case FingerprintResponseCode.AUTH_ERROR:
                // Called when an unrecoverable error has been encountered and the operation is complete.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (fingerprintResultsHandler != null) {
                        fingerprintResultsHandler.stopListening();
                    }
                }
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                getView().fingerprintAuthError();
                getView().showToast("Too many tries. Please login");
                break;
            case FingerprintResponseCode.AUTH_FAILED:
                // Called when a fingerprint is valid but not recognized.
                getView().showFingerprintMessage("Fingerprint Authentication Failed", true);
                break;
            case FingerprintResponseCode.AUTH_HELP:
                // Called when a recoverable error has been encountered during authentication.
                getView().showFingerprintMessage("Make sure your finger is on the scanner", false);
                break;
            case FingerprintResponseCode.AUTH_SUCCESS:
                // Do whatever you want
                getView().showFingerprintMessage("Login successful", false);
                getView().loginSuccessfulFingerprint();
                break;
        }
    }

    //TODO work on this
    private void registerForFingerprintService(LoginActivity loginView) {
        fingerPrintHelper = new FingerprintHelper(loginView, Common.KEY_FINGERPRINT);
        switch (fingerPrintHelper.checkAndEnableFingerPrintService()) {
            case FingerprintResponseCode.FINGERPRINT_SERVICE_INITIALISATION_SUCCESS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fingerprintResultsHandler = new FingerprintResultsHandler(loginView);
                    fingerprintResultsHandler.setFingerprintAuthListener(this);
                    fingerprintResultsHandler.startListening(fingerPrintHelper.getFingerprintManager(), fingerPrintHelper.getCryptoObject());
                }
                getView().showToast("Fingerprint sensor started scanning");
                break;
            case FingerprintResponseCode.OS_NOT_SUPPORTED:
                getView().showToast("OS doesn't support fingerprint api");
                break;
            case FingerprintResponseCode.FINGER_PRINT_SENSOR_UNAVAILABLE:
                getView().showToast("Fingerprint sensor not found");
                break;
            case FingerprintResponseCode.ENABLE_FINGER_PRINT_SENSOR_ACCESS:
                getView().showToast("Give access to use fingerprint sensor");
                break;
            case FingerprintResponseCode.NO_FINGER_PRINTS_ARE_ENROLLED:
                break;
            case FingerprintResponseCode.FINGERPRINT_SERVICE_INITIALISATION_FAILED:
                break;
            case FingerprintResponseCode.DEVICE_NOT_KEY_GUARD_SECURED:
                break;
        }
    }
}
