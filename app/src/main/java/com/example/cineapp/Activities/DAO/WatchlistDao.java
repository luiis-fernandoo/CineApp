package com.example.cineapp.Activities.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntryWatchlist;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WatchlistDao {
    private final WatchList watchlist;
    private Connection connection;
    private FeedEntryWatchlist.DBHelpers db;
    private static final String TAG = "WatchlistLog";
    public WatchlistDao(Context ctx, WatchList watchlist) {
        this.watchlist = watchlist;
        this.db = new FeedEntryWatchlist.DBHelpers(ctx);
    }
    public WatchlistDao(Context ctx, WatchList watchlist, Connection connection) {
        this.watchlist = watchlist;
        this.db = new FeedEntryWatchlist.DBHelpers(ctx);
        this.connection = connection;
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

    @SuppressLint("Range")
    public List<WatchList> getAllWatchList(User userPass) throws SQLException {
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<WatchList> watchlists = new ArrayList<>();
        String sql = "Select * From watchlist Where user_id = ? ;";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userPass.getId())});

        while (cursor.moveToNext()) {
            WatchList watchlist = new WatchList();
            watchlist.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            watchlist.setName(cursor.getString(cursor.getColumnIndex("name")));
            watchlists.add(watchlist);
        }
        cursor.close();
        db.close();

        return watchlists;
    }

}
