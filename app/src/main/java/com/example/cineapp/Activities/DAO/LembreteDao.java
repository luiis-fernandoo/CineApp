package com.example.cineapp.Activities.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntry;
import com.example.cineapp.Activities.Helpers.FeedEntryLembrete;
import com.example.cineapp.Activities.Models.Reminder;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LembreteDao {
    private final Reminder reminder;
    private final FeedEntry.DBHelpers db;
    private FeedEntryLembrete feedEntryLembrete;
    private static final String TAG = "ReminderLog";

    public LembreteDao(Context ctx, Reminder reminder) {
        this.reminder = reminder;
        this.db = new FeedEntry.DBHelpers(ctx);
    }

    public boolean insertLembrete(String date, int filmId, int userId){
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            ContentValues values = new ContentValues();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            Log.d("", ""+ date);
            values.put("date", date);
            values.put("film_id", filmId);
            values.put("user_id", userId);

            long resultado = dbLite.insert("reminder", null, values);

            return resultado != -1;
        } catch (Exception e) {

            Log.e("SQLite", "Erro ao inserir na tabela SaveList: " + e.getMessage());
            return false;
        }
    }

    @SuppressLint("Range")
    public List<Reminder> getAllLembretes(User userID) {

        SQLiteDatabase db = this.db.getReadableDatabase();
        List<Reminder> reminders = new ArrayList<>();
        String sql = "Select * From reminder Where user_id = ? ;";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userID.getId())});

        while (cursor.moveToNext()) {
            Reminder reminder = new Reminder();
            reminder.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            reminder.setDate(cursor.getString(cursor.getColumnIndex("date")));
            reminder.setUser_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("user_id"))));
            reminder.setFilm_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("film_id"))));
            reminders.add(reminder);
        }
        cursor.close();
        db.close();

        return reminders;

    }
}
