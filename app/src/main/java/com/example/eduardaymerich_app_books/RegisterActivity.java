package com.example.eduardaymerich_app_books;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    EditText email, username, password, confirm_password;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.edit_email);
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        confirm_password = findViewById(R.id.edit_confirm_password);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = email.getText().toString();
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                String confirmValue = confirm_password.getText().toString();
            }
        });
    }

    public void onClickToLogin(View v)
    {
        TextView tv = (TextView) findViewById(R.id.to_login);

        Intent myIntent = new Intent(this, LoginActivity.class);
        this.startActivity(myIntent);
    }

}