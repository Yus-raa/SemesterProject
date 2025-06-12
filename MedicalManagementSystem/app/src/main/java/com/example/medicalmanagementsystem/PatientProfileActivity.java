package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfileActivity extends AppCompatActivity {

    TextView fullNameTextView, emailTextView, phoneTextView, dobTextView, emergencyNameTextView, emergencyPhoneTextView;

    DatabaseReference databaseReference;
    String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // Initialize TextViews
        fullNameTextView = findViewById(R.id.fullNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        dobTextView = findViewById(R.id.dobTextView);
        emergencyNameTextView = findViewById(R.id.emergencyNameTextView);
        emergencyPhoneTextView = findViewById(R.id.emergencyPhoneTextView);

        // Get patientId from intent
        patientId = getIntent().getStringExtra("patientId");
        if (patientId == null || patientId.isEmpty()) {
            Toast.makeText(this, "Error: Missing patient ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Setup Firebase reference
        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Patients").child(patientId);

        loadPatientProfile();
    }

    private void loadPatientProfile() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Patient patient = snapshot.getValue(Patient.class);
                    if (patient != null) {
                        fullNameTextView.setText(patient.getFullName());
                        emailTextView.setText(patient.getEmail());
                        phoneTextView.setText(patient.getPhone());
                        dobTextView.setText(patient.getDob());
                        emergencyNameTextView.setText(patient.getEmergencyContactName());
                        emergencyPhoneTextView.setText(patient.getEmergencyContactPhone());
                    }
                } else {
                    Toast.makeText(PatientProfileActivity.this, "Patient not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PatientProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                Log.e("PatientProfile", "DatabaseError", error.toException());
            }
        });
    }
}
