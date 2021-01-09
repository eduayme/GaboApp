package com.example.eduardaymerich_app_books;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity<textView> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickToRegister(View v)
    {
        TextView tv = (TextView) findViewById(R.id.to_register);

        Intent myIntent = new Intent(this, RegisterActivity.class);
        this.startActivity(myIntent);
    }

}