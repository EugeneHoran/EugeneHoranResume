package com.resume.horan.eugene.eugenehoranresume.post;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.Author;
import com.resume.horan.eugene.eugenehoranresume.model.Post;
import com.resume.horan.eugene.eugenehoranresume.model.User;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.FirebaseUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rapid.decoder.BitmapDecoder;

public class NewPostViewHolder extends BaseObservable {
    private static final String TAG = "NewPostActivity";
    private static final String[] cameraPerms = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int TC_PICK_IMAGE = 101;
    private static final int RC_CAMERA_PERMISSIONS = 102;
    // Image sizes
    private static final int THUMBNAIL_MAX_DIMENSION = 640;
    private static final int FULL_SIZE_MAX_DIMENSION = 1280;

    private User user;
    private String userName;
    private String userImageUrl;
    private String userId;
    private boolean isLoading = false;
    private DatabaseReference userReference;
    private NewPostActivity activity;

    NewPostViewHolder(Activity activity) {
        this.activity = (NewPostActivity) activity;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseUtil.getCurrentUserRef();
        loadUserData();
    }

    /**
     * Handle Post
     */
    private Author getAuthor() {
        return new Author(
                user.getDisplayName() != null ? user.getDisplayName() : "Anonymous",
                user.getImageUrl() != null ? user.getImageUrl() : null,
                userId);
    }

    void makePost(String message) {
        if (mFileUri == null && TextUtils.isEmpty(message)) {
            showError("Nothing to post");
            return;
        }
        setLoading(true);
        if (mFileUri == null && !TextUtils.isEmpty(message)) {
            postMessage(message);
            return;
        }
        if (mFileUri != null && TextUtils.isEmpty(message)) {
            makePost();
            return;
        }
        if (mFileUri != null && !TextUtils.isEmpty(message)) {
            postMessageImage(message);
        }
    }

