package com.example.cineapp.Activities.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cineapp.Activities.Helpers.FeedEntryCategory;
import com.example.cineapp.Activities.Helpers.FeedEntryUser;
import com.example.cineapp.Activities.Models.Category;
import com.example.cineapp.Activities.Models.User;

public class CategoryDao {
    private final Category category;
    private FeedEntryCategory.DBHelpers db;
    private static final String TAG = "CategoryLog";

    public CategoryDao(Context ctx, Category category) {
        this.category = category;
        this.db = new FeedEntryCategory.DBHelpers(ctx);
    }

    public boolean insertNewCategory() {
        try {
            SQLiteDatabase dbLite = this.db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", this.category.getName());

            long resultado = dbLite.insert("category", null, values);
            Log.i("category", "" + resultado);
            return resultado != -1;
        } catch (Exception e) {
            Log.e("Test", "Erro ao inserir na tabela category: " + e.getMessage());
            return false;
        }
    }

}
