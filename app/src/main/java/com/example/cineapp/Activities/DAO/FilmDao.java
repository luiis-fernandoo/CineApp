package com.example.cineapp.Activities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntryFilm;
import com.example.cineapp.Activities.Helpers.FeedEntryUser;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.User;

public class FilmDao {
    private final Film film;
    private FeedEntryFilm.DBHelpers db;
    private static final String TAG = "FilmLog";

    public FilmDao(Context ctx, Film film) {
        this.film = film;
        this.db = new FeedEntryFilm.DBHelpers(ctx);
    }

}
