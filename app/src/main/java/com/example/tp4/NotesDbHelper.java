package com.example.tp4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDbHelper extends SQLiteOpenHelper {
    // Define database version and name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Notes.db";

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database tables
        db.execSQL(NotesContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL(NotesContract.SQL_DELETE_ENTRIES);
        // Recreate the database
        onCreate(db);
    }
}
