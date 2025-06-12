package com.example.medicalmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorHomeActivity extends AppCompatActivity {

    TextView welcomeTextView;
    Button profileBtn, pendingAppointmentsBtn, todaysAppointmentsBtn,
            historyUpdateBtn, generateBillBtn, patientHistoryBtn, logoutBtn;

    DatabaseReference databaseReference;
    String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_home);

        // UI references
        welcomeTextView = findViewById(R.id.welcomeTextView);
        profileBtn = findViewById(R.id.profileBtn);
        pendingAppointmentsBtn = findViewById(R.id.pendingAppointmentsBtn);
        todaysAppointmentsBtn = findViewById(R.id.todaysAppointmentsBtn);
        historyUpdateBtn = findViewById(R.id.historyUpdateBtn);
        generateBillBtn = findViewById(R.id.generateBillBtn);
        patientHistoryBtn = findViewById(R.id.patientHistoryBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Get doctorId from intent
        doctorId = getIntent().getStringExtra("doctorId");
        if (doctorId == null || doctorId.isEmpty()) {
            Toast.makeText(this, "Error: Missing doctor ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Reference to doctor node
        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Doctors").child(doctorId);

        loadDoctorName();

        // Buttons click listeners

        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorHomeActivity.this, DoctorProfileActivity.class);
            intent.putExtra("doctorId", doctorId);
            startActivity(intent);
        });

        pendingAppointmentsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorHomeActivity.this, PendingAppointmentsActivity.class);
            intent.putExtra("doctorId", doctorId);
            startActivity(intent);
        });

        todaysAppointmentsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorHomeActivity.this, TodaysAppointmentsActivity.class);
            intent.putExtra("doctorId", doctorId);
            startActivity(intent);
        });

        historyUpdateBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorHomeActivity.this, HistoryUpdateActivity.class);
            intent.putExtra("doctorId", doctorId);
            startActivity(intent);
        });

        generateBillBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorHomeActivity.this, GenerateBillActivity.class);
            intent.putExtra("doctorId", doctorId);
            startActivity(intent);
        });

        patientHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorHomeActivity.this, PatientHistoryActivity.class);
            intent.putExtra("doctorId", doctorId);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DoctorHomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadDoctorName() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        welcomeTextView.setText("Welcome, " + doctor.getName() + "!");
                    }
                } else {
                    Toast.makeText(DoctorHomeActivity.this, "Doctor not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DoctorHomeActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                Log.e("DoctorHome", "DatabaseError", error.toException());
            }
        });
    }
}
