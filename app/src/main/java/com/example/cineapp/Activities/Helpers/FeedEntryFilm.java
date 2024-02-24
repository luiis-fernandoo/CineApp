package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedEntryFilm implements BaseColumns {
    public String TABLE_NAME = "film";
    public String COLUMN_NAME_TITLE = "title";
    public String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
    public Boolean COLUMN_NAME_ADULT = Boolean.valueOf("adult");
    public String COLUMN_NAME_ORIGINAL_LANGUAGE = "original_language";
    public String COLUMN_NAME_ORIGINAL_TITLE = "original_title";
    public String COLUMN_NAME_OVERVIEW = "overview";
    public String COLUMN_NAME_POSTER_PATH = "poster_path";
    public String COLUMN_NAME_RELEASE_DATE = "release_date";
    public String COLUMN_NAME_VOTE_AVERAGE = "vote_average";

}
