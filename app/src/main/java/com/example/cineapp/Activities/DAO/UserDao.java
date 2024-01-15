package com.example.cineapp.Activities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntryUser;
import com.example.cineapp.Activities.Models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final User user;
    private FeedEntryUser.DBHelpers db;
    private static final String TAG = "UserLog";

    public UserDao(Context ctx, User user) {
        this.user = user;
        this.db = new FeedEntryUser.DBHelpers(ctx);
    }

}
