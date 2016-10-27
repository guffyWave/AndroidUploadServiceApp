package com.gufran.androiduploadserviceapp;

import java.io.File;

/**
 * Created by gufran on 10/27/16.
 */

public class ImageUploadTask {
    private File file;
    private String uploadID;
    private UploadStatus uploadStatus;
    private String uploadServerURL;
    private FileStatus fileStatus;

    public ImageUploadTask() {
    }

    public ImageUploadTask(File file) {
        this.setFile(file);
    }


    public String getUploadID() {
        return uploadID;
    }

    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }

    public UploadStatus getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(UploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadServerURL() {
        return uploadServerURL;
    }

    public void setUploadServerURL(String uploadServerURL) {
        this.uploadServerURL = uploadServerURL;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public enum UploadStatus {
        IDLE, IN_PROGRESS, SUCCESS, ERROR, FAILED, CANCELLED,
    }

    public enum FileStatus {
        EXISTS, DELETED;
    }

}
