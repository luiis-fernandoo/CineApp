package com.example.cineapp.Activities.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntry;
import com.example.cineapp.Activities.Helpers.FeedEntryUser;
import com.example.cineapp.Activities.Models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final User user;
    private FeedEntry.DBHelpers db;

    private static final String TAG = "UserLog";

    public UserDao(Context ctx, User user) {
        this.user = user;
        this.db = new FeedEntry.DBHelpers(ctx);
    }


    public boolean insertNewUser(User user) {
        SQLiteDatabase dbLite = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("senha", user.getSenha());
        values.put("nome", user.getNome());
        values.put("cpf", user.getCpf());

        long resultado = dbLite.insert("user", null, values);

        return resultado != -1;
    }

    public boolean UserUpdate(User updatedUser) {
        SQLiteDatabase dbLite = this.db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", updatedUser.getNome());
        values.put("cpf", updatedUser.getCpf());
        values.put("email", updatedUser.getEmail());
        values.put("senha", updatedUser.getSenha());


        int rowsAffected = dbLite.update("user", values, "email = ?", new String[]{updatedUser.getEmail()});

        return rowsAffected > 0;
    }


    public boolean DeleteUser(String email) {
        SQLiteDatabase dbLite = this.db.getWritableDatabase();
        int deletedRows = dbLite.delete("user", "email = ?", new String[]{email});
        return deletedRows > 0;
    }


    public boolean verifyEmailAndPassword() {
        SQLiteDatabase dbLite = this.db.getReadableDatabase();
        String sql = "SELECT * FROM user where email = ? AND senha = ?";
        Cursor cursor = dbLite.rawQuery(sql, new String[]{this.user.getEmail(), this.user.getSenha()});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public String getUserName(String email) {
        SQLiteDatabase dbLite = this.db.getReadableDatabase();
        String nomeUsuario = "";

        String[] columns = {"nome"};
        String selection = "email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = dbLite.query("user", columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("nome");
                if (columnIndex != -1) {
                    nomeUsuario = cursor.getString(columnIndex);
                }
            }
            cursor.close();
        }

        return nomeUsuario;
    }


    @SuppressLint("Range")
    public User getUserNameID(String email) {
        SQLiteDatabase dbLite = this.db.getReadableDatabase();

        String sql = "Select * From user Where email = '"+ email +"';";
        User user = new User();
        Cursor cursor = dbLite.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            user.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        }
        cursor.close();
        db.close();

        return user;
    }

}