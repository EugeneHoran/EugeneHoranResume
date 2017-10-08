package com.resume.horan.eugene.eugenehoranresume.post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class NewPostUploadFragment extends Fragment {
    private static final String TAG = "NewPostTaskFragment";

    public interface TaskCallbacks {
        void onBitmapResized(Bitmap resizedBitmap, int mMaxDimension);

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

    public void resizeBitmap(Uri uri, int maxDimension) {
        LoadResizedBitmapTask task = new LoadResizedBitmapTask(maxDimension);
        task.execute(uri);
    }

    public void uploadPost(Bitmap bitmap, String inBitmapPath, Bitmap thumbnail, String inThumbnailPath,
                           String inFileName, String inPostText) {
        UploadPostTask uploadTask = new UploadPostTask(bitmap, inBitmapPath, thumbnail, inThumbnailPath, inFileName, inPostText);
        uploadTask.execute();
    }

    class UploadPostTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Bitmap> bitmapReference;
        private WeakReference<Bitmap> thumbnailReference;
        private String postText;
        private String fileName;
        private String bitmapPath;
        private String thumbnailPath;

        private String mUserId;

        public UploadPostTask(Bitmap bitmap, String inBitmapPath, Bitmap thumbnail, String inThumbnailPath,
                              String inFileName, String inPostText) {
            bitmapReference = new WeakReference<Bitmap>(bitmap);
            thumbnailReference = new WeakReference<Bitmap>(thumbnail);
            postText = inPostText;
            fileName = inFileName;
            bitmapPath = inBitmapPath;
            thumbnailPath = inThumbnailPath;
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
                                    Post newPost = new Post(author, fullSizeUrl.toString(), fullSizeRef.toString(), thumbnailUrl.toString(), thumbnailRef.toString(), postText, ServerValue.TIMESTAMP);
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
                            mCallbacks.onPostUploaded(mApplicationContext.getString(
                                    R.string.error_upload_task_create));
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

    class LoadResizedBitmapTask extends AsyncTask<Uri, Void, Bitmap> {
        private int mMaxDimension;

        public LoadResizedBitmapTask(int maxDimension) {
            mMaxDimension = maxDimension;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Uri... params) {
            Uri uri = params[0];
            if (uri != null) {
                // TODO: Currently making these very small to investigate modulefood bug.
                // Implement thumbnail + fullsize later.
                Bitmap bitmap = null;
                try {
                    bitmap = decodeSampledBitmapFromUri(uri, mMaxDimension, mMaxDimension);
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Can't find file to resize: " + e.getMessage());
//                    FirebaseCrash.report(e);
                } catch (IOException e) {
                    Log.e(TAG, "Error occurred during resize: " + e.getMessage());
//                    FirebaseCrash.report(e);
                }
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mCallbacks.onBitmapResized(bitmap, mMaxDimension);
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromUri(Uri fileUri, int reqWidth, int reqHeight) throws IOException {
        InputStream stream = new BufferedInputStream(mApplicationContext.getContentResolver().openInputStream(fileUri));
        stream.mark(stream.available());
        BitmapFactory.Options options = new BitmapFactory.Options();
        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        stream.reset();
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeStream(stream, null, options);
        // Decode bitmap with inSampleSize set
        stream.reset();
        return BitmapFactory.decodeStream(stream, null, options);
    }
}
