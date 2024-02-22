package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import androidx.annotation.Nullable;

public class FeedEntryUser implements BaseColumns {

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NAME_NAME = "nome";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_CPF = "cpf";
    public static final String COLUMN_NAME_PASSWORD = "senha";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntryUser.TABLE_NAME + " (" +
                    FeedEntryUser._ID + " INTEGER PRIMARY KEY, " +
                    FeedEntryUser.COLUMN_NAME_NAME + " TEXT, " +
                    FeedEntryUser.COLUMN_NAME_EMAIL + " TEXT, " +
                    FeedEntryUser.COLUMN_NAME_CPF + " TEXT, " +
                    FeedEntryUser.COLUMN_NAME_PASSWORD + " TEXT)";

    public static class DBHelpers extends SQLiteOpenHelper {

        public DBHelpers(@Nullable Context context) {
            super(context, "CineAppV3.db", null, 1);
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
