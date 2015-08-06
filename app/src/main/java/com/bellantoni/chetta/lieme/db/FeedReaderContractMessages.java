package com.bellantoni.chetta.lieme.db;

import android.provider.BaseColumns;

/**
 * Created by alessandro on 6/2/15.
 */
public final class FeedReaderContractMessages {

    private static final String TEXT_TYPE = " TEXT";
    private static final String TIMESTAMP = " TIMESTAMP";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_SENDER_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_RECEIVER_ID + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_MESSAGE_READ + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_ANSWER + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TIMESTAMP + TIMESTAMP + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContractMessages() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "messages";
        public static final String COLUMN_NAME_SENDER_ID = "sender_id";
        public static final String COLUMN_NAME_RECEIVER_ID = "receiver_id";
        public static final String COLUMN_NAME_MESSAGE_READ = "message_read";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_ANSWER = "answer";
    }

}
