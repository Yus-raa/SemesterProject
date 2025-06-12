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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TodaysAppointmentsActivity extends AppCompatActivity {

    RecyclerView todaysAppointmentsRecyclerView;
    TodaysAppointmentsAdapter adapter;
    ArrayList<AppointmentDisplay> todaysAppointmentsList;

    DatabaseReference databaseReference;
    String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_todays_appointments);

        // RecyclerView setup
        todaysAppointmentsRecyclerView = findViewById(R.id.todaysAppointmentsRecyclerView);
        todaysAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todaysAppointmentsList = new ArrayList<>();
        adapter = new TodaysAppointmentsAdapter(todaysAppointmentsList);
        todaysAppointmentsRecyclerView.setAdapter(adapter);

        // Get doctorId
        doctorId = getIntent().getStringExtra("doctorId");
        if (doctorId == null || doctorId.isEmpty()) {
            Toast.makeText(this, "Error: Missing doctor ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference();

        loadTodaysAppointments();
    }

    private void loadTodaysAppointments() {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        databaseReference.child("Appointments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        todaysAppointmentsList.clear();

                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            Appointment appointment = appointmentSnapshot.getValue(Appointment.class);

                            if (appointment != null
                                    && doctorId.equals(appointment.getDoctorId())
                                    && todayDate.equals(appointment.getAppointmentDate()) ){
                                   // && "Approved".equalsIgnoreCase(appointment.getStatus())) {

                                // Now fetch patient name
                                String patientId = appointment.getPatientId();

                                databaseReference.child("Patients").child(patientId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot patientSnapshot) {
                                                String patientName = "Unknown";
                                                if (patientSnapshot.exists()) {
                                                    Patient patient = patientSnapshot.getValue(Patient.class);
                                                    if (patient != null) {
                                                        patientName = patient.getFullName();
                                                    }
                                                }

                                                // Create display object
                                                AppointmentDisplay displayItem = new AppointmentDisplay(
                                                        appointment.getAppointmentDate(),
                                                        patientName,
                                                        appointment.getReason(),
                                                        appointment.getDisease(),
                                                        appointment.getProgress()
                                                );

                                                todaysAppointmentsList.add(displayItem);
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                Log.e("TodaysAppointments", "Error loading patient", error.toException());
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(TodaysAppointmentsActivity.this, "Error loading today's appointments", Toast.LENGTH_SHORT).show();
                        Log.e("TodaysAppointments", "DatabaseError", error.toException());
                    }
                });
    }
}
