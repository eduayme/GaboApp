package com.example.eduardaymerich_app_books;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eduardaymerich_app_books.db.MySQLiteHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText email, username, password, confirm_password;
    Button btn_register;
    MySQLiteHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.edit_email);
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        btn_register = findViewById(R.id.button_register);
        databaseHelper = new MySQLiteHelper(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = email.getText().toString().trim();
                String usernameValue = username.getText().toString().trim();
                String passwordValue = password.getText().toString().trim();

                if( usernameValue.length() > 1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("email", emailValue);
                    contentValues.put("username", usernameValue);
                    contentValues.put("password", passwordValue);

                    databaseHelper.insertUser(contentValues);
                    Toast.makeText(RegisterActivity.this, "User registered! :) |" + usernameValue + "|" + passwordValue, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "User register failed! :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickToLogin(View v)
    {
        Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        this.startActivity(myIntent);
    }

}