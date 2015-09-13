package com.bellantoni.chetta.lieme.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alessandro on 6/2/15.
 */
public class FeedReaderDbHelperMessages extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Messages.db";

    public FeedReaderDbHelperMessages(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContractMessages.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
