package com.example.alexandr.androidclient;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.*;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

public class DB extends SQLiteOpenHelper {

    public DB(Context context, CursorFactory cursorFactory, int version) {
        super(context, "App_BD", cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE session (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "token VARCHAR(50) UNIQUE," +
                "create_ts INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
