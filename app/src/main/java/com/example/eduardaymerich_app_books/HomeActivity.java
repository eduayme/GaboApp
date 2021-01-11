package com.example.eduardaymerich_app_books;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.eduardaymerich_app_books.adapters.BookAdapter;
import com.example.eduardaymerich_app_books.models.Book;
import com.example.eduardaymerich_app_books.models.BookClient;

// To be able to do the call to get all to the book search API
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import okhttp3.Headers;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ListView lvBooks;
    private BookAdapter bookAdapter;
    private BookClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvBooks = (ListView) findViewById(R.id.lvBooks);
        ArrayList<Book> aBooks = new ArrayList<Book>();
        bookAdapter = new BookAdapter(this, aBooks);
        lvBooks.setAdapter(bookAdapter);
        
        // get books data
        fetchBooks();
    }

    // Do the API call to the OpenLibrary
    private void fetchBooks() {
        client = new BookClient();
        client.getBooks("oscar Wilde", new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(HomeActivity.this, "Error onFailure HomeActivity! :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray docs = null;

                    if(json != null) {
                        // Crear json object
                        JSONObject response = new JSONObject(String.valueOf(json));

                        // Get docs json array
                        docs = response.getJSONArray("docs");

                        // Parse json array into array of objects
                        final ArrayList<Book> books = Book.fromJson(docs);

                        // Remove all books from the adapter
                        bookAdapter.clear();

                        // Load model objects into the adapter
                        for (Book book : books) {
                            bookAdapter.add(book);
                        }

                        bookAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
}