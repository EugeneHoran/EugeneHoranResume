package com.resume.horan.eugene.eugenehoranresume.start;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
import com.resume.horan.eugene.eugenehoranresume.BuildConfig;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.nullpresenters.StartPresenterNullCheck;
import com.resume.horan.eugene.eugenehoranresume.model.AppVersion;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;
import com.resume.horan.eugene.eugenehoranresume.util.ui.LayoutUtil;
import com.resume.horan.eugene.eugenehoranresume.util.ui.Verify;

import java.util.HashMap;
import java.util.Map;

public class StartPresenter extends StartPresenterNullCheck implements StartContract.Presenter {
    private static final String TAG = "StartPresenter";
    private FragmentActivity activity;
    private Resources res;
    private SharedPreferences mSharedPref;
    private boolean requiresFingerprint;


    public ObservableField<String> navTitle = new ObservableField<>();
    public ObservableField<Integer> navIcon = new ObservableField<>(R.drawable.ic_null);
    public ObservableField<Boolean> showLoading = new ObservableField<>(false);
    public ObservableField<Boolean> showTitle = new ObservableField<>(true);
    public ObservableField<Boolean> showDisplayName = new ObservableField<>(false);
    public ObservableField<Boolean> showPassword = new ObservableField<>(true);
    public ObservableField<Boolean> showSocial = new ObservableField<>(true);
    public ObservableField<Boolean> showSignIn = new ObservableField<>(true);
    public ObservableField<Boolean> showCreateAccount = new ObservableField<>(true);
    public ObservableField<Boolean> showForgotPassword = new ObservableField<>(true);
    public ObservableField<String> forgotText = new ObservableField<>();


    @Override
    public void onStart() {
        checkVersion();
    }

