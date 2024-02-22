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
            values.put("user_id", this.watchlist.getUser_id());
            values.put("name", this.watchlist.getName());

            long resultado = dbLite.insert("watchlist", null, values);
            Log.i("WatchList", "" + resultado);

            return resultado != -1;
        } catch (Exception e) {
            Log.e("Test", "Erro ao inserir na tabela watchlist: " + e.getMessage());
            return false;
        }
    }

}
