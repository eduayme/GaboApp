package com.example.eduardaymerich_app_books.models;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Book {
    private String openLibraryId;
    private String author;
    private String title;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" +
                openLibraryId +
                "-M.jpg?default=false";
    }

    public String getLargeCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" +
                openLibraryId +
                "-L.jpg?default=false";
    }

    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            // Si portada disponible
            if (jsonObject.has("cover_edition_key"))  {
                book.openLibraryId = jsonObject.getString("cover_edition_key");
            }
            // Si no tiene portada
            else if(jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.openLibraryId = ids.getString(0);
            }
            book.title = jsonObject.has("title_suggest") ?
                    jsonObject.getString("title_suggest") : "";
            book.author = getAuthor(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return book;
    }

    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];

            // Obtener todos lo autores en caso de mas de uno
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }

            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    // Tratar resultado json de una lista de libros
    public static ArrayList<Book> fromJson(JSONArray jsonArray) {
        // Lista temporal
        ArrayList<Book> books = new ArrayList<Book>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Book book = Book.fromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }

        return books;
    }
}
