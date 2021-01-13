package com.example.eduardaymerich_app_books;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.eduardaymerich_app_books.db.MySQLiteHelper;
import com.example.eduardaymerich_app_books.models.Book;
import com.example.eduardaymerich_app_books.models.BookClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class BookDetailsActivity extends AppCompatActivity {
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublishDate;
    private TextView tvPublishCountry;
    private TextView tvPublisher;
    private TextView tvPageCount;
    private Book book;
    private MySQLiteHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        tvPublishDate = (TextView) findViewById(R.id.tvPublishDate);
        tvPublishCountry = (TextView) findViewById(R.id.tvPublishCountry);

        // book data into views
        book = (Book) getIntent().getSerializableExtra(HomeActivity.BOOK_DETAIL_KEY);
        loadBook(book);

        // set menu
        invalidateOptionsMenu();

        // load database;
        databaseHelper = new MySQLiteHelper(this);
    }

    private void loadBook(Book book) {
        // set book title as title
        this.setTitle(book.getTitle());

        // get data
        Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.no_cover).into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());

        // fetch extra book data from books API
        BookClient client = new BookClient();
        client.getExtraDetailsBook(book.getOpenLibraryId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                try {
                    JSONObject response = json.jsonObject;

                    // Publish date
                    if (response.has("publish_date")) {
                        tvPublishDate.setText(response.getString("publish_date"));
                    }
                    else {
                        tvPublishDate.setText("--");
                    }

                    // Publish country
                    if (response.has("publish_country")) {
                        tvPublishCountry.setText(response.getString("publish_country").toUpperCase());
                    }
                    else {
                        tvPublishCountry.setText("--");
                    }

                    // Publishers
                    if (response.has("publishers")) {
                        // display comma separated list of publishers
                        final JSONArray publisher = response.getJSONArray("publishers");
                        final int numPublishers = publisher.length();
                        final String[] publishers = new String[numPublishers];
                        for (int i = 0; i < numPublishers; ++i) {
                            publishers[i] = publisher.getString(i);
                        }
                        tvPublisher.setText(TextUtils.join(", ", publishers));
                    }
                    else {
                        tvPublisher.setText("--");
                    }

                    // Nº pages
                    if (response.has("number_of_pages")) {
                        tvPageCount.setText(Integer.toString(response.getInt("number_of_pages")));
                    }
                    else {
                        tvPageCount.setText("--");
                    }

                    Log.d("response: ", response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(BookDetailsActivity.this, "Error getting details from the book", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_details, menu);

        MenuItem item_save = menu.findItem(R.id.action_save);
        MenuItem item_delete = menu.findItem(R.id.action_delete);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String usernameCurrentUser = settings.getString("username", "").toString();

        if( databaseHelper.bookIsSavedInUser(book.getOpenLibraryId(), usernameCurrentUser) ) {
            item_save.setVisible(false);
            item_delete.setVisible(true);
        }
        else {
            item_save.setVisible(true);
            item_delete.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String usernameCurrentUser = settings.getString("username", "").toString();

        switch (item.getItemId()) {
            case R.id.action_save:
                // prepare data
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", usernameCurrentUser);
                contentValues.put("id_open_library", book.getOpenLibraryId());

                // save book in user
                databaseHelper.insertBookInUser(contentValues);
                invalidateOptionsMenu();

                // display toast job done
                Toast.makeText(BookDetailsActivity.this, book.getTitle() + " saved! :)", Toast.LENGTH_LONG).show();

                return true;

            case R.id.action_delete:
                // prepare data
                String[] deleteData = {book.getOpenLibraryId(), usernameCurrentUser};

                // delete book in user
                databaseHelper.deleteBookInUser(deleteData);
                invalidateOptionsMenu();

                // display toast job done
                Toast.makeText(BookDetailsActivity.this, book.getTitle() + " removed! :)", Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item_save = menu.findItem(R.id.action_save);
        MenuItem item_delete = menu.findItem(R.id.action_delete);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String usernameCurrentUser = settings.getString("username", "").toString();

        if( databaseHelper.bookIsSavedInUser(book.getOpenLibraryId(), usernameCurrentUser) ) {
            item_save.setVisible(false);
            item_delete.setVisible(true);
        }
        else {
            item_save.setVisible(true);
            item_delete.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }
}
