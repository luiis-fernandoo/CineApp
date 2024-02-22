package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedEntryCategory implements BaseColumns {

        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_NAME = "name";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntryCategory.TABLE_NAME + " (" +
                        FeedEntryCategory._ID + " INTEGER PRIMARY KEY, " +
                        FeedEntryCategory.COLUMN_NAME_NAME + " TEXT )";
        private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntryCategory.TABLE_NAME;
        public static class DBHelpers extends SQLiteOpenHelper {
            public static final int DATABASE_VERSION = 1;
            public static final String DATABASE_NAME = "cineApp.db";
            public DBHelpers(@Nullable Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_ENTRIES);
                db.execSQL(SQL_DELETE_ENTRIES);
            }

            @Override
            public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL(SQL_DELETE_ENTRIES);
                onCreate(db);
            }
            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onUpgrade(db, oldVersion, newVersion);
            }


        }
}
