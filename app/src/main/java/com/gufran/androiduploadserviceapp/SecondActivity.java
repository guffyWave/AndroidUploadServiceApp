package com.gufran.androiduploadserviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gufran.androiduploadserviceapp.persistence.ImageUploadDBHelper;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class SecondActivity extends AppCompatActivity {

    private ImageUploadDBHelper imageUploadDBHelper;

    String[] fileNameArray = {"two.jpg", "one.jpg", "three.jpg", "four.jpg"};

    //"five.jpg", "six.jpg"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageUploadDBHelper = new ImageUploadDBHelper(this);
    }

    public void uploadOnClick(View v) {


        for (String f :
                fileNameArray) {
            File outputFile = new File(Environment.getExternalStorageDirectory(), "/GufranPics/" + f);

            try {
                MultipartUploadRequest req = new MultipartUploadRequest(this, "http://mdev.broex.net/media/upload")
                        .addFileToUpload(outputFile.getPath(), "image.jpeg")
                        .setNotificationConfig(getNotificationConfig(outputFile.getName()))
                        .setAutoDeleteFilesAfterSuccessfulUpload(false)
                        .setUsesFixedLengthStreamingMode(true)
                        .setMaxRetries(3);

                String uploadID = req.startUpload();

                //             showToast("Upload ID " + uploadID);

                // statusTextView.setText("Uploading...  ");

                imageUploadDBHelper.insertImageUpload(uploadID, outputFile.getAbsolutePath(), "UPLOADING", "EXISTS", "");

            } catch (FileNotFoundException exc) {
                showToast(exc.getMessage());
            } catch (IllegalArgumentException exc) {
                showToast("Missing some arguments. " + exc.getMessage());
            } catch (MalformedURLException exc) {
                showToast(exc.getMessage());
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

    /* ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TY);

        if (mWifi.isConnected()) {
            // Do whatever
        }
*/

}
