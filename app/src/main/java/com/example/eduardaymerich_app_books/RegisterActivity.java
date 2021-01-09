package com.example.eduardaymerich_app_books;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClickToLogin(View v)
    {
        TextView tv = (TextView) findViewById(R.id.to_login);

        Intent myIntent = new Intent(this, LoginActivity.class);
        this.startActivity(myIntent);
    }

}