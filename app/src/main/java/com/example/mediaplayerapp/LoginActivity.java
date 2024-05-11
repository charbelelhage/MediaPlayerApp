package com.example.mediaplayerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonGoToSignup = findViewById(R.id.buttonGoToSignup);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFromSQLite();
            }
        });

        buttonGoToSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void verifyFromSQLite() {
        if (databaseHelper.checkUser(editTextEmail.getText().toString().trim(),
                editTextPassword.getText().toString().trim())) {
            Spotify.getAccessToken();try {
                Thread.sleep(3000); // Sleep for 5 seconds (5000 milliseconds)
            } catch (InterruptedException e) {
                // Handle interrupted exception if needed
            }
            Intent accountsIntent = new Intent(LoginActivity.this, MainActivity.class);
            accountsIntent.putExtra("EMAIL", editTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        editTextEmail.setText(null);
        editTextPassword.setText(null);
    }
}
