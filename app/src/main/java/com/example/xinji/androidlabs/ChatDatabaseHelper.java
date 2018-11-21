package com.example.xinji.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// Lab5 step 2, 3
public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "chat.db";
    public static final String TABLE_NAME = "CHAT";
    public static final int VERSION_NUM = 8;
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "MESSAGE";
    public static final String ACTIVITY_NAME = "ChatDatabaseHelper";

    public ChatDatabaseHelper(Context ctx){

        super(ctx, DATABASE_NAME, null, VERSION_NUM);

    }

    //Lab5 step 3
    //create table if database doesn't exist yet, db is the database object
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MESSAGE + " TEXT )";
        db.execSQL(createTable) ;

        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    //Lab5 step 4, 8
    //handle upgrading the data, or drop the table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVer+" newVersion=" + newVer);
    }
}
