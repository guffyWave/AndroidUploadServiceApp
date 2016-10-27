package com.gufran.androiduploadserviceapp;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.gufran.androiduploadserviceapp.uploadservice.UploadService;
import com.gufran.androiduploadserviceapp.util.UploadMedium;


/**
 * Created by gufran on 10/25/16.
 */

public class AndroidUploadServiceApp extends Application {

    static final public UploadMedium UPLOAD_MEDIUM = UploadMedium.WIFI;
    public static final String TAG = AndroidUploadServiceApp.class.getName();
    public static final String SERVER_URL = "http://mdev.broex.net/media/upload";

    @Override
    public void onCreate() {
        super.onCreate();
        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        //UploadService.NAMESPACE = "com.gufran.androiduploadserviceapp";

        Stetho.initializeWithDefaults(this);
    }
}
