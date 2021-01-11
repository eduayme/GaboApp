package com.example.eduardaymerich_app_books;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gaboapp.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists `users` " +
                    "( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `username` TEXT," +
                    " `password` TEXT, `email` TEXT )";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void insertUser(ContentValues contentValues) {
        getWritableDatabase().insert("user", "", contentValues );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        getWritableDatabase().execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
