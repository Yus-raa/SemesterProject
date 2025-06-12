package com.example.medicalmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodaysAppointmentsAdapter extends RecyclerView.Adapter<TodaysAppointmentsAdapter.ViewHolder> {

    private ArrayList<AppointmentDisplay> todaysAppointmentsList;

    public TodaysAppointmentsAdapter(ArrayList<AppointmentDisplay> todaysAppointmentsList) {
        this.todaysAppointmentsList = todaysAppointmentsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todays_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentDisplay item = todaysAppointmentsList.get(position);
        holder.patientNameTextView.setText("Patient: " + item.getPatientName());
        holder.appointmentDateTextView.setText("Date: " + item.getAppointmentDate());
        holder.reasonTextView.setText("Reason: " + item.getReason());
        holder.diseaseTextView.setText("Disease: " + item.getDisease());
        holder.progressTextView.setText("Progress: " + item.getProgress());
    }

    @Override
    public int getItemCount() {
        return todaysAppointmentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientNameTextView, appointmentDateTextView, reasonTextView, diseaseTextView, progressTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            diseaseTextView = itemView.findViewById(R.id.diseaseTextView);
            progressTextView = itemView.findViewById(R.id.progressTextView);
        }
    }
}
