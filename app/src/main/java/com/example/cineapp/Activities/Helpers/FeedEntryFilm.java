package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public class FeedEntryFilm implements BaseColumns {
    public static final String TABLE_NAME = "film";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
    public static final Boolean COLUMN_NAME_ADULT = Boolean.valueOf("adult");
    public static final String COLUMN_NAME_ORIGINAL_LANGUAGE = "original_language";
    public static final String COLUMN_NAME_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_NAME_OVERVIEW = "overview";
    public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
    public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
    public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntryFilm.TABLE_NAME + " (" +
                    FeedEntryFilm._ID + " INTEGER PRIMARY KEY, " +
                    FeedEntryFilm.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntryFilm.COLUMN_NAME_BACKDROP_PATH + "TEXT, " +
                    FeedEntryFilm.COLUMN_NAME_ADULT + " BOOLEAN, " +
                    FeedEntryFilm.COLUMN_NAME_ORIGINAL_LANGUAGE + " TEXT, " +
                    FeedEntryFilm.COLUMN_NAME_ORIGINAL_TITLE + " TEXT, " +
                    FeedEntryFilm.COLUMN_NAME_OVERVIEW + " TEXT, " +
                    FeedEntryFilm.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                    FeedEntryFilm.COLUMN_NAME_RELEASE_DATE + " INTEGER, " +
                    FeedEntryFilm.COLUMN_NAME_VOTE_AVERAGE + " TEXT )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntryFilm.TABLE_NAME;
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
