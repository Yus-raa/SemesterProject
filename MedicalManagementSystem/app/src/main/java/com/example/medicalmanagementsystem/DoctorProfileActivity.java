package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfileActivity extends AppCompatActivity {

    TextView nameTextView, emailTextView, specializationTextView;
    DatabaseReference databaseReference;
    String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_profile);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        specializationTextView = findViewById(R.id.specializationTextView);

        // Get doctorId from intent
        doctorId = getIntent().getStringExtra("doctorId");
        if (doctorId == null || doctorId.isEmpty()) {
            Toast.makeText(this, "Error: Missing doctor ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Doctors").child(doctorId);

        loadDoctorProfile();
    }

    private void loadDoctorProfile() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Doctor doctor = snapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        nameTextView.setText(doctor.getName());
                        emailTextView.setText(doctor.getEmail());
                        specializationTextView.setText(doctor.getSpecialization());
                    }
                } else {
                    Toast.makeText(DoctorProfileActivity.this, "Doctor not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DoctorProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                Log.e("DoctorProfile", "DatabaseError", error.toException());
            }
        });
    }
}
