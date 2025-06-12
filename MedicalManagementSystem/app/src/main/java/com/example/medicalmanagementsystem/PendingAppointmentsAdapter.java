package com.example.medicalmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendingAppointmentsAdapter extends RecyclerView.Adapter<PendingAppointmentsAdapter.AppointmentViewHolder> {

    private List<Appointment> pendingAppointmentsList;

    public PendingAppointmentsAdapter(List<Appointment> pendingAppointmentsList) {
        this.pendingAppointmentsList = pendingAppointmentsList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = pendingAppointmentsList.get(position);
        holder.patientIdTextView.setText("Patient ID: " + appointment.getPatientId());
        holder.appointmentDateTextView.setText("Date: " + appointment.getAppointmentDate());
        holder.reasonTextView.setText("Reason: " + appointment.getReason());
    }

    @Override
    public int getItemCount() {
        return pendingAppointmentsList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView patientIdTextView, appointmentDateTextView, appointmentTimeTextView, reasonTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            patientIdTextView = itemView.findViewById(R.id.patientIdTextView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
            appointmentTimeTextView = itemView.findViewById(R.id.appointmentTimeTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
        }
    }
}