    private void checkVersion() {
        FirebaseUtil.getVersionRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final AppVersion appVersion = dataSnapshot.getValue(AppVersion.class);
                if (appVersion.getVersion() == BuildConfig.VERSION_CODE) {
                    handleUser();
                } else {
                    new AlertDialog.Builder(activity)
                            .setTitle("Available Update")
                            .setCancelable(false)
                            .setMessage("Update is required to use app!")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appVersion.getLink()));
                                    activity.startActivity(browserIntent);
                                    activity.finish();
                                }
                            }).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        onDetachView();
    }

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFacebookCallbackManager;

    StartPresenter(Activity activity, StartContract.View view) {
        this.activity = (FragmentActivity) activity;
        this.res = activity.getResources();
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        requiresFingerprint = mSharedPref.getBoolean(res.getString(R.string.pref_key_fingerprint_oauth), false);
        // Initiate views
        this.onAttachView(view);
        this.getView().setPresenter(this);
        // Initialize login clients
        initiateLoginClients();
    }

    public void onSignEmailSignIn(View view) {
        LayoutUtil.hideKeyboard(view);
    }

    public void showLoginView() {
        navIcon.set(R.drawable.ic_null);
        navTitle.set(null);
        showTitle.set(true);
        showDisplayName.set(false);
        showPassword.set(true);
        showSocial.set(true);
        showSignIn.set(true);
        showCreateAccount.set(true);
        showForgotPassword.set(true);
        forgotText.set(res.getString(R.string.forgot_password));
    }

    public void showCreateAccountView() {
        navIcon.set(R.drawable.ic_arrow_back);
        navTitle.set(res.getString(R.string.create_account));
        showTitle.set(false);
        showDisplayName.set(true);
        showPassword.set(true);
        showSocial.set(true);
        showSignIn.set(false);
        showCreateAccount.set(true);
        showForgotPassword.set(false);
        forgotText.set(res.getString(R.string.forgot_password));
    }

    public void showForgotPasswordView() {
        navIcon.set(R.drawable.ic_arrow_back);
        navTitle.set(res.getString(R.string.reset_password));
        showTitle.set(false);
        showDisplayName.set(false);
        showPassword.set(false);
        showSocial.set(false);
        showSignIn.set(false);
        showCreateAccount.set(false);
        showForgotPassword.set(true);
        forgotText.set(res.getString(R.string.reset_password));
    }

    public void showLoading(boolean show) {
        showLoading.set(show);
    }

    private void handleUser() {
        FirebaseUser firebaseUser = FirebaseUtil.getUser();
        if (firebaseUser == null) {
            showLoading(false);
            getView().showLoginView();
        } else {
            if (TextUtils.isEmpty(firebaseUser.getEmail())) {
                showLoading(false);
                getView().showEmailRequired();
            } else {
                showLoading(true);
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
        FirebaseUtil.getNewUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    FirebaseUtil.getCurrentUserRef().updateChildren(getUserDetails(firebaseUser), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            // No response
                            if (databaseError != null) {
                                showLoading(false);
                                //
                                getView().showLoginView();
                                getView().showToast(databaseError.getMessage());
                            } else {
                                if (requiresFingerprint) {
                                    showLoading(false);
                                    getView().showFingerprintView();
                                } else {
                                    getView().loginSuccessful();
                                }
                            }
                        }
                    });
                } else {
                    if (requiresFingerprint) {
                        showLoading(false);
                        getView().showFingerprintView();
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
                        showLoading(false);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .requestProfile()
                                .requestIdToken(BuildConfig.GSO_TOKEN_ID)
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
                showLoading(false);
                getView().showToast("Facebook Canceled Login");
            }

            @Override
            public void onError(FacebookException error) {
                showLoading(false);
                getView().showToast("Facebook Login Error {" + error.getMessage() + "}");
            }
        });
    }

    private void firebaseAuthCredential(AuthCredential credential) {
        showLoading(true);
        FirebaseUtil.getAuth().signInWithCredential(credential)
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
                AuthCredential credential = GoogleAuthProvider.getCredential(acct != null ? acct.getIdToken() : null, null);
                firebaseAuthCredential(credential);
            } else {
                showLoading(false);
                Log.e(TAG, "Google Sign-In failed.");
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
                FirebaseUtil.getAuth().signOut();
                if (data != null) {
                    String reason = data.getExtras().getString(Common.ARG_FINGERPRINT_RETURN_ERROR);
                    getView().showToast(reason);
                }

                showLoading(false);
                LoginManager.getInstance().logOut();
                getView().showLoginView();
            }
        } else {
            showLoading(false);
            getView().showLoginView();
        }
    }

    /**
     * Email
     */
    @Override
    public void createAccountEmail(final String displayName, String email, String password) {
        if (verifyNameAndEmailAndPassword(displayName, email, password)) {
            showLoading(true);
            FirebaseUtil.getAuth().createUserWithEmailAndPassword(email, password)
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
                                showLoading(false);
                                getView().showToast(task.getException().getMessage());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showLoading(false);
                            getView().showToast(e.getMessage());
                        }
                    });
        }
    }

    @Override
    public void launchSignInEmail(String email, String password) {
        if (verifyEmailAndPassword(email, password)) {
            showLoading(true);
            FirebaseUtil.getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        handleUser();
                    } else {
                        showLoading(false);
                        getView().showToast(task.getException().getMessage());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showLoading(false);
                    getView().showToast(e.getMessage());
                }
            });
        }
    }

    @Override
    public void updateUserEmail(String email) {
        showLoading(true);
        final FirebaseUser user = FirebaseUtil.getUser();
        if (user != null)
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        handleUser();
                    } else {
                        getView().showEmailRequired();
                        //noinspection ThrowableResultOfMethodCallIgnored,ConstantConditions
                        getView().showToast(task.getException().getMessage() != null ? task.getException().getMessage() : "Error");
                        showLoading(false);
                    }
                }
            });
        else {
            showLoading(false);
            getView().showToast("Problem with data");
        }
    }

    @Override
    public void resetEmail(String email) {
        if (verifyEmail(email)) {
            showLoading(true);
            FirebaseUtil.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    showLoading(false);
                    getView().showLoginView();
                    getView().showToast(task.isSuccessful() ? "Check your email" : "There was an error");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showLoading(false);
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
