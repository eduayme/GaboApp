package com.example.eduardaymerich_app_books.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.eduardaymerich_app_books.models.Book;
import com.example.eduardaymerich_app_books.models.BookClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Headers;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gaboapp.db";
    private static final int DATABASE_VERSION = 5;

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE if not exists users " +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " email TEXT," +
            " username TEXT, password TEXT )";
    private static final String DROP_TABLE_USERS = "DROP TABLE if exists users";

    private static final String CREATE_TABLE_USERS_BOOKS =
            "CREATE TABLE if not exists users_books " +
                    "( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " username TEXT," +
                    " id_open_library TEXT )";
    private static final String DROP_TABLE_USERS_BOOKS = "DROP TABLE if exists users_books";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS_BOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_USERS);
        sqLiteDatabase.execSQL(DROP_TABLE_USERS_BOOKS);

        onCreate(sqLiteDatabase);
    }
    
    public void insertUser(ContentValues contentValues) {
        getWritableDatabase().insert("users", "", contentValues );
    }

    public int countBooksFromUser(String username) {
        String sql = "Select count(*) from users_books where username='" + username + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        int numRows = (int) DatabaseUtils.longForQuery(getReadableDatabase(), sql, null);

        return numRows;
    }

    public void insertBookInUser(ContentValues contentValues) {
        String bookId = (String) contentValues.get("id_open_library");
        String username = (String) contentValues.get("username");
        if( !bookIsSavedInUser(bookId, username) ) {
            getWritableDatabase().insert("users_books", "", contentValues );
        }
    }

    public void deleteBookInUser(String[] contentValues) {
        String bookId = (String) contentValues[0];
        String username = (String) contentValues[1];
        if( bookIsSavedInUser(bookId, username) ) {
            getWritableDatabase().delete("users_books", "id_open_library=? and username=?", contentValues );
        }
    }

    public boolean bookIsSavedInUser(String openLibraryId, String username) {
        String sql = "Select count(*) from users_books where id_open_library='" + openLibraryId + "' and username='" + username + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();

        if( l == 1 ) {
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<String> getBooksFromUser(String username) {
        ArrayList<String> booksIds = new ArrayList<String>();
        String selectQuery = "Select id_open_library from users_books where username='" + username + "' ORDER BY id_open_library DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String id = c.getString((c.getColumnIndex("id_open_library")));
                booksIds.add(id);
            } while (c.moveToNext());
        }

        return booksIds;
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
}
