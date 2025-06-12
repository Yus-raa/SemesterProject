package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PatientAppointmentActivity extends AppCompatActivity {

    TextView patientNameTextView;
    Spinner doctorSpinner;
    EditText appointmentDateEditText, reasonEditText;
    Button submitAppointmentBtn;

    DatabaseReference databaseReference;
    String patientId;

    ArrayList<String> doctorList = new ArrayList<>();
    ArrayList<String> doctorIds = new ArrayList<>(); // To store doctor IDs corresponding to names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_appointment);

        patientNameTextView = findViewById(R.id.patientNameTextView);
        doctorSpinner = findViewById(R.id.doctorSpinner);
        appointmentDateEditText = findViewById(R.id.appointmentDateEditText);
        reasonEditText = findViewById(R.id.reasonEditText);
        submitAppointmentBtn = findViewById(R.id.submitAppointmentBtn);

        patientId = getIntent().getStringExtra("patientId");
        if (patientId == null || patientId.isEmpty()) {
            Toast.makeText(this, "Error: Missing patient ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        loadPatientName();
        loadDoctors();

        submitAppointmentBtn.setOnClickListener(v -> submitAppointment());
    }

    private void loadPatientName() {
        databaseReference.child("Patients").child(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Patient patient = snapshot.getValue(Patient.class);
                            if (patient != null) {
                                patientNameTextView.setText(patient.getFullName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("PatientAppointment", "Error loading patient", error.toException());
                    }
                });
    }

    private void loadDoctors() {
        databaseReference.child("Doctors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        doctorList.clear();
                        doctorIds.clear();

                        for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                            Doctor doctor = doctorSnapshot.getValue(Doctor.class);
                            if (doctor != null) {
                                doctorList.add(doctor.getName() + " (" + doctor.getSpecialization() + ")");
                                doctorIds.add(doctorSnapshot.getKey());
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                PatientAppointmentActivity.this,
                                android.R.layout.simple_spinner_item,
                                doctorList
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        doctorSpinner.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("PatientAppointment", "Error loading doctors", error.toException());
                    }
                });
    }

    private void submitAppointment() {
        int selectedDoctorIndex = doctorSpinner.getSelectedItemPosition();
        String doctorId = doctorIds.get(selectedDoctorIndex);

        String appointmentDate = appointmentDateEditText.getText().toString().trim();
        String reason = reasonEditText.getText().toString().trim();

        if (appointmentDate.isEmpty() || reason.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create unique appointmentId
        String appointmentId = UUID.randomUUID().toString();

        // Build data map
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("appointmentId", appointmentId);
        appointmentData.put("patientId", patientId);
        appointmentData.put("doctorId", doctorId);
        appointmentData.put("appointmentDate", appointmentDate);
        appointmentData.put("reason", reason);
        appointmentData.put("status", "Pending"); // Status can be Pending, Approved, Rejected
        appointmentData.put("prescription", ""); // Empty for now
        appointmentData.put("disease", ""); // Empty for now
        appointmentData.put("progress", ""); // Empty for now

        // Save to DB: /Appointments/appointmentId
        databaseReference.child("Appointments").child(appointmentId).setValue(appointmentData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show();
                    finish(); // Go back to PatientHome
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                    Log.e("PatientAppointment", "Error saving appointment", e);
                });
    }
}
