package com.example.ecosynergy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get references to UI elements
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button registerButton = findViewById(R.id.registerButton);

        // Set up the register button click listener
        registerButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Validate inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Register the user with Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Registration successful
                            Log.i(TAG, "User registered successfully");
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

                            // Redirect to LoginActivity or another screen
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Close this activity
                        } else {
                            // Handle registration failure
                            Log.e(TAG, "Registration failed: " + task.getException());
                            handleRegistrationError(task.getException());
                        }
                    });
        });
    }

    /**
     * Handles Firebase registration errors and provides user-friendly messages.
     *
     * @param exception The exception returned by Firebase.
     */
    private void handleRegistrationError(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            Toast.makeText(this, "Weak password: Password must be at least 6 characters", Toast.LENGTH_LONG).show();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_LONG).show();
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "An account with this email already exists", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Registration failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

