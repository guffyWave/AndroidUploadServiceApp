package com.gufran.androiduploadserviceapp.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gufran.androiduploadserviceapp.persistence.ImageUploadDBHelper;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import org.json.JSONObject;

/**
 * Created by gufran on 10/26/16.
 */

public class MyReciever extends UploadServiceBroadcastReceiver {


    private ImageUploadDBHelper imageUploadDBHelper;

    Context context;
    String TAG = "GUFRAN";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        imageUploadDBHelper = new ImageUploadDBHelper(context);
        super.onReceive(context, intent);
    }

    @Override
    public void onProgress(UploadInfo uploadInfo) {
        // your code here
        //  Log.d(TAG, "onProgress " + uploadInfo.getUploadId() + "  " + uploadInfo.getProgressPercent() + " %");
    }

    @Override
    public void onCancelled(UploadInfo uploadInfo) {
        // your code here
        Log.d(TAG, uploadInfo.getUploadId() + "   onCancelled");

        imageUploadDBHelper.updateImageUploadStatus(uploadInfo.getUploadId(), "CANCELLED");
    }

    @Override
    public void onError(UploadInfo uploadInfo, Exception exception) {
        // Context context = getReceiverContext();
        // your code here
        Log.d(TAG, uploadInfo.getUploadId() + "   onError");

        imageUploadDBHelper.updateImageUploadStatus(uploadInfo.getUploadId(), "ERROR");
    }

    @Override
    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {

        // your code here
        Log.d(TAG, "File Uploaded" + uploadInfo.getUploadId());
        // Toast.makeText(context, "File Uploaded " + uploadInfo.getUploadId(), Toast.LENGTH_LONG).show();

        imageUploadDBHelper.updateImageUploadStatus(uploadInfo.getUploadId(), "UPLOADED");


        try {
            String response = new String(serverResponse.getBody());

            // Log.d(TAG, "Server Response " + response);

            JSONObject jsonObject = new JSONObject(response);
            String fileURL = jsonObject.getString("file_url");

            imageUploadDBHelper.updateImageUploadServerURL(uploadInfo.getUploadId(), fileURL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
