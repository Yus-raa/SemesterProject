package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.util.Log;
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

public class PendingAppointmentsActivity extends AppCompatActivity {

    RecyclerView pendingAppointmentsRecyclerView;
    PendingAppointmentsAdapter adapter;
    List<Appointment> pendingAppointmentsList;

    DatabaseReference databaseReference;
    String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_appointments);

        pendingAppointmentsRecyclerView = findViewById(R.id.pendingAppointmentsRecyclerView);
        pendingAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pendingAppointmentsList = new ArrayList<>();
        adapter = new PendingAppointmentsAdapter(pendingAppointmentsList);
        pendingAppointmentsRecyclerView.setAdapter(adapter);

        doctorId = getIntent().getStringExtra("doctorId");

        if (doctorId == null || doctorId.isEmpty()) {
            Toast.makeText(this, "Error: Missing doctor ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Appointments");

        loadPendingAppointments();
    }

    private void loadPendingAppointments() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pendingAppointmentsList.clear();
                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);

                    if (appointment != null
                            && appointment.getDoctorId() != null
                            && appointment.getDoctorId().equals(doctorId)
                            && "Pending".equalsIgnoreCase(appointment.getStatus())) {

                        pendingAppointmentsList.add(appointment);
                    }
                }
                adapter.notifyDataSetChanged();

                if (pendingAppointmentsList.isEmpty()) {
                    Toast.makeText(PendingAppointmentsActivity.this, "No pending appointments.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PendingAppointmentsActivity.this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                Log.e("PendingAppointments", "DatabaseError", error.toException());
            }
        });
    }

}
