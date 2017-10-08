package com.resume.horan.eugene.eugenehoranresume.start;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.nullpresenters.StartPresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.Verify;

import java.util.HashMap;
import java.util.Map;

public class StartPresenter extends StartPresenterNullCheck implements StartContract.Presenter {
    private static final String TAG = "StartPresenter";
    private FragmentActivity activity;
    private Resources res;
    private SharedPreferences mSharedPref;
    private boolean requiresFingerprint;

    @Override
    public void onStart() {
//        FirebaseUser mCurrentUser = mFirebaseAuth.getCurrentUser();
        handleUser();
    }

    @Override
    public void onDestroy() {
        onDetachView();
    }

    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFacebookCallbackManager;

    public StartPresenter(Activity activity, StartContract.View view) {
        this.activity = (FragmentActivity) activity;
        this.res = activity.getResources();
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        requiresFingerprint = mSharedPref.getBoolean(res.getString(R.string.pref_key_fingerprint_oauth), false);
        // Initialize authentication and set up callbacks
        this.mFirebaseAuth = FirebaseAuth.getInstance();
        // Initiate views
        this.onAttachView(view);
        this.getView().setPresenter(this);
        // Initialize login clients
        initiateLoginClients();
    }

    private void handleUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            getView().showLoginView();
            getView().showLoading(false);
        } else {
            if (TextUtils.isEmpty(firebaseUser.getEmail())) {
                getView().showEmailRequired();
                getView().showLoading(false);
            } else {
                getView().showLoading(true);
                addOrUpdateUser(firebaseUser);
            }
        }
    }

    private Map<String, Object> getUserDetails(FirebaseUser firebaseUser) {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("email", firebaseUser.getEmail() != null ? firebaseUser.getEmail() : "no_email");
        userDetails.put("displayName", !TextUtils.isEmpty(firebaseUser.getDisplayName()) ? firebaseUser.getDisplayName() : "Anonymous");
        userDetails.put("imageUrl", firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null);
        return userDetails;
    }

    private void addOrUpdateUser(final FirebaseUser firebaseUser) {
        FirebaseDatabase.getInstance().getReference().child(Common.FB_REF_USERS).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    FirebaseDatabase.getInstance().getReference().child(Common.FB_REF_USERS).child(firebaseUser.getUid())
                            .updateChildren(getUserDetails(firebaseUser), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    // No response
                                    if (databaseError != null) {
                                        getView().showLoading(false);
                                        getView().showLoginView();
                                        getView().showToast(databaseError.getMessage());
                                    } else {
                                        if (requiresFingerprint) {
                                            getView().showFingerprintView();
                                            getView().showLoading(false);
                                        } else {
                                            getView().loginSuccessful();
                                        }
                                    }
                                }
                            });
                } else {
                    if (requiresFingerprint) {
                        getView().showFingerprintView();
                        getView().showLoading(false);
                    } else {
                        getView().loginSuccessful();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initiateLoginClients() {
        // GoogleApiClient with Sign In
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        getView().showToast("Google Play Connection Failed");
                        getView().showLoading(false);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .requestProfile()
                                .requestIdToken(res.getString(R.string.gso_token_id))
                                .build()
                ).build();
        // FacebookSignIn with Sign In
        mFacebookCallbackManager = CallbackManager.Factory.create();
        // FacebookSignIn Callbacks
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()));
            }

            @Override
            public void onCancel() {
                getView().showLoading(false);
                getView().showToast("Facebook Canceled Login");
            }

            @Override
            public void onError(FacebookException error) {
                getView().showLoading(false);
                getView().showToast("Facebook Login Error {" + error.getMessage() + "}");
            }
        });
    }

    private void firebaseAuthCredential(AuthCredential credential) {
        getView().showLoading(true);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(activity, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult result) {
                        handleUser();
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "auth:onFailure:" + e.getMessage());
                        getView().showToast(e.getMessage());
                        handleUser();
                    }
                });
    }

    @Override
    public void launchSignInGoogleIntent() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, Common.RC_SIGN_IN_G);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Common.RC_SIGN_IN_G) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e(TAG, "handleSignInResult:" + result.getStatus());
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                firebaseAuthCredential(credential);
            } else {
                Log.e(TAG, "Google Sign-In failed.");
                getView().showLoading(false);
                getView().showToast("Google Sign-In failed.");
            }
        }
        /* Facebook Handler */
        else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        /* Fingerprint Handler */
        else if (requestCode == Common.FINGERPRINT_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                getView().loginSuccessful();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                mSharedPref.edit().putBoolean(res.getString(R.string.pref_key_fingerprint_oauth), false).apply();
                mFirebaseAuth.signOut();
                if (data != null) {
                    String reason = data.getExtras().getString(Common.ARG_FINGERPRINT_RETURN_ERROR);
                    getView().showToast(reason);
                }
                LoginManager.getInstance().logOut();
                getView().showLoginView();
                getView().showLoading(false);
            }
        } else {
            getView().showLoginView();
            getView().showLoading(false);
        }
    }

    /**
     * Email
     */
    @Override
    public void createAccountEmail(final String displayName, String email, String password) {
        if (verifyNameAndEmailAndPassword(displayName, email, password)) {
            getView().showLoading(true);
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    handleUser();
                                                }
                                            }
                                        });
                            } else {
                                getView().showLoading(false);
                                getView().showToast(task.getException().getMessage());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getView().showLoading(false);
                            getView().showToast(e.getMessage());
                        }
                    });
        }
    }

    @Override
    public void launchSignInEmail(String email, String password) {
        if (verifyEmailAndPassword(email, password)) {
            getView().showLoading(true);
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        handleUser();
                    } else {
                        getView().showLoading(false);
                        getView().showToast(task.getException().getMessage());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getView().showLoading(false);
                    getView().showToast(e.getMessage());
                }
            });
        }
    }

    @Override
    public void updateUserEmail(String email) {
        getView().showLoading(true);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        handleUser();
                    } else {
                        getView().showEmailRequired();
                        getView().showToast(task.getException().getMessage());
                        getView().showLoading(false);
                    }
                }
            });
        else {
            getView().showLoading(false);
            getView().showToast("Problem with data");
        }
    }

    @Override
    public void resetEmail(String email) {
        if (verifyEmail(email)) {
            getView().showLoading(true);
            mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    getView().showLoading(false);
                    getView().showLoginView();
                    getView().showToast(task.isSuccessful() ? "Check your email" : "There was an error");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getView().showLoading(false);
                    getView().showToast(e.getMessage());
                    getView().showLoginView();
                }
            });
        }
    }

    /**
     * Verify Fields
     */
    private boolean verifyEmailAndPassword(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            getView().showErrorEmail(res.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isEmailValid(email)) {
            getView().showErrorEmail(res.getString(R.string.error_invalid_email));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            getView().showErrorPassword(res.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isPasswordValid(password)) {
            getView().showErrorPassword(res.getString(R.string.error_incorrect_password));
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyNameAndEmailAndPassword(String displayName, String email, String password) {
        if (TextUtils.isEmpty(displayName)) {
            getView().showErrorName(res.getString(R.string.error_field_required));
            return false;
        } else if (TextUtils.isEmpty(email)) {
            getView().showErrorEmail(res.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isEmailValid(email)) {
            getView().showErrorEmail(res.getString(R.string.error_invalid_email));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            getView().showErrorPassword(res.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isPasswordValid(password)) {
            getView().showErrorPassword(res.getString(R.string.error_incorrect_password));
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            getView().showErrorPassword(res.getString(R.string.error_field_required));
            return false;
        } else if (!Verify.isEmailValid(email)) {
            getView().showErrorPassword(res.getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }
}
