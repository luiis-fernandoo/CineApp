package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class FeedEntryCategory implements BaseColumns {

        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_NAME = "name";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntryCategory.TABLE_NAME + " (" +
                        FeedEntryCategory._ID + " INTEGER PRIMARY KEY, " +
                        FeedEntryCategory.COLUMN_NAME_NAME + " TEXT )";

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