    private void postMessage(String message) {
        Post newPost = new Post(getAuthor(), null, null, null, null, message, ServerValue.TIMESTAMP, Common.TYPE_POST_MESSAGE);
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        final String newPostKey = postsRef.push().getKey();
        Map<String, Object> updatedUserData = new HashMap<>();
        updatedUserData.put("users/" + getAuthor().getUid() + "/posts/" + newPostKey, true);
        updatedUserData.put("posts/" + newPostKey, new ObjectMapper().convertValue(newPost, Map.class));
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                setLoading(false);
                if (firebaseError == null) {
                    postUploaded();
                } else {
                    Log.e(TAG, "Unable to create new post: " + firebaseError.getMessage());
                    showError(activity.getString(R.string.error_upload_task_create));
                }
            }
        });
    }

    private void makePost() {
        String fileName = mFileUri.getLastPathSegment();
        WeakReference<Bitmap> bitmapReference = new WeakReference<>(mResizedBitmap);
        WeakReference<Bitmap> thumbnailReference = new WeakReference<>(mThumbnail);
        Bitmap fullSize = bitmapReference.get();
        final Bitmap thumbnail = thumbnailReference.get();
        if (fullSize == null || thumbnail == null) {
            return;
        }
        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference photoRef = storageRef.getReferenceFromUrl("gs://" + activity.getString(R.string.google_storage_bucket));
        Long timestamp = System.currentTimeMillis();
        final StorageReference fullSizeRef = photoRef.child(userId).child("full").child(timestamp.toString()).child(fileName + ".jpg");
        final StorageReference thumbnailRef = photoRef.child(userId).child("thumb").child(timestamp.toString()).child(fileName + ".jpg");
        Log.d(TAG, fullSizeRef.toString());
        Log.d(TAG, thumbnailRef.toString());

        ByteArrayOutputStream fullSizeStream = new ByteArrayOutputStream();
        fullSize.compress(Bitmap.CompressFormat.JPEG, 90, fullSizeStream);
        byte[] bytes = fullSizeStream.toByteArray();
        fullSizeRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri fullSizeUrl = taskSnapshot.getDownloadUrl();
                ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, thumbnailStream);
                thumbnailRef.putBytes(thumbnailStream.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
                        final String newPostKey = postsRef.push().getKey();
                        final Uri thumbnailUrl = taskSnapshot.getDownloadUrl();
                        Author author = getAuthor();
                        if (author == null) {
                            showError(activity.getString(R.string.error_user_not_signed_in));
                            return;
                        }
                        Post newPost = new Post(author, fullSizeUrl.toString(), fullSizeRef.toString(), thumbnailUrl.toString(), thumbnailRef.toString(), null, ServerValue.TIMESTAMP, Common.TYPE_POST_IMAGE);
                        Map<String, Object> updatedUserData = new HashMap<>();
                        updatedUserData.put("users/" + author.getUid() + "/posts/" + newPostKey, true);
                        updatedUserData.put("posts/" + newPostKey, new ObjectMapper().convertValue(newPost, Map.class));
                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                                setLoading(false);
                                if (firebaseError == null) {
                                    postUploaded();
                                } else {
                                    Log.e(TAG, "Unable to create new post: " + firebaseError.getMessage());
                                    showError(activity.getString(R.string.error_upload_task_create));
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(activity.getString(R.string.error_upload_task_create));
                        setLoading(false);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setLoading(false);
                showError(activity.getString(R.string.error_upload_task_create));
            }
        });
    }

    private void postMessageImage(String message) {
        final String text = message;
        String fileName = mFileUri.getLastPathSegment();
        WeakReference<Bitmap> bitmapReference = new WeakReference<>(mResizedBitmap);
        WeakReference<Bitmap> thumbnailReference = new WeakReference<>(mThumbnail);
        Bitmap fullSize = bitmapReference.get();
        final Bitmap thumbnail = thumbnailReference.get();
        if (fullSize == null || thumbnail == null) {
            return;
        }
        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference photoRef = storageRef.getReferenceFromUrl("gs://" + activity.getString(R.string.google_storage_bucket));
        Long timestamp = System.currentTimeMillis();
        final StorageReference fullSizeRef = photoRef.child(userId).child("full").child(timestamp.toString()).child(fileName + ".jpg");
        final StorageReference thumbnailRef = photoRef.child(userId).child("thumb").child(timestamp.toString()).child(fileName + ".jpg");
        Log.d(TAG, fullSizeRef.toString());
        Log.d(TAG, thumbnailRef.toString());

        ByteArrayOutputStream fullSizeStream = new ByteArrayOutputStream();
        fullSize.compress(Bitmap.CompressFormat.JPEG, 90, fullSizeStream);
        byte[] bytes = fullSizeStream.toByteArray();
        fullSizeRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri fullSizeUrl = taskSnapshot.getDownloadUrl();
                ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, thumbnailStream);
                thumbnailRef.putBytes(thumbnailStream.toByteArray())
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
                                final String newPostKey = postsRef.push().getKey();
                                final Uri thumbnailUrl = taskSnapshot.getDownloadUrl();
                                Author author = getAuthor();
                                if (author == null) {
                                    setLoading(false);
                                    showError(activity.getString(R.string.error_user_not_signed_in));
                                    return;
                                }
                                Post newPost = new Post(author, fullSizeUrl.toString(), fullSizeRef.toString(), thumbnailUrl.toString(), thumbnailRef.toString(), text, ServerValue.TIMESTAMP, Common.TYPE_POST_MESSAGE_IMAGE);
                                Map<String, Object> updatedUserData = new HashMap<>();
                                updatedUserData.put("users/" + author.getUid() + "/posts/" + newPostKey, true);
                                updatedUserData.put("posts/" + newPostKey, new ObjectMapper().convertValue(newPost, Map.class));
                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                                        setLoading(false);
                                        if (firebaseError == null) {
                                            postUploaded();
                                        } else {
                                            Log.e(TAG, "Unable to create new post: " + firebaseError.getMessage());
                                            showError(activity.getString(R.string.error_upload_task_create));
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setLoading(false);
                        showError(activity.getString(R.string.error_upload_task_create));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setLoading(false);
                showError(activity.getString(R.string.error_upload_task_create));
            }
        });
    }

    private void postUploaded() {
        Toast.makeText(activity, "Post created!", Toast.LENGTH_SHORT).show();
        activity.finish();
    }

    private void showError(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle User Data
     */
    private void loadUserData() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    setUserName(user.getDisplayName() != null ? user.getDisplayName() : "Anonymous");
                    setUserImageUrl(user.getImageUrl());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Image Handler
     */
    private Uri mFileUri;
    private Bitmap mResizedBitmap;
    private Bitmap mThumbnail;

    @AfterPermissionGranted(RC_CAMERA_PERMISSIONS)
    public void showImagePicker() {
        if (!EasyPermissions.hasPermissions(activity, cameraPerms)) {
            EasyPermissions.requestPermissions(activity, "This sample will upload a picture from your Camera", RC_CAMERA_PERMISSIONS, cameraPerms);
            return;
        }
        // Choose file storage location
        File file = new File(activity.getExternalCacheDir(), UUID.randomUUID().toString());
        mFileUri = Uri.fromFile(file);
        // Camera
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = activity.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
            cameraIntents.add(intent);
        }
        // Image Picker
        Intent pickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooserIntent = Intent.createChooser(pickerIntent, activity.getString(R.string.picture_chooser_title));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        activity.startActivityForResult(chooserIntent, TC_PICK_IMAGE);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TC_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                final boolean isCamera;
                if (data.getData() == null) {
                    isCamera = true;
                } else {
                    isCamera = MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction());
                }
                if (!isCamera) {
                    mFileUri = data.getData();
                }
                onBitmapResized(BitmapDecoder.from(activity, mFileUri).scaleBy((float) 0.5).decode(), FULL_SIZE_MAX_DIMENSION);
                onBitmapResized(BitmapDecoder.from(activity, mFileUri).scaleBy((float) 0.25).decode(), THUMBNAIL_MAX_DIMENSION);
            }
        }
    }

    private void onBitmapResized(Bitmap resizedBitmap, int mMaxDimension) {
        if (resizedBitmap == null) {
            Log.e(TAG, "Couldn't resize bitmap in background task.");
            Toast.makeText(activity, "Couldn't resize bitmap.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mMaxDimension == THUMBNAIL_MAX_DIMENSION) {
            mThumbnail = resizedBitmap;
        } else if (mMaxDimension == FULL_SIZE_MAX_DIMENSION) {
            mResizedBitmap = resizedBitmap;
            setImageBitmap(mResizedBitmap);
        }
    }

    /**
     * On Click Listeners
     */
    public void onAddImageClick(View view) {
        showImagePicker();
    }

    public void onRemoveBitmapClicked(View view) {
        mThumbnail = null;
        mResizedBitmap = null;
        setImageBitmap(null);
    }


    /**
     * Getters And Setters
     */

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
        notifyChange();
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
        notifyChange();
    }

    @Bindable
    public String getUserImageUrl() {
        return userImageUrl;
    }

    private void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
        notifyChange();
    }

    private Bitmap imageBitmap;

    @Bindable
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    private void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
        notifyChange();
    }

}
