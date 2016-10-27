package com.gufran.androiduploadserviceapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gufran.androiduploadserviceapp.uploadservice.MultipartUploadRequest;
import com.gufran.androiduploadserviceapp.uploadservice.ServerResponse;
import com.gufran.androiduploadserviceapp.uploadservice.UploadInfo;
import com.gufran.androiduploadserviceapp.uploadservice.UploadNotificationConfig;
import com.gufran.androiduploadserviceapp.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity implements UploadStatusDelegate {

    private static final int TAKE_PICTURE = 1;
    ImageView imageView;
    Uri outputFileUri;
    File outputFile;
    TextView statusTextView;

    String TAG = "GUFRAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
    }

    public void takePictureOnClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFile = new File(Environment.getExternalStorageDirectory(), "external_pic.jpg");
        outputFileUri = Uri.fromFile(outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                outputFileUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        MultipartUploadRequest req = new MultipartUploadRequest(this, "http://mdev.broex.net/media/upload")
                                .addFileToUpload(outputFile.getPath(), "image.jpeg")
                                .setNotificationConfig(getNotificationConfig(outputFile.getName()))
                                .setAutoDeleteFilesAfterSuccessfulUpload(false)
                                .setUsesFixedLengthStreamingMode(true)
                                .setMaxRetries(3);

                        String uploadID = req.setDelegate(this).startUpload();

                        showToast("Upload ID " + uploadID);

                        statusTextView.setText("Uploading...  ");

                    } catch (FileNotFoundException exc) {
                        showToast(exc.getMessage());
                    } catch (IllegalArgumentException exc) {
                        showToast("Missing some arguments. " + exc.getMessage());
                    } catch (MalformedURLException exc) {
                        showToast(exc.getMessage());
                    }

                }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

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
                .setClickIntent(new Intent(this, MainActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

    @Override
    public void onProgress(UploadInfo uploadInfo) {

        statusTextView.setText("Uploaded " + uploadInfo.getProgressPercent() + " % ");

    }

    @Override
    public void onError(UploadInfo uploadInfo, Exception exception) {
        statusTextView.setText("Upload Error ");
    }

    @Override
    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
        String response = new String(serverResponse.getBody());

        Log.d(TAG, "Server Response " + response);

        statusTextView.setText("Upload Complete ");
    }

    @Override
    public void onCancelled(UploadInfo uploadInfo) {
        statusTextView.setText("Upload Cancelled ");
    }
}
