package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import androidx.annotation.Nullable;

import java.util.Date;

public class FeedEntryUser implements BaseColumns {

        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_BIRTH_DATE = "birth_date";
        public static final String COLUMN_NAME_USERNAME = "username";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + com.example.cineapp.Activities.Helpers.FeedEntryFilm.TABLE_NAME + " (" +
                        FeedEntryUser._ID + " INTEGER PRIMARY KEY, " +
                        FeedEntryUser.COLUMN_NAME_NAME + " TEXT," +
                        FeedEntryUser.COLUMN_NAME_EMAIL + "TEXT, " +
                        FeedEntryUser.COLUMN_NAME_USERNAME + "TEXT, " +
                        FeedEntryUser.COLUMN_NAME_BIRTH_DATE + "INTEGER )";

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
