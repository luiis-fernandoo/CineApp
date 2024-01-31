package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class FeedEntryCast implements BaseColumns {
    public static final String TABLE_NAME = "casts";
    public static final String COLUMN_NAME_PROFILE_PATH = "profile_path";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_CAST_ID = "cast_id";
    public static final String COLUMN_NAME_CHARACTER = "character";
    public static final String COLUMN_NAME_CREDIT_ID = "credit_id";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntryCast.TABLE_NAME + " (" +
                    FeedEntryCast._ID + " INTEGER PRIMARY KEY, " +
                    FeedEntryCast.COLUMN_NAME_PROFILE_PATH + " TEXT, " +
                    FeedEntryCast.COLUMN_NAME_CAST_ID + " TEXT, " +
                    FeedEntryCast.COLUMN_NAME_CHARACTER + " TEXT, " +
                    FeedEntryCast.COLUMN_NAME_CREDIT_ID + " TEXT, " +
                    FeedEntryCast.COLUMN_NAME_NAME + " TEXT)";

    public static class DBHelpers extends SQLiteOpenHelper {


        public DBHelpers(@Nullable Context context) {
            super(context, "CineAppV2.db", null, 2);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    }
}
