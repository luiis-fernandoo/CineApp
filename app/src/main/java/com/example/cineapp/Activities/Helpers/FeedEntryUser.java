package com.example.cineapp.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedEntryUser implements BaseColumns {

    public String TABLE_NAME = "user";
    public String COLUMN_NAME_NAME = "nome";
    public String COLUMN_NAME_EMAIL = "email";
    public String COLUMN_NAME_CPF = "cpf";
    public String COLUMN_NAME_PASSWORD = "senha";

}
