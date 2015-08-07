package com.bellantoni.chetta.lieme.db;

import android.provider.BaseColumns;

/**
 * Created by alessandro on 6/2/15.
 */
public final class FeedReaderContractNotification {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String TIMESTAMP = " TIMESTAMP";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TIMESTAMP + TIMESTAMP + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContractNotification() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }

}
