package com.example.medicalmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientHomeActivity extends AppCompatActivity {

    TextView welcomeTextView;
    Button viewProfileBtn, bookAppointmentBtn, treatmentHistoryBtn, billsHistoryBtn, logoutBtn;

    DatabaseReference databaseReference;
    String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_home);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        viewProfileBtn = findViewById(R.id.viewProfileBtn);
        bookAppointmentBtn = findViewById(R.id.bookAppointmentBtn);
        treatmentHistoryBtn = findViewById(R.id.treatmentHistoryBtn);
        billsHistoryBtn = findViewById(R.id.billsHistoryBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Get the patientId dynamically from the Intent
        patientId = getIntent().getStringExtra("patientId");
        if (patientId == null || patientId.isEmpty()) {
            Toast.makeText(this, "Error: Missing patient ID!", Toast.LENGTH_LONG).show();
            finish();  // Can't proceed without patientId
            return;
        }

        // Initialize databaseReference using dynamic patientId
        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Patients").child(patientId);

        loadPatientName();

        // Button listeners
        viewProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PatientHomeActivity.this, PatientProfileActivity.class);
            intent.putExtra("patientId", patientId);  // Pass patientId
            startActivity(intent);
        });

        bookAppointmentBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PatientHomeActivity.this, PatientAppointmentActivity.class);
            intent.putExtra("patientId", patientId);  // Pass patientId
            startActivity(intent);
        });

        treatmentHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PatientHomeActivity.this, PatientTreatmentHistoryActivity.class);
            intent.putExtra("patientId", patientId);  // Pass patientId
            startActivity(intent);
        });

        billsHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PatientHomeActivity.this, PatientBillsHistoryActivity.class);
            intent.putExtra("patientId", patientId);  // Pass patientId
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show();
            // no proper sign-out logic yet (e.g., mAuth.signOut())
            Intent intent = new Intent(PatientHomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Close PatientHomeActivity
        });
    }

    private void loadPatientName() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Patient patient = snapshot.getValue(Patient.class);
                    if (patient != null) {
                        welcomeTextView.setText("Welcome, " + patient.getFullName() + "!");
                    }
                } else {
                    Toast.makeText(PatientHomeActivity.this, "Patient not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PatientHomeActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                Log.e("PatientHome", "DatabaseError", error.toException());
            }
        });
    }
}
