package com.example.cineapp.Activities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntrySaveList;
import com.example.cineapp.Activities.Helpers.FeedEntryWatchlist;
import com.example.cineapp.Activities.Models.SaveList;
import com.example.cineapp.Activities.Models.WatchList;

public class SaveListDAO {

    private final SaveList saveList;
    private FeedEntrySaveList.DBHelpers db;
    private static final String TAG = "SaveListLog";

    public SaveListDAO(Context ctx,SaveList saveList) {
        this.saveList = saveList;
        this.db = new FeedEntrySaveList.DBHelpers(ctx);
    }
    public boolean insertNewSaveList() {
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(FeedEntrySaveList.COLUMN_NAME_WATCHLIST_ID, this.saveList.getWatchList_id());
            values.put(FeedEntrySaveList.COLUMN_NAME_FILM_ID, this.saveList.getFilm_id());
            values.put(FeedEntrySaveList.COLUMN_NAME_USER_ID, this.saveList.getUser_id());

            long resultado = dbLite.insert(FeedEntrySaveList.TABLE_NAME, null, values);
            Log.i("SaveList", String.valueOf(values));

            return resultado != -1;
        } catch (Exception e) {

            Log.e("SQLite", "Erro ao inserir na tabela SaveList: " + e.getMessage());
            return false;
        }
    }
}
