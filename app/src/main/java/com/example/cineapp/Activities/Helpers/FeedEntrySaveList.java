package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedEntrySaveList implements BaseColumns {
        public String TABLE_NAME = "saveList";
        public String COLUMN_NAME_FILM_ID = "film_id";
        public String COLUMN_NAME_USER_ID = "user_id";
        public String COLUMN_NAME_WATCHLIST_ID = "watchList_id";

}
