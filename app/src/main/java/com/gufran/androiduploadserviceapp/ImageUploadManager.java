package com.gufran.androiduploadserviceapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gufran.androiduploadserviceapp.persistence.ImageUploadDBHelper;
import com.gufran.androiduploadserviceapp.uploadservice.MultipartUploadRequest;
import com.gufran.androiduploadserviceapp.uploadservice.UploadNotificationConfig;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * <h1>ImageUploadManager</h1>
 * Manages all the behaviour needs to s
 *
 * @author Gufran Khurshid
 * @version 1.0
 * @since 2016-11-26
 */
public class ImageUploadManager implements ImageUploadWorker {

    Context context;
    private ImageUploadDBHelper imageUploadDBHelper;
    private boolean AUTO_DELETE_FILES_AFTER_SUCCESSFULL_UPLOAD = false;
    private int MAX_RETRIES = 3;


    public ImageUploadManager(Context context) {
        this.context = context;
        imageUploadDBHelper = new ImageUploadDBHelper(context);
    }

    @Override
    public void enqueueTask(ImageUploadTask imageUploadTask) throws FileNotFoundException {
        try {
            MultipartUploadRequest req = new MultipartUploadRequest(context, AndroidUploadServiceApp.SERVER_URL)
                    .addFileToUpload(imageUploadTask.getFile().getPath(), "image.jpeg")
                    .setNotificationConfig(getNotificationConfig(imageUploadTask.getFile().getName()))
                    .setAutoDeleteFilesAfterSuccessfulUpload(AUTO_DELETE_FILES_AFTER_SUCCESSFULL_UPLOAD)
                    .setUsesFixedLengthStreamingMode(true)
                    .setMaxRetries(MAX_RETRIES);
            String uploadID = req.startUpload();
            imageUploadDBHelper.insertImageUpload(uploadID, imageUploadTask.getFile().getAbsolutePath(), ImageUploadTask.UploadStatus.IN_PROGRESS, ImageUploadTask.FileStatus.EXISTS, "");
        } catch (FileNotFoundException exc) {
            throw exc;
        } catch (IllegalArgumentException exc) {
            Log.e(AndroidUploadServiceApp.TAG, exc.getMessage());
        } catch (MalformedURLException exc) {
            Log.e(AndroidUploadServiceApp.TAG, exc.getMessage());
        }

    }

    @Override
    public void forceSync() {

    }

    public List<ImageUploadTask> getAllTask() {
        List<ImageUploadTask> taskList = imageUploadDBHelper.getAllImageUpload();
        return taskList;
    }

    public List<ImageUploadTask> getFailedTask() {
        List<ImageUploadTask> taskList = imageUploadDBHelper.getImageUpload(ImageUploadTask.UploadStatus.ERROR, ImageUploadTask.UploadStatus.FAILED, ImageUploadTask.UploadStatus.CANCELLED, ImageUploadTask.UploadStatus.IDLE);
        return taskList;
    }
    // get FAILED ImageUploadTask from DB
    // delete  FILEUpload Objects from AndroidUploadService
    // delete ImageUploadTask from DB
    // add new ImageUploadTask in DB
    // add new  FILEUpload Object in AndroidUploadService


    private UploadNotificationConfig getNotificationConfig(String filename) {
        return new UploadNotificationConfig()
                .setIcon(R.drawable.ic_upload)
                .setCompletedIcon(R.drawable.ic_upload_success)
                .setErrorIcon(R.drawable.ic_upload_error)
                .setTitle(filename)
                .setInProgressMessage("Uploading")
                .setCompletedMessage("Upload SUCCESS")
                .setErrorMessage("Upload FAILED")
                .setAutoClearOnSuccess(false)
                //.setClickIntent(new Intent(this, MainActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

}
