package com.example.cineapp.Activities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntryUser;
import com.example.cineapp.Activities.Models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final User user;
    private FeedEntryUser.DBHelpers db;

    private static final String TAG = "UserLog";

    public UserDao(Context ctx, User user) {
        this.user = user;
        this.db = new FeedEntryUser.DBHelpers(ctx);
    }


    public boolean insertNewUser() {
        SQLiteDatabase dbLite = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", this.user.getEmail());
        values.put("senha", this.user.getSenha());
        values.put("nome", this.user.getNome());
        values.put("cpf", this.user.getCpf());

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


    public User getUserNameID(String email) {
        SQLiteDatabase dbLite = this.db.getReadableDatabase();
        User userName = new User(0, "");
        String nomeUsuario = "";
        int userId = 0;

        String[] columns = {"_id", "nome"};
        String selection = "email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = dbLite.query("user", columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int userIdColumnIndex = cursor.getColumnIndex("_id");
                int columnIndex = cursor.getColumnIndex("nome");
                if (userIdColumnIndex != -1 && columnIndex != -1) {
                    userId = cursor.getInt(userIdColumnIndex);
                    nomeUsuario = cursor.getString(columnIndex);
                    userName = new User(userId, nomeUsuario);
                }
            }
            cursor.close();
        }

        return userName;
    }



}
