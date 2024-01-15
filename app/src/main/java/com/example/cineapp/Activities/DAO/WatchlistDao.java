package com.example.cineapp.Activities.DAO;

import android.content.Context;

import com.example.cineapp.Activities.Helpers.FeedEntryUser;
import com.example.cineapp.Activities.Helpers.FeedEntryWatchlist;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;

public class WatchlistDao {
    private final WatchList watchlist;
    private FeedEntryWatchlist.DBHelpers db;
    private static final String TAG = "WatchlistLog";
    public WatchlistDao(Context ctx, WatchList watchlist) {
        this.watchlist = watchlist;
        this.db = new FeedEntryWatchlist.DBHelpers(ctx);
    }

}
