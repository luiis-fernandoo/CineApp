package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class FeedEntry implements BaseColumns {

    static FeedEntryUser feedEntryUser = new FeedEntryUser();
    static FeedEntryWatchlist feedEntryWatchlist = new FeedEntryWatchlist();
    static FeedEntrySaveList feedEntrySaveList = new FeedEntrySaveList();
    static FeedEntryFilm feedEntryFilm = new FeedEntryFilm();
    static FeedEntryLembrete feedEntryLembrete = new FeedEntryLembrete();

    public static class DBHelpers extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "cineAppOficial.db";

        private static final String SQL_CREATE_ENTRIES_USER =
                "CREATE TABLE " + feedEntryUser.TABLE_NAME + " (" +
                        feedEntryUser._ID + " INTEGER PRIMARY KEY, " +
                        feedEntryUser.COLUMN_NAME_NAME + " TEXT, " +
                        feedEntryUser.COLUMN_NAME_EMAIL + " TEXT, " +
                        feedEntryUser.COLUMN_NAME_CPF + " TEXT, " +
                        feedEntryUser.COLUMN_NAME_PASSWORD + " TEXT)";

        private static final String SQL_CREATE_ENTRIES_FILM =
                "CREATE TABLE " + feedEntryFilm.TABLE_NAME + " (" +
                        feedEntryFilm._ID + " INTEGER PRIMARY KEY, " +
                        feedEntryFilm.COLUMN_NAME_FILM_ID + " INTEGER," +
                        feedEntryFilm.COLUMN_NAME_TITLE + " TEXT," +
                        feedEntryFilm.COLUMN_NAME_BACKDROP_PATH + " TEXT, " +
                        feedEntryFilm.COLUMN_NAME_ADULT + " BOOLEAN, " +
                        feedEntryFilm.COLUMN_NAME_ORIGINAL_LANGUAGE + " TEXT, " +
                        feedEntryFilm.COLUMN_NAME_ORIGINAL_TITLE + " TEXT, " +
                        feedEntryFilm.COLUMN_NAME_OVERVIEW + " TEXT, " +
                        feedEntryFilm.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                        feedEntryFilm.COLUMN_NAME_RELEASE_DATE + " INTEGER, " +
                        feedEntryFilm.COLUMN_NAME_VOTE_AVERAGE + " TEXT )";

        private static final String SQL_CREATE_ENTRIES_SAVELIST =
                "CREATE TABLE " + feedEntrySaveList.TABLE_NAME + " (" +
                        feedEntrySaveList._ID + " INTEGER PRIMARY KEY, " +
                        feedEntrySaveList.COLUMN_NAME_FILM_ID + " INTEGER REFERENCES " +
                        feedEntryFilm.TABLE_NAME + "(" + FeedEntryFilm._ID + ")," +
                        feedEntrySaveList.COLUMN_NAME_USER_ID + " INTEGER REFERENCES " +
                        feedEntryUser.TABLE_NAME + "(" + FeedEntryUser._ID + ")," +
                        feedEntrySaveList.COLUMN_NAME_WATCHLIST_ID + " INTEGER REFERENCES " +
                        feedEntryWatchlist.TABLE_NAME + "(" + FeedEntryWatchlist._ID + "))";

        private static final String SQL_CREATE_ENTRIES_WATCHLIST =
                "CREATE TABLE " + feedEntryWatchlist.TABLE_NAME + " (" +
                        feedEntryWatchlist._ID + " INTEGER PRIMARY KEY, " +
                        feedEntryWatchlist.COLUMN_NAME_NAME + " TEXT," +
                        feedEntryWatchlist.COLUMN_NAME_USER_ID + " INTEGER REFERENCES " +
                        feedEntryWatchlist.TABLE_NAME + "(" + feedEntryUser._ID + "))";
        private static final String SQL_CREATE_ENTRIES_LEMBRETE =
                "CREATE TABLE " + feedEntryLembrete.TABLE_NAME + " (" +
                        feedEntryLembrete._ID + " INTEGER PRIMARY KEY, " +
                        feedEntryLembrete.COLUMN_NAME_DATE + " DATE," +
                        feedEntryLembrete.COLUMN_NAME_FILM_ID + " INTEGER REFERENCES " +
                        feedEntryFilm.TABLE_NAME + "(" + feedEntryFilm._ID + ")," +
                        feedEntryLembrete.COLUMN_NAME_USER_ID + " INTEGER REFERENCES " +
                        feedEntryUser.TABLE_NAME + "(" + feedEntryUser._ID + "))";

        public DBHelpers(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES_USER);
            db.execSQL(SQL_CREATE_ENTRIES_FILM);
            db.execSQL(SQL_CREATE_ENTRIES_SAVELIST);
            db.execSQL(SQL_CREATE_ENTRIES_WATCHLIST);
            db.execSQL(SQL_CREATE_ENTRIES_LEMBRETE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Aqui você colocaria código para atualizar as tabelas, se necessário
        }

    }
}
