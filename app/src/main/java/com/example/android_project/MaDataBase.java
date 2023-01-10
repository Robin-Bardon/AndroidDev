package com.example.android_project;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class MaDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE_NAME = "mydatabase";
    private static final String PKEY = "pkey";
    private static final String COL1 = "col1";
    private String TAG = "SQLite Database:";

    MaDataBase(Context context) {
        super(context, DATABASE_TABLE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                PKEY + " INTEGER PRIMARY KEY," +
                COL1 + " DRAWABLE);";
        db.execSQL(DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertPicture(Drawable d){
        Log.i(TAG," Insert in database");
        //
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COL1, d.toString());
        db.insertOrThrow(DATABASE_TABLE_NAME,null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    public void insertData(String s) {
        Log.i(TAG," Insert in database");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COL1, s);
        db.insertOrThrow(DATABASE_TABLE_NAME,null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public Cursor querryData(String querry){
        String select = new String(querry);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        return cursor;
    };

    public void readData(){
        Log.i(TAG, "Reading database...");
        String select = new String("SELECT * from " + DATABASE_TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "Number of entries: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Log.i(TAG, "Reading: " + cursor.getString(cursor.getColumnIndex(COL1)));
            } while (cursor.moveToNext());
        }
    };

    public void clearData() {
        String clearDBQuery = "DELETE FROM "+DATABASE_TABLE_NAME;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(clearDBQuery);
        Log.d(TAG, "clearData: Data cleared");
        readData();
    }
}
