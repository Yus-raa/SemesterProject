package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;

public class PatientHistoryActivity extends AppCompatActivity {

    Spinner patientSpinner;
    Button loadHistoryBtn;
    TextView historyTextView;

    DatabaseReference databaseReference;
    String doctorId;

    ArrayList<String> patientIds = new ArrayList<>();
    ArrayList<String> patientDisplayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_history);

        patientSpinner = findViewById(R.id.patientSpinner);
        loadHistoryBtn = findViewById(R.id.loadHistoryBtn);
        historyTextView = findViewById(R.id.historyTextView);

        doctorId = getIntent().getStringExtra("doctorId");
        if (doctorId == null || doctorId.isEmpty()) {
            Toast.makeText(this, "Error: Missing doctor ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        loadPatients();

        loadHistoryBtn.setOnClickListener(v -> loadPatientHistory());
    }

    private void loadPatients() {
        databaseReference.child("Appointments")
                .orderByChild("doctorId").equalTo(doctorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        patientIds.clear();
                        patientDisplayList.clear();

                        List<String> uniquePatientIds = new ArrayList<>();

                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            String patientId = appointmentSnapshot.child("patientId").getValue(String.class);

                            if (patientId != null && !uniquePatientIds.contains(patientId)) {
                                uniquePatientIds.add(patientId);
                            }
                        }

                        if (uniquePatientIds.isEmpty()) {
                            Toast.makeText(PatientHistoryActivity.this, "No patients found!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Now load patient names from Patients table
                        for (String pId : uniquePatientIds) {
                            databaseReference.child("Patients").child(pId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot patientSnapshot) {
                                            String name = patientSnapshot.child("fullName").getValue(String.class);

                                            patientIds.add(pId);
                                            patientDisplayList.add(name + " (" + pId + ")");

                                            if (patientIds.size() == uniquePatientIds.size()) {
                                                // All loaded → update spinner
                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                                        PatientHistoryActivity.this,
                                                        android.R.layout.simple_spinner_item,
                                                        patientDisplayList
                                                );
                                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                patientSpinner.setAdapter(adapter);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            Log.e("PatientHistory", "Error loading patient name", error.toException());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("PatientHistory", "Error loading patients", error.toException());
                    }
                });
    }

    private void loadPatientHistory() {
        int selectedIndex = patientSpinner.getSelectedItemPosition();
        if (selectedIndex < 0 || selectedIndex >= patientIds.size()) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show();
            return;
        }

        String patientId = patientIds.get(selectedIndex);

        historyTextView.setText("Loading history...");

        StringBuilder historyBuilder = new StringBuilder();

        // Load Appointments
        databaseReference.child("Appointments")
                .orderByChild("patientId").equalTo(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        historyBuilder.append("---- Appointments ----\n");

                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            String appointmentId = appointmentSnapshot.child("appointmentId").getValue(String.class);
                            String appointmentDate = appointmentSnapshot.child("appointmentDate").getValue(String.class);
                            String reason = appointmentSnapshot.child("reason").getValue(String.class);
                            String status = appointmentSnapshot.child("status").getValue(String.class);
                            String prescription = appointmentSnapshot.child("prescription").getValue(String.class);
                            String disease = appointmentSnapshot.child("disease").getValue(String.class);
                            String progress = appointmentSnapshot.child("progress").getValue(String.class);

                            historyBuilder.append("ID: ").append(appointmentId).append("\n")
                                    .append("Date: ").append(appointmentDate).append("\n")
                                    .append("Reason: ").append(reason).append("\n")
                                    .append("Status: ").append(status).append("\n")
                                    .append("Prescription: ").append(prescription).append("\n")
                                    .append("Disease: ").append(disease).append("\n")
                                    .append("Progress: ").append(progress).append("\n\n");
                        }

                        // After Appointments → Load Bills
                        loadBills(patientId, historyBuilder);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("PatientHistory", "Error loading appointments", error.toException());
                    }
                });
    }

    private void loadBills(String patientId, StringBuilder historyBuilder) {
        databaseReference.child("Bills")
                .orderByChild("patientId").equalTo(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        historyBuilder.append("---- Bills ----\n");

                        for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                            String billId = billSnapshot.child("billId").getValue(String.class);
                            Double totalAmount = billSnapshot.child("totalAmount").getValue(Double.class);
                            String billDate = billSnapshot.child("billDate").getValue(String.class);

                            historyBuilder.append("Bill ID: ").append(billId).append("\n")
                                    .append("Date: ").append(billDate).append("\n")
                                    .append("Total: ").append(totalAmount).append("\n\n");
                        }

                        // Show full history
                        historyTextView.setText(historyBuilder.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("PatientHistory", "Error loading bills", error.toException());
                    }
                });
    }
}
