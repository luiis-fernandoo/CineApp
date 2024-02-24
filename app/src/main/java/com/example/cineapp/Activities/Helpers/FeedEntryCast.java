package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedEntryCast implements BaseColumns {
    public String TABLE_NAME = "casts";
    public String COLUMN_NAME_PROFILE_PATH = "profile_path";
    public String COLUMN_NAME_NAME = "name";
    public String COLUMN_NAME_CAST_ID = "cast_id";
    public String COLUMN_NAME_CHARACTER = "character";
    public String COLUMN_NAME_CREDIT_ID = "credit_id";

}
