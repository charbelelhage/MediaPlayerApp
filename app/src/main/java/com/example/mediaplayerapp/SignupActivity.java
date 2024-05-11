package com.example.mediaplayerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private Spinner sectorsSpinner;
    private FirestoreHelper firestoreHelper;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseHelper = new DatabaseHelper(this);
        firestoreHelper = new FirestoreHelper();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        sectorsSpinner = findViewById(R.id.sectorsSpinner);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        loadSectors();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });
    }

    private void loadSectors() {
        firestoreHelper.fetchSectors(new FirestoreHelper.FirestoreCallback() {
            @Override
            public void onCallback(List<String> sectorNames) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, sectorNames);
                sectorsSpinner.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SignupActivity.this, "Failed to load sectors: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void postDataToSQLite() {
        if (!databaseHelper.checkUser(editTextEmail.getText().toString().trim(),editTextPassword.getText().toString().trim())) {
            databaseHelper.addUser(editTextEmail.getText().toString().trim(),
                    editTextPassword.getText().toString().trim());
            Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show();
            emptyInputEditText();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Account already exists", Toast.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        editTextEmail.setText(null);
        editTextPassword.setText(null);
    }
}
