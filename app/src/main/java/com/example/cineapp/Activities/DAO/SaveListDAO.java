package com.example.cineapp.Activities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntry;
import com.example.cineapp.Activities.Helpers.FeedEntrySaveList;
import com.example.cineapp.Activities.Helpers.FeedEntryWatchlist;
import com.example.cineapp.Activities.Models.SaveList;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;

public class SaveListDAO {

    private final SaveList saveList;
    private FeedEntry.DBHelpers db;
    private FeedEntrySaveList feedEntrySaveList;
    private static final String TAG = "SaveListLog";

    public SaveListDAO(Context ctx,SaveList saveList) {
        this.saveList = saveList;
        this.db = new FeedEntry.DBHelpers(ctx);
    }
    public boolean insertNewSaveList(String filmId, WatchList ObjWatchList) {
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("watchList_id", ObjWatchList.getId());
            values.put("film_id", filmId);
            values.put("user_id", ObjWatchList.getUser_id());

            long resultado = dbLite.insert("saveList", null, values);

            return resultado != -1;
        } catch (Exception e) {

            Log.e("SQLite", "Erro ao inserir na tabela SaveList: " + e.getMessage());
            return false;
        }
    }
}
