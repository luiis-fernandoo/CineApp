package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import androidx.annotation.Nullable;

import com.example.cineapp.Activities.Models.User;

public class FeedEntryWatchlist implements BaseColumns {
        public static final String TABLE_NAME = "watchlist";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_FILM_ID = "film_id";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + com.example.cineapp.Activities.Helpers.FeedEntryFilm.TABLE_NAME + " (" +
                        FeedEntryWatchlist._ID + " INTEGER PRIMARY KEY, " +
                        FeedEntryWatchlist.COLUMN_NAME_NAME + " TEXT," +
                        FeedEntryWatchlist.COLUMN_NAME_USER_ID + " INTEGER REFERENCES " +
                        FeedEntryUser.TABLE_NAME + "(" + FeedEntryUser._ID + ")," +
                        FeedEntryWatchlist.COLUMN_NAME_FILM_ID + " INTEGER REFERENCES " +
                        FeedEntryFilm.TABLE_NAME + "(" + FeedEntryFilm._ID + "))";

    public static class DBHelpers extends SQLiteOpenHelper {


            public DBHelpers(@Nullable Context context) {
                super(context, "CineApp.db", null, 1);
            }


            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_ENTRIES);
            }
            @Override

            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }

        }
}
