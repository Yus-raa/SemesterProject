package com.example.medicalmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorRegistrationActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, specializationEditText;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private DatabaseReference doctorsRef;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        nameEditText = findViewById(R.id.editTextDoctorName);
        emailEditText = findViewById(R.id.editTextDoctorEmail);
        passwordEditText = findViewById(R.id.editTextDoctorPassword);
        specializationEditText = findViewById(R.id.editTextDoctorSpecialization);
        registerButton = findViewById(R.id.buttonDoctorRegister);

        mAuth = FirebaseAuth.getInstance();
        doctorsRef = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Doctors");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering doctor...");

        registerButton.setOnClickListener(v -> registerDoctor());
    }

    private void registerDoctor() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String specialization = specializationEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Enter name");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Enter email");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }
        if (TextUtils.isEmpty(specialization)) {
            specializationEditText.setError("Enter specialization");
            return;
        }

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        Doctor doctor = new Doctor(name, email, specialization);

                        doctorsRef.child(uid).setValue(doctor)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(DoctorRegistrationActivity.this,
                                                "Doctor registered successfully", Toast.LENGTH_LONG).show();

                                        // Clear fields after registration
                                        clearFields();

                                        // Optionally navigate to MainActivity or stay on the screen
                                        startActivity(new Intent(DoctorRegistrationActivity.this, MainActivity.class));


                                    } else {
                                        Toast.makeText(DoctorRegistrationActivity.this,
                                                "Failed to save doctor data", Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {
                        Toast.makeText(DoctorRegistrationActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void clearFields() {
        nameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        specializationEditText.setText("");
    }



    // Doctor model class (can be in a separate file if you want)
    public static class Doctor {
        public String name, email, specialization;

        public Doctor() { }

        public Doctor(String name, String email, String specialization) {
            this.name = name;
            this.email = email;
            this.specialization = specialization;
        }
    }
}
