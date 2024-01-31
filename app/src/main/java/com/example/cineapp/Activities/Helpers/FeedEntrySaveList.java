package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class FeedEntrySaveList implements BaseColumns {
        public static final String TABLE_NAME = "saveList";
        public static final String COLUMN_NAME_FILM_ID = "film_id";
        public static final String COLUMN_NAME_WATCHLIST_ID = "watchList_id";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntrySaveList.TABLE_NAME + " (" +
                        FeedEntrySaveList._ID + " INTEGER PRIMARY KEY, " +
                        FeedEntrySaveList.COLUMN_NAME_FILM_ID + " INTEGER REFERENCES " +
                        FeedEntryFilm.TABLE_NAME + "(" + FeedEntryFilm._ID + ")," +
                        FeedEntrySaveList.COLUMN_NAME_WATCHLIST_ID + " INTEGER REFERENCES " +
                        FeedEntryWatchlist.TABLE_NAME + "(" + FeedEntryWatchlist._ID + "))";
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
