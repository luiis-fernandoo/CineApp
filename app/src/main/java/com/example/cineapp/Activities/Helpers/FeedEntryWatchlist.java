package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

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

    public static class DBHelpers extends SQLiteOpenHelper {
        public DBHelpers(@Nullable Context context) {
            super(context, "CineAppV3.db", null, 1);
        }

        @Override
            public void onCreate(SQLiteDatabase db) {
                Log.d("SQLite", "Tabela watchlist criada com sucesso!" + SQL_CREATE_ENTRIES);
                db.execSQL(SQL_CREATE_ENTRIES);
            }
            @Override

            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }

        }
}
