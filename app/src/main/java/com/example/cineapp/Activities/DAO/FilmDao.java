package com.example.cineapp.Activities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntry;
import com.example.cineapp.Activities.Helpers.FeedEntryFilm;
import com.example.cineapp.Activities.Helpers.FeedEntrySaveList;
import com.example.cineapp.Activities.Helpers.FeedEntryUser;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.User;

import org.json.JSONObject;

public class FilmDao {
    private final Film film;
    private final FeedEntry.DBHelpers db;
    private FeedEntryFilm feedEntryFilm;
    private static final String TAG = "FilmLog";

    public FilmDao(Context ctx, Film film) {
        this.film = film;
        this.db = new FeedEntry.DBHelpers(ctx);
    }

    public boolean insertFilm(JSONObject film){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("backdrop_path", film.getString("backdrop_path"));
            values.put("original_language", film.getString("original_language"));
            values.put("original_title", film.getString("original_title"));
            values.put("overview", film.getString("overview"));
            values.put("title", film.getString("title"));
            values.put("poster_path", film.getString("poster_path"));
            values.put("release_date", film.getString("release_date"));
            values.put("vote_average", film.getString("vote_average"));
            values.put(film.getString("adult"), "adult");

            long resultado = dbLite.insert("film", null, values);

            return resultado != -1;
        } catch (Exception e) {
            Log.e("SQLite", "Erro ao inserir na tabela Film: " + e.getMessage());
            return false;
        }
    }
}
