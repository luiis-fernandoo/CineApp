package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedEntrySaveList implements BaseColumns {
        public static final String TABLE_NAME = "saveList";
        public static final String COLUMN_NAME_FILM_ID = "film_id";
    public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_WATCHLIST_ID = "watchList_id";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntrySaveList.TABLE_NAME + " (" +
                        FeedEntrySaveList._ID + " INTEGER PRIMARY KEY, " +
                        FeedEntrySaveList.COLUMN_NAME_FILM_ID + " INTEGER REFERENCES " +
                        FeedEntryFilm.TABLE_NAME + "(" + FeedEntryFilm._ID + ")," +
                        FeedEntrySaveList.COLUMN_NAME_USER_ID + " INTEGER REFERENCES " +
                        FeedEntryFilm.TABLE_NAME + "(" + FeedEntryUser._ID + ")," +
                        FeedEntrySaveList.COLUMN_NAME_WATCHLIST_ID + " INTEGER REFERENCES " +
                        FeedEntryWatchlist.TABLE_NAME + "(" + FeedEntryWatchlist._ID + "))";
        private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntrySaveList.TABLE_NAME;
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
