package com.gufran.androiduploadserviceapp.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gufran.androiduploadserviceapp.AndroidUploadServiceApp;
import com.gufran.androiduploadserviceapp.ImageUploadTask;
import com.gufran.androiduploadserviceapp.persistence.ImageUploadDBHelper;
import com.gufran.androiduploadserviceapp.uploadservice.ServerResponse;
import com.gufran.androiduploadserviceapp.uploadservice.UploadInfo;
import com.gufran.androiduploadserviceapp.uploadservice.UploadServiceBroadcastReceiver;

import org.json.JSONObject;

/**
 * Created by gufran on 10/26/16.
 */

public class UploadStatusBroadcastReceiver extends UploadServiceBroadcastReceiver {

    private ImageUploadDBHelper imageUploadDBHelper;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        imageUploadDBHelper = new ImageUploadDBHelper(context);
        super.onReceive(context, intent);
    }

    @Override
    public void onProgress(UploadInfo uploadInfo) {
        //  Log.d(TAG, "onProgress " + uploadInfo.getUploadId() + "  " + uploadInfo.getProgressPercent() + " %");
    }

    @Override
    public void onCancelled(UploadInfo uploadInfo) {
        Log.d(AndroidUploadServiceApp.TAG, uploadInfo.getUploadId() + "   onCancelled");
        imageUploadDBHelper.updateImageUploadStatus(uploadInfo.getUploadId(), ImageUploadTask.UploadStatus.CANCELLED);
    }

    @Override
    public void onError(UploadInfo uploadInfo, Exception exception) {
        Log.d(AndroidUploadServiceApp.TAG, uploadInfo.getUploadId() + "   onError");
        imageUploadDBHelper.updateImageUploadStatus(uploadInfo.getUploadId(), ImageUploadTask.UploadStatus.ERROR);
    }

    @Override
    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
        Log.d(AndroidUploadServiceApp.TAG, "File Uploaded" + uploadInfo.getUploadId());
        imageUploadDBHelper.updateImageUploadStatus(uploadInfo.getUploadId(), ImageUploadTask.UploadStatus.SUCCESS);
        try {
            String response = new String(serverResponse.getBody());
            JSONObject jsonObject = new JSONObject(response);
            String fileURL = jsonObject.getString("file_url");
            imageUploadDBHelper.updateImageUploadServerURL(uploadInfo.getUploadId(), fileURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
