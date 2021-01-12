package com.example.eduardaymerich_app_books;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduardaymerich_app_books.db.MySQLiteHelper;

public class LoginActivity<textView> extends AppCompatActivity {
    EditText username, password;
    Button btn_login;
    MySQLiteHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.button_login);
        databaseHelper = new MySQLiteHelper(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();

                if( databaseHelper.isLoginValid(usernameValue, passwordValue)) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);

                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("username", usernameValue);
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Login successful! :)", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login failed! :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickToRegister(View v)
    {
        Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        this.startActivity(myIntent);
    }

}