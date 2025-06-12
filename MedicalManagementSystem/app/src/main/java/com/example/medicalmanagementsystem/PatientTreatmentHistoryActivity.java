package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PatientTreatmentHistoryActivity extends AppCompatActivity {

    TextView noHistoryTextView;
    RecyclerView treatmentHistoryRecyclerView;

    TreatmentHistoryAdapter adapter;
    List<TreatmentHistoryItem> historyList = new ArrayList<>();

    DatabaseReference databaseReference;
    String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_treatment_history);

        noHistoryTextView = findViewById(R.id.noHistoryTextView);
        treatmentHistoryRecyclerView = findViewById(R.id.treatmentHistoryRecyclerView);

        treatmentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TreatmentHistoryAdapter(historyList);
        treatmentHistoryRecyclerView.setAdapter(adapter);

        patientId = getIntent().getStringExtra("patientId");
        if (patientId == null || patientId.isEmpty()) {
            Toast.makeText(this, "Error: Missing patient ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        loadTreatmentHistory();
    }

    private void loadTreatmentHistory() {
        databaseReference.child("Appointments")
                .orderByChild("patientId").equalTo(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        historyList.clear();

                        if (snapshot.exists()) {
                            for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                                String appointmentDate = appointmentSnapshot.child("appointmentDate").getValue(String.class);
                                String doctorId = appointmentSnapshot.child("doctorId").getValue(String.class);
                                String disease = appointmentSnapshot.child("disease").getValue(String.class);
                                String prescription = appointmentSnapshot.child("prescription").getValue(String.class);
                                String progress = appointmentSnapshot.child("progress").getValue(String.class);

                                // Show only appointments that have a treatment record (doctor has filled in data)
                                if (disease != null && !disease.isEmpty()) {
                                    TreatmentHistoryItem item = new TreatmentHistoryItem(appointmentDate, doctorId, disease, prescription, progress);
                                    historyList.add(item);
                                }
                            }

                            if (historyList.isEmpty()) {
                                noHistoryTextView.setVisibility(View.VISIBLE);
                                treatmentHistoryRecyclerView.setVisibility(View.GONE);
                            } else {
                                noHistoryTextView.setVisibility(View.GONE);
                                treatmentHistoryRecyclerView.setVisibility(View.VISIBLE);
                                adapter.notifyDataSetChanged();
                                loadDoctorNames(); // replace doctorId with doctor name
                            }
                        } else {
                            noHistoryTextView.setVisibility(View.VISIBLE);
                            treatmentHistoryRecyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("TreatmentHistory", "Error loading treatment history", error.toException());
                    }
                });
    }

    private void loadDoctorNames() {
        databaseReference.child("Doctors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (TreatmentHistoryItem item : historyList) {
                            String doctorId = item.getDoctorId();

                            DataSnapshot doctorSnapshot = snapshot.child(doctorId);
                            Doctor doctor = doctorSnapshot.getValue(Doctor.class);
                            if (doctor != null) {
                                item.setDoctorName(doctor.getName());
                            }
                        }
                        adapter.notifyDataSetChanged(); // Update doctor names
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("TreatmentHistory", "Error loading doctors", error.toException());
                    }
                });
    }
}
