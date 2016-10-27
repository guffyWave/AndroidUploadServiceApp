package com.gufran.androiduploadserviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gufran.androiduploadserviceapp.persistence.ImageUploadDBHelper;
import com.gufran.androiduploadserviceapp.uploadservice.MultipartUploadRequest;
import com.gufran.androiduploadserviceapp.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    String[] fileNameArray = {"two.jpg", "one.jpg", "three.jpg", "four.jpg"};
    ImageUploadManager imageUploadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        imageUploadManager = new ImageUploadManager(this);
    }

    public void uploadOnClick(View v) {
        for (String f :
                fileNameArray) {
            File outputFile = new File(Environment.getExternalStorageDirectory(), "/GufranPics/" + f);
            try {
                imageUploadManager.enqueueTask(new ImageUploadTask(outputFile));
            } catch (FileNotFoundException fnfe) {
                showToast("File Not Found " + fnfe.getMessage());
            }
        }
    }

    public void showEntries(View v) {
        List<ImageUploadTask> taskList = imageUploadManager.getFailedTask();
        for (ImageUploadTask task :
                taskList) {
            Log.d(AndroidUploadServiceApp.TAG, "" + task.getFile().getPath() + "     " + task.getUploadStatus() + "    " + task.getUploadServerURL());
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
