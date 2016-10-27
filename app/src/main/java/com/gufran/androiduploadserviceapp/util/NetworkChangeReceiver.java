package com.gufran.androiduploadserviceapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.gufran.androiduploadserviceapp.ImageUploadManager;

/**
 * Created by gufran on 10/26/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    ImageUploadManager imageUploadManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        imageUploadManager = new ImageUploadManager(context);

        /// check for if mobile data is selected or not

        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                mobile != null && mobile.isConnectedOrConnecting();
        if (isConnected) {
            Log.d("Network Available ", "YES");

            imageUploadManager.forceSync();

        } else {
            Log.d("Network Available ", "NO");
        }
    }
}