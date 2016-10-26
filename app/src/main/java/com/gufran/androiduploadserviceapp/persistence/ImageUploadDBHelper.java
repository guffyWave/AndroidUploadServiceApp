package com.gufran.androiduploadserviceapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public boolean insertImageUpload(String uploadId, String filepath, String uploadStatus, String fileStatus, String serverURL) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_ID, uploadId);
            contentValues.put(IMAGE_UPLOAD_COLUMN_FILE_PATH, filepath);
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS, uploadStatus);
            contentValues.put(IMAGE_UPLOAD_COLUMN_FILE_STATUS, fileStatus);
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

    public Cursor getImageUpload(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + IMAGE_UPLOAD_TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    public Cursor getImageUpload(String uploadId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + IMAGE_UPLOAD_TABLE_NAME + " where " + IMAGE_UPLOAD_COLUMN_UPLOAD_ID + "=" + uploadId + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, IMAGE_UPLOAD_TABLE_NAME);
        return numRows;
    }

    public boolean updateImageUpload(Integer id, String uploadId, String filepath, String uploadStatus, String fileStatus, String serverURL) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_ID, uploadId);
            contentValues.put(IMAGE_UPLOAD_COLUMN_FILE_PATH, filepath);
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS, uploadStatus);
            contentValues.put(IMAGE_UPLOAD_COLUMN_FILE_STATUS, fileStatus);
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

    public boolean updateImageUploadStatus(String uploadId, String uploadStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_UPLOAD_COLUMN_UPLOAD_STATUS, uploadStatus);
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

    public Cursor getAllImageUpload() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + IMAGE_UPLOAD_TABLE_NAME, null);
        return res;
    }


}