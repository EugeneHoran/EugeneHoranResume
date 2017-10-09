package com.resume.horan.eugene.eugenehoranresume.post;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.model.Author;
import com.resume.horan.eugene.eugenehoranresume.model.Post;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class NewPostUploadFragment extends Fragment {
    private static final String TAG = "NewPostTaskFragment";

    public interface TaskCallbacks {
        void onPostUploaded(String error);
    }

    private Context mApplicationContext;
    private TaskCallbacks mCallbacks;
    private Bitmap selectedBitmap;
    private Bitmap thumbnail;

    public NewPostUploadFragment() {
        // Required empty public constructor
    }

    public static NewPostUploadFragment newInstance() {
        return new NewPostUploadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across config changes.
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskCallbacks) {
            mCallbacks = (TaskCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TaskCallbacks");
        }
        mApplicationContext = context.getApplicationContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void setSelectedBitmap(Bitmap bitmap) {
        this.selectedBitmap = bitmap;
    }

    public Bitmap getSelectedBitmap() {
        return selectedBitmap;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void uploadPost(Bitmap bitmap, Bitmap thumbnail, String inFileName, String inPostText) {
        UploadPostTask uploadTask = new UploadPostTask(bitmap, thumbnail, inFileName, inPostText);
        uploadTask.execute();
    }

    class UploadPostTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Bitmap> bitmapReference;
        private WeakReference<Bitmap> thumbnailReference;
        private String postText;
        private String fileName;

        private String mUserId;

        public UploadPostTask(Bitmap bitmap, Bitmap thumbnail, String inFileName, String inPostText) {
            bitmapReference = new WeakReference<>(bitmap);
            thumbnailReference = new WeakReference<>(thumbnail);
            postText = inPostText;
            fileName = inFileName;
        }

        public Author getAuthor() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) return null;
            return new Author(
                    user.getDisplayName() != null ? user.getDisplayName() : "Anonymous",
                    user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null,
                    user.getUid());
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap fullSize = bitmapReference.get();
            final Bitmap thumbnail = thumbnailReference.get();
            if (fullSize == null || thumbnail == null) {
                return null;
            }
            FirebaseStorage storageRef = FirebaseStorage.getInstance();
            StorageReference photoRef = storageRef.getReferenceFromUrl("gs://" + getString(R.string.google_storage_bucket));
            mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


            Long timestamp = System.currentTimeMillis();
            final StorageReference fullSizeRef = photoRef.child(mUserId).child("full").child(timestamp.toString()).child(fileName + ".jpg");
            final StorageReference thumbnailRef = photoRef.child(mUserId).child("thumb").child(timestamp.toString()).child(fileName + ".jpg");
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
                                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("posts");
                                    final String newPostKey = postsRef.push().getKey();
                                    final Uri thumbnailUrl = taskSnapshot.getDownloadUrl();
                                    Author author = getAuthor();
                                    if (author == null) {
                                        mCallbacks.onPostUploaded(mApplicationContext.getString(R.string.error_user_not_signed_in));
                                        return;
                                    }
                                    Post newPost = new Post(author, fullSizeUrl.toString(), fullSizeRef.toString(), thumbnailUrl.toString(), thumbnailRef.toString(), postText, ServerValue.TIMESTAMP, 0);
                                    Map<String, Object> updatedUserData = new HashMap<>();
                                    updatedUserData.put("people/" + author.getUid() + "/posts/" + newPostKey, true);
                                    updatedUserData.put("posts/" + newPostKey, new ObjectMapper().convertValue(newPost, Map.class));
                                    ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                                            if (firebaseError == null) {
                                                mCallbacks.onPostUploaded(null);
                                            } else {
                                                Log.e(TAG, "Unable to create new post: " + firebaseError.getMessage());
                                                mCallbacks.onPostUploaded(mApplicationContext.getString(R.string.error_upload_task_create));
                                            }
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mCallbacks.onPostUploaded(mApplicationContext.getString(R.string.error_upload_task_create));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mCallbacks.onPostUploaded(mApplicationContext.getString(
                            R.string.error_upload_task_create));
                }
            });
            // TODO: Refactor these insanely nested callbacks.
            return null;
        }
    }
}
