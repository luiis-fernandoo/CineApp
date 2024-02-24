package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cineapp.Activities.Models.User;

public class FeedEntryWatchlist implements BaseColumns {
        public String TABLE_NAME = "watchlist";
        public String COLUMN_NAME_NAME = "name";
        public String COLUMN_NAME_USER_ID = "user_id";

}
