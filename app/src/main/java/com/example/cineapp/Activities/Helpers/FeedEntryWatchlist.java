package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cineapp.Activities.Models.User;

public class FeedEntryWatchlist implements BaseColumns {
        public static final String TABLE_NAME = "watchlist";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntryWatchlist.TABLE_NAME + " (" +
                    FeedEntryWatchlist._ID + " INTEGER PRIMARY KEY, " +
                    FeedEntryWatchlist.COLUMN_NAME_NAME + " TEXT," +
                    FeedEntryWatchlist.COLUMN_NAME_USER_ID + " INTEGER REFERENCES " +
                    FeedEntryUser.TABLE_NAME + "(" + FeedEntryUser._ID + "))";
        private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntryWatchlist.TABLE_NAME;
    public static class DBHelpers extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "cineApp.db";
        public DBHelpers(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
            public void onCreate(@NonNull SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_ENTRIES);
            }
        public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }
}
