package com.example.cineapp.Activities.DAO;

import android.content.Context;

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

}
