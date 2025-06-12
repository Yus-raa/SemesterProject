package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryUpdateActivity extends AppCompatActivity {

    RecyclerView historyUpdateRecyclerView;
    DatabaseReference databaseReference;
    String doctorId;

    ArrayList<Appointment> appointmentList = new ArrayList<>();
    HistoryUpdateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_update);

        historyUpdateRecyclerView = findViewById(R.id.historyUpdateRecyclerView);
        historyUpdateRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        doctorId = getIntent().getStringExtra("doctorId");
        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        adapter = new HistoryUpdateAdapter(this, appointmentList, databaseReference);
        historyUpdateRecyclerView.setAdapter(adapter);

        loadAppointments();
    }

    private void loadAppointments() {
        databaseReference.child("Appointments")
                .orderByChild("doctorId").equalTo(doctorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        appointmentList.clear();
                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                            if (appointment != null) {
                                appointmentList.add(appointment);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HistoryUpdateActivity.this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
