package com.example.medicalmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Spinner roleSpinner;  // Optional - UI only
    Button loginButton;
    TextView registerPatientTextView, registerDoctorTextView;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleSpinner = findViewById(R.id.roleSpinner);  // Optional
        loginButton = findViewById(R.id.loginButton);
        registerPatientTextView = findViewById(R.id.registerPatientTextView);
        registerDoctorTextView = findViewById(R.id.registerDoctorTextView);

        mAuth = FirebaseAuth.getInstance();

        // Setup Spinner — UI only
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Patient", "Doctor"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Login Button Click
        loginButton.setOnClickListener(v -> loginUser());

        // Register Patient Click
        registerPatientTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PatientRegistrationActivity.class);
            startActivity(intent);
        });

        // Register Doctor Click
        registerDoctorTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DoctorRegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();

                        // Check in Doctors first
                        DatabaseReference doctorsRef = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("Doctors").child(uid);

                        doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Doctor login
                                    Toast.makeText(MainActivity.this, "Doctor login successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, DoctorHomeActivity.class);
                                    intent.putExtra("doctorId", uid);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Check Patients
                                    DatabaseReference patientsRef = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app")
                                            .getReference("Patients").child(uid);

                                    patientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                // Patient login
                                                Toast.makeText(MainActivity.this, "Patient login successful!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity.this, PatientHomeActivity.class);
                                                intent.putExtra("patientId", uid);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // Not found in either role
                                                Toast.makeText(MainActivity.this, "Error: User role not found!", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            Toast.makeText(MainActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(MainActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
