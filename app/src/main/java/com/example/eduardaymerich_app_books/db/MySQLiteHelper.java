package com.example.eduardaymerich_app_books.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gaboapp.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists users " +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT," +
            " username TEXT, password TEXT )";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void insertUser(ContentValues contentValues) {
        getWritableDatabase().insert("users", "", contentValues );
    }

    public boolean isLoginValid(String username, String password) {
        String sql = "Select count(*) from users where username='" + username + "' and password='" + password + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();

        if( l == 1 ) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE if not exists users (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, \n" +
                "username TEXT, password TEXT \n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE users;");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists users (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, \n" +
                "username TEXT, password TEXT \n" +
                ");");
    }
}
