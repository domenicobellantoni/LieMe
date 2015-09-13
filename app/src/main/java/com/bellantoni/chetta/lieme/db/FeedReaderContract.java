package com.bellantoni.chetta.lieme.db;

import android.provider.BaseColumns;

/**
 * Created by alessandro on 6/2/15.
 */
public final class FeedReaderContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String TIMESTAMP = " TIMESTAMP";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_FACEBOOK_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TIMESTAMP + TIMESTAMP + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


    public FeedReaderContract() {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_FACEBOOK_ID = "facebook_id";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }

}
