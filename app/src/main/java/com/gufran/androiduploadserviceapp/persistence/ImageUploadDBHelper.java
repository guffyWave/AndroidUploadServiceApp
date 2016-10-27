package com.gufran.androiduploadserviceapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gufran.androiduploadserviceapp.ImageUploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gufran on 10/26/16.
 */

public class ImageUploadDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ImageUploadDB.db";
    public static final String IMAGE_UPLOAD_TABLE_NAME = "ImageUploadTable";
    public static final String IMAGE_UPLOAD_COLUMN_ID = "id";
    public static final String IMAGE_UPLOAD_COLUMN_UPLOAD_ID = "upload_id";
    public static final String IMAGE_UPLOAD_COLUMN_FILE_PATH = "filepath";
    public static final String IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS = "upload_status";
    public static final String IMAGE_UPLOAD_COLUMN_FILE_STATUS = "file_status";
    public static final String IMAGE_UPLOAD_COLUMN_SERVER_URL = "server_url";

    public ImageUploadDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGE_UPLOAD_TABLE = "CREATE TABLE " + IMAGE_UPLOAD_TABLE_NAME + "("
                + IMAGE_UPLOAD_COLUMN_ID + " INTEGER PRIMARY KEY," + IMAGE_UPLOAD_COLUMN_UPLOAD_ID + " TEXT,"
                + IMAGE_UPLOAD_COLUMN_FILE_PATH + " TEXT," + IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS + " TEXT," +
                IMAGE_UPLOAD_COLUMN_FILE_STATUS + " TEXT," + IMAGE_UPLOAD_COLUMN_SERVER_URL + " TEXT" + ")";
        db.execSQL(CREATE_IMAGE_UPLOAD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_UPLOAD_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertImageUpload(String uploadId, String filepath, ImageUploadTask.UploadStatus uploadStatus, ImageUploadTask.FileStatus fileStatus, String serverURL) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_ID, uploadId);
            contentValues.put(IMAGE_UPLOAD_COLUMN_FILE_PATH, filepath);
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS, uploadStatus.toString());
            contentValues.put(IMAGE_UPLOAD_COLUMN_FILE_STATUS, fileStatus.toString());
            contentValues.put(IMAGE_UPLOAD_COLUMN_SERVER_URL, serverURL);
            db.insert(IMAGE_UPLOAD_TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public List<ImageUploadTask> getAllImageUpload() {
        ArrayList<ImageUploadTask> taskList = new ArrayList<ImageUploadTask>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + IMAGE_UPLOAD_TABLE_NAME, null);
        prepareTaskList(taskList, cursor);
        return taskList;
    }


    public List<ImageUploadTask> getImageUpload(int id) {
        ArrayList<ImageUploadTask> taskList = new ArrayList<ImageUploadTask>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + IMAGE_UPLOAD_TABLE_NAME + " where " + IMAGE_UPLOAD_COLUMN_ID + "=" + id + "", null);
        prepareTaskList(taskList, cursor);
        return taskList;
    }

    public List<ImageUploadTask> getImageUpload(ImageUploadTask.UploadStatus... uploadStatusArr) {
        ArrayList<ImageUploadTask> taskList = new ArrayList<ImageUploadTask>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + IMAGE_UPLOAD_TABLE_NAME + " where ";

        for (int i = 0; i < uploadStatusArr.length; i++) {
            ImageUploadTask.UploadStatus uploadStatus = uploadStatusArr[i];
            query = query + IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS + "='" + uploadStatus.toString() + "'";
            if (i != uploadStatusArr.length - 1)
                query = query + " OR ";
        }

        Cursor cursor = db.rawQuery(query, null);

        prepareTaskList(taskList, cursor);
        return taskList;
    }

    public Cursor getImageUpload(String uploadId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + IMAGE_UPLOAD_TABLE_NAME + " where " + IMAGE_UPLOAD_COLUMN_UPLOAD_ID + "=?;", new String[]{uploadId});
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, IMAGE_UPLOAD_TABLE_NAME);
        return numRows;
    }

    public boolean updateImageUpload(Integer id, String uploadId, String filepath, ImageUploadTask.UploadStatus uploadStatus, ImageUploadTask.FileStatus fileStatus, String serverURL) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_ID, uploadId);
            contentValues.put(IMAGE_UPLOAD_COLUMN_FILE_PATH, filepath);
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS, uploadStatus.toString());
            contentValues.put(IMAGE_UPLOAD_COLUMN_FILE_STATUS, fileStatus.toString());
            contentValues.put(IMAGE_UPLOAD_COLUMN_SERVER_URL, serverURL);
            db.update(IMAGE_UPLOAD_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean updateImageUploadStatus(String uploadId, ImageUploadTask.UploadStatus uploadStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS, uploadStatus.toString());
            db.update(IMAGE_UPLOAD_TABLE_NAME, contentValues, IMAGE_UPLOAD_COLUMN_UPLOAD_ID + " = ? ", new String[]{uploadId});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean updateImageUploadServerURL(String uploadId, String serverULR) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_UPLOAD_COLUMN_SERVER_URL, serverULR);
            db.update(IMAGE_UPLOAD_TABLE_NAME, contentValues, IMAGE_UPLOAD_COLUMN_UPLOAD_ID + " = ? ", new String[]{uploadId});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public Integer deleteImageUpload(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(IMAGE_UPLOAD_TABLE_NAME,
                    "id = ? ",
                    new String[]{Integer.toString(id)});
            db.setTransactionSuccessful();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return 0;
    }


    public Integer deleteImageUpload(String uploadId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            int result = db.delete(IMAGE_UPLOAD_TABLE_NAME,
                    IMAGE_UPLOAD_COLUMN_UPLOAD_ID + " = ? ",
                    new String[]{uploadId});
            db.setTransactionSuccessful();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return 0;
    }

    private void prepareTaskList(ArrayList<ImageUploadTask> taskList, Cursor cursor) {
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            ImageUploadTask imageUploadTask = new ImageUploadTask();
            imageUploadTask.setFile(new File(cursor.getString(cursor.getColumnIndex(IMAGE_UPLOAD_COLUMN_FILE_PATH))));
            imageUploadTask.setUploadID(cursor.getString(cursor.getColumnIndex(IMAGE_UPLOAD_COLUMN_UPLOAD_ID)));
            imageUploadTask.setUploadStatus(convertUploadStatus(cursor.getString(cursor.getColumnIndex(IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS))));
            imageUploadTask.setUploadServerURL(cursor.getString(cursor.getColumnIndex(IMAGE_UPLOAD_COLUMN_SERVER_URL)));
            imageUploadTask.setFileStatus(convertFileStatus(cursor.getString(cursor.getColumnIndex(IMAGE_UPLOAD_COLUMN_FILE_STATUS))));
            taskList.add(imageUploadTask);
            cursor.moveToNext();
        }
    }

    private ImageUploadTask.UploadStatus convertUploadStatus(String uploadStatus) {
        if (uploadStatus.equals(ImageUploadTask.UploadStatus.IDLE.toString())) {
            return ImageUploadTask.UploadStatus.IDLE;
        } else if (uploadStatus.equals(ImageUploadTask.UploadStatus.CANCELLED.toString())) {
            return ImageUploadTask.UploadStatus.CANCELLED;
        } else if (uploadStatus.equals(ImageUploadTask.UploadStatus.ERROR.toString())) {
            return ImageUploadTask.UploadStatus.ERROR;
        } else if (uploadStatus.equals(ImageUploadTask.UploadStatus.IN_PROGRESS.toString())) {
            return ImageUploadTask.UploadStatus.IN_PROGRESS;
        } else if (uploadStatus.equals(ImageUploadTask.UploadStatus.SUCCESS.toString())) {
            return ImageUploadTask.UploadStatus.SUCCESS;
        } else {
            return ImageUploadTask.UploadStatus.FAILED;
        }
    }

    private ImageUploadTask.FileStatus convertFileStatus(String fileStatus) {
        if (fileStatus.equals(ImageUploadTask.FileStatus.EXISTS.toString())) {
            return ImageUploadTask.FileStatus.EXISTS;
        } else {
            return ImageUploadTask.FileStatus.DELETED;
        }
    }


}