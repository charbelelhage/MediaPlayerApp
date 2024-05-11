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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private Spinner sectorsSpinner;
    private FirestoreHelper firestoreHelper;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firestoreHelper = new FirestoreHelper();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        sectorsSpinner = findViewById(R.id.sectorsSpinner);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        loadSectors();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
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

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String sector = sectorsSpinner.getSelectedItem().toString();

        // Create a new user with Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // On successful auth, store additional fields in Firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("sector", sector);

                        db.collection("users").document(auth.getCurrentUser().getUid())
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(SignupActivity.this, "Account created successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(SignupActivity.this, "Failed to save user details: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    } else {
                        Toast.makeText(SignupActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
