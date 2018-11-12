package com.example.alexandr.androidclient;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.Response;

public class TokenHelper {

    public static String loadToken(Context context) {
        try {
            SQLiteDatabase db = (new DB(context, null,MainActivity.getVersion())).getWritableDatabase();
            Cursor dbCursor = db.query("session", null, null, null, "", "", "");
            if(dbCursor == null) {
                return null;
            }
            dbCursor.moveToFirst();
            if(!dbCursor.isFirst()) {
                return null;
            }
            String token = dbCursor.getString(dbCursor.getColumnIndex("token"));
            int ts = dbCursor.getInt(dbCursor.getColumnIndex("create_ts"));
            //timestamp checking
            db.execSQL(
                    "UPDATE session SET create_ts = ?",
                    new String[]{(new Integer((int)System.currentTimeMillis() / 1000)).toString()});
            return token;
        }
        catch(Exception e) {
            return null;
        }
    }

    public static void saveToken(Context context, String token) {
        DB db = new DB(context, null, MainActivity.getVersion());
        SQLiteDatabase writeDB = db.getWritableDatabase();
        writeDB.execSQL(
                "INSERT INTO session(token, create_ts) VALUES (?, ?)",
                new String[]{token, (new Integer((int)(System.currentTimeMillis() / 1000))).toString()});
    }

    public static void deleteToken(Context context) {
        SQLiteDatabase db = (new DB(context, null, MainActivity.getVersion())).getWritableDatabase();
        db.execSQL("DELETE FROM session");
    }
}
