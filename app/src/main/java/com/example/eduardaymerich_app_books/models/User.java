package com.example.eduardaymerich_app_books.models;

public class User {
    private int id = -1;
    private String email;
    private String username;
    private String password;

    public User(int id, String email, String username, String password){
        this.id=id;
        this.email=email;
        this.username=username;
        this.password=password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
