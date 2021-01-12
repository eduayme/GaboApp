package com.example.eduardaymerich_app_books.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_1 =
            "CREATE TABLE if not exists users " +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " email TEXT," +
            " username TEXT, password TEXT )";
    private static final String DROP_TABLE_1 = "DROP TABLE users;";

    private static final String CREATE_TABLE_2 =
            "CREATE TABLE if not exists users_books " +
                    "( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " username TEXT," +
                    " id_open_library TEXT )";
    private static final String DROP_TABLE_2 = "DROP TABLE users_books;";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_1);
        sqLiteDatabase.execSQL(CREATE_TABLE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_1);
        sqLiteDatabase.execSQL(DROP_TABLE_2);

        onCreate(sqLiteDatabase);
    }
    
    public void insertUser(ContentValues contentValues) {
        getWritableDatabase().insert("users", "", contentValues );
    }

    public boolean userHasBook(String username) {
        String sql = "Select count(*) from users_books where username='" + username + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();

        if( l == 1 ) {
            return true;
        }
        else {
            return false;
        }
    }

    public void insertBookInUser(ContentValues contentValues) {
        getWritableDatabase().insert("users_books", "", contentValues );
    }

    public void deleteBookInUser(String[] contentValues) {
        getWritableDatabase().delete("users_books", "username=? and id_open_library=?", contentValues );
    }

    public boolean bookIsSavedInUser(String openLibraryId) {
        boolean isSaved = false;

        //getWritableDatabase().delete("books", "", contentValues );

        return isSaved;
    }

    public ArrayList<Book> getBooksFromUser(String username) {
        ArrayList<Book> books = new ArrayList<Book>();
        String selectQuery = "Select * from users_books where username='" + username + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            BookClient client = new BookClient();

            do {
                String id = c.getString((c.getColumnIndex("id_open_library")));
                client.getExtraDetailsBook( id, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONObject response = json.jsonObject;

                        if(response != null) {
                            Book b = Book.fromSingleJson(response);

                            // adding to books list
                            books.add(b);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        // Nothing
                    }
                });

            } while (c.moveToNext());
        }

        return books;
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
