package com.example.medicalmanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryUpdateAdapter extends RecyclerView.Adapter<HistoryUpdateAdapter.HistoryViewHolder> {

    Context context;
    ArrayList<Appointment> appointmentList;
    DatabaseReference databaseReference;

    public HistoryUpdateAdapter(Context context, ArrayList<Appointment> appointmentList, DatabaseReference databaseReference) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_update, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.patientNameTextView.setText("Patient ID: " + appointment.getPatientId());
        holder.appointmentDateTextView.setText("Date: " + appointment.getAppointmentDate());
        holder.diseaseEditText.setText(appointment.getDisease());
        holder.prescriptionEditText.setText(appointment.getPrescription());
        holder.progressEditText.setText(appointment.getProgress());

        holder.updateButton.setOnClickListener(v -> {
            String disease = holder.diseaseEditText.getText().toString().trim();
            String prescription = holder.prescriptionEditText.getText().toString().trim();
            String progress = holder.progressEditText.getText().toString().trim();

            Map<String, Object> updates = new HashMap<>();
            updates.put("disease", disease);
            updates.put("prescription", prescription);
            updates.put("progress", progress);

            databaseReference.child("Appointments").child(appointment.getAppointmentId()).updateChildren(updates)
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Updated successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to update!", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView patientNameTextView, appointmentDateTextView;
        EditText diseaseEditText, prescriptionEditText, progressEditText;
        Button updateButton;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
            diseaseEditText = itemView.findViewById(R.id.diseaseEditText);
            prescriptionEditText = itemView.findViewById(R.id.prescriptionEditText);
            progressEditText = itemView.findViewById(R.id.progressEditText);
            updateButton = itemView.findViewById(R.id.updateButton);
        }
    }
}
