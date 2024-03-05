package com.example.cineapp.Activities.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntry;
import com.example.cineapp.Activities.Helpers.FeedEntryWatchlist;
import com.example.cineapp.Activities.Models.SaveList;
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
    private FeedEntryWatchlist feedEntryWatchlist;
    private FeedEntry.DBHelpers db;
    private static final String TAG = "WatchlistLog";
    public WatchlistDao(Context ctx, WatchList watchlist) {
        this.watchlist = watchlist;
        this.db = new FeedEntry.DBHelpers(ctx);
    }
    public WatchlistDao(Context ctx, WatchList watchlist, Connection connection) {
        this.watchlist = watchlist;
        this.db = new FeedEntry.DBHelpers(ctx);
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

    @SuppressLint("Range")
    public WatchList getWatchlist(String name){
        SQLiteDatabase db = this.db.getReadableDatabase();

        String sql = "Select * From watchlist Where name = '"+ name +"';";
        WatchList watchlist = new WatchList();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            watchlist.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            watchlist.setName(cursor.getString(cursor.getColumnIndex("name")));
            watchlist.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
        }
        cursor.close();
        db.close();

        return watchlist;

    }

    @SuppressLint("Range")
    public WatchList getWatchlistID(String watchlist_id){
        SQLiteDatabase db = this.db.getReadableDatabase();

        String sql = "Select * From watchlist Where _id = '"+ watchlist_id +"';";
        WatchList watchlist = new WatchList();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            watchlist.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            watchlist.setName(cursor.getString(cursor.getColumnIndex("name")));
            watchlist.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
        }
        cursor.close();
        db.close();

        return watchlist;
    }

    public boolean updateWatchlist(int watchlistId, String name){
        SQLiteDatabase db = this.db.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("name", name);
            String whereClause = "_id = ?";
            String[] whereArgs = {String.valueOf(watchlistId)};

            long resultado = db.update("watchlist", values, whereClause, whereArgs);
            db.close();

            return resultado != -1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteWatchList(int watchlistId, Context context){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            ContentValues values = new ContentValues();

            long resultado = dbLite.delete("watchlist", "_id = ?", new String[]{String.valueOf(watchlistId)});
            db.close();
            SaveList saveList = new SaveList();
            SaveListDAO saveListDAO = new SaveListDAO(context, saveList);
            saveListDAO.deleteSaveList(watchlistId);
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Test", "Erro ao deletar na tabela watchlist: " + e.getMessage());
            return false;
        }
    }

}
