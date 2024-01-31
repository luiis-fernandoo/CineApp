package com.example.cineapp.Activities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

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

    public boolean insertNewWatchList() {
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            ContentValues values = new ContentValues();
            Log.i("WatchList", this.watchlist.getName());

            values.put(FeedEntryWatchlist.COLUMN_NAME_NAME, this.watchlist.getName());
            values.put(FeedEntryWatchlist.COLUMN_NAME_USER_ID, this.watchlist.getUser_id());

            long resultado = dbLite.insert(FeedEntryWatchlist.TABLE_NAME, null, values);
            Log.i("WatchList", String.valueOf(values));

            return resultado != -1;
        } catch (Exception e) {

            Log.e("SQLite", "Erro ao inserir na tabela watchlist: " + e.getMessage());
            return false;
        }
    }

}
