package com.gufran.androiduploadserviceapp;

import java.io.FileNotFoundException;

/**
 * Created by gufran on 10/27/16.
 */

public interface ImageUploadWorker {

    // enqueue task
    public void enqueueTask(ImageUploadTask imageUploadTask) throws FileNotFoundException;

    public void forceSync();

}
