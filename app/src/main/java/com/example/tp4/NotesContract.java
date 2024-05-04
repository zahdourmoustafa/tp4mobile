package com.example.tp4;
import android.provider.BaseColumns;
public final class NotesContract {
    private NotesContract() {} // Private constructor to prevent instantiation

    // Define table and column names
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Define SQL queries for database creation and deletion
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_CONTENT + " TEXT," +
                    COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
