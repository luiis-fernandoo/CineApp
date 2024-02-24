package com.example.cineapp.Activities.DAO;

import android.content.Context;

import com.example.cineapp.Activities.Helpers.FeedEntry;
import com.example.cineapp.Activities.Helpers.FeedEntryCast;
import com.example.cineapp.Activities.Models.Cast;

public class CastDao {

    private final Cast cast;
    private FeedEntry.DBHelpers db;
    private static final String TAG = "CastLog";

    public CastDao(Context ctx, Cast cast) {
        this.cast = cast;
        this.db = new FeedEntry.DBHelpers(ctx);
    }

}
