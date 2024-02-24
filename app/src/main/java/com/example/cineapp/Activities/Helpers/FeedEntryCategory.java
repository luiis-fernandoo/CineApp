package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedEntryCategory implements BaseColumns {

        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_NAME = "name";

}
