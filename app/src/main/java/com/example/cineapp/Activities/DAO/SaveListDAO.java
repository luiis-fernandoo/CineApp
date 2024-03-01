package com.example.cineapp.Activities.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntry;
import com.example.cineapp.Activities.Helpers.FeedEntrySaveList;
import com.example.cineapp.Activities.Helpers.FeedEntryWatchlist;
import com.example.cineapp.Activities.Models.SaveList;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;

import java.util.ArrayList;
import java.util.List;

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

    @SuppressLint("Range")
    public List<SaveList> getSaveListByUserAndWatchlist(int userID, int watchlistID){
        SQLiteDatabase db = this.db.getReadableDatabase();
        List<SaveList> saveLists = new ArrayList<>();
        String sql = "Select * From saveList Where user_id = '"+ userID +"' And watchList_id = '"+ watchlistID +"';";
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            SaveList saveList = new SaveList();
            saveList.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            saveList.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
            saveList.setFilm_id(cursor.getInt(cursor.getColumnIndex("film_id")));
            saveList.setWatchList_id(cursor.getInt(cursor.getColumnIndex("watchList_id")));
            saveLists.add(saveList);
        }
        cursor.close();
        db.close();

        return saveLists;
    }

    public void deleteSaveList(int watchlistId){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            ContentValues values = new ContentValues();

            long resultado = dbLite.delete("saveList", "watchlist_id = ?", new String[]{String.valueOf(watchlistId)});
            db.close();

        } catch (Exception e) {
            Log.e("Test", "Erro ao deletar na tabela saveList: " + e.getMessage());

        }
    }

    public boolean deleteSaveListByIdAndFilm(int id){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();

            long resultado = dbLite.delete("saveList", "_id = ?", new String[]{String.valueOf(id)});
            db.close();
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Test", "Erro ao deletar na tabela saveList: " + e.getMessage());
            return false;
        }
    }
}
