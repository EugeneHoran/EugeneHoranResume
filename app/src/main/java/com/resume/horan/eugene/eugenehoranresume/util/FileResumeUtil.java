package com.resume.horan.eugene.eugenehoranresume.util;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.resume.horan.eugene.eugenehoranresume.BuildConfig;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class FileResumeUtil {

    private Context context;
    private static final String[] writePermission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int RC_WRITE_PERMISSIONS = 104;

    public FileResumeUtil(Context context) {
        this.context = context;
    }


    public boolean doesFileExist() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "EugeneFolder");
        return file.exists() && new File(file, "Resume.pdf").exists();
    }


    public void saveFile() {
        if (!EasyPermissions.hasPermissions(context, writePermission)) {
            EasyPermissions.requestPermissions(context, "Permission is required to save file", RC_WRITE_PERMISSIONS, writePermission);
            return;
        }
        File myDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "EugeneFolder");
        File parent = myDirectory.getParentFile();
        final File localFile = new File(myDirectory, "Resume.pdf");
        if (localFile.exists()) {
            openFile();
            return;
        }
        if (!parent.exists()) {
            boolean wasSuccessful = parent.mkdirs();
            if (wasSuccessful) {
                saveFile();
            }
            return;
        }
        if (!myDirectory.exists()) {
            boolean wasSuccessful = myDirectory.mkdirs();
            if (wasSuccessful) {
                saveFile();
            }
            return;
        }
        StorageReference islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(BuildConfig.RESUME_DOWNLOAD_LINK);
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if (listener != null) {
                    listener.onFileSaved();
                }
                openFile();
            }
        });
    }

    private void openFile() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/" + "EugeneFolder/Resume.pdf");
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Open File");
        try {

            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Listener listener;

    public interface Listener {
        void onFileSaved();
    }
}
