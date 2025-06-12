package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PatientRegistrationActivity extends AppCompatActivity {

    TextInputEditText fullNameEditText, emailEditText, passwordEditText, phoneEditText, dobEditText,
            emergencyContactNameEditText, emergencyContactPhoneEditText;
    RadioGroup genderRadioGroup;
    Button registerButton;

    FirebaseAuth mAuth;
    DatabaseReference patientsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dobEditText = findViewById(R.id.dobEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        emergencyContactNameEditText = findViewById(R.id.emergencyContactNameEditText);
        emergencyContactPhoneEditText = findViewById(R.id.emergencyContactPhoneEditText);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();
        patientsRef = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Patients");

        registerButton.setOnClickListener(v -> registerPatient());
    }

    private void registerPatient() {
        final String fullName = fullNameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString();
        final String phone = phoneEditText.getText().toString().trim();
        final String dob = dobEditText.getText().toString().trim();

        final int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        final String gender;
        if (selectedGenderId != -1) {
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            gender = selectedGenderButton.getText().toString();
        } else {
            gender = "";
        }

        final String emergencyContactName = emergencyContactNameEditText.getText().toString().trim();
        final String emergencyContactPhone = emergencyContactPhoneEditText.getText().toString().trim();

        // Basic validation
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Full Name, Email and Password are required.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        String patientId = firebaseUser.getUid();

                        // Prepare patient data map
                        Map<String, Object> patientData = new HashMap<>();
                        patientData.put("fullName", fullName);
                        patientData.put("email", email);
                        patientData.put("phone", phone);
                        patientData.put("dob", dob);
                        patientData.put("gender", gender);
                        patientData.put("emergencyContactName", emergencyContactName);
                        patientData.put("emergencyContactPhone", emergencyContactPhone);

                        // Save patient profile in Firebase Realtime Database
                        patientsRef.child(patientId).setValue(patientData)
                                .addOnSuccessListener(aVoid -> {
                                    // Show success Toast
                                    Toast.makeText(this, "Patient Registered Successfully!", Toast.LENGTH_SHORT).show();

                                    // Clear all fields
                                    clearFields();
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save patient data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    private void clearFields() {
        fullNameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        phoneEditText.setText("");
        dobEditText.setText("");
        genderRadioGroup.clearCheck();
        emergencyContactNameEditText.setText("");
        emergencyContactPhoneEditText.setText("");
    }


}
