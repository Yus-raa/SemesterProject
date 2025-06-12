package com.example.medicalmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TreatmentHistoryAdapter extends RecyclerView.Adapter<TreatmentHistoryAdapter.ViewHolder> {

    private List<TreatmentHistoryItem> historyList;

    public TreatmentHistoryAdapter(List<TreatmentHistoryItem> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public TreatmentHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_treatment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreatmentHistoryAdapter.ViewHolder holder, int position) {
        TreatmentHistoryItem item = historyList.get(position);

        holder.appointmentDateTextView.setText("Date: " + item.getAppointmentDate());
        holder.doctorNameTextView.setText("Doctor: " + item.getDoctorName());
        holder.diseaseTextView.setText("Disease: " + item.getDisease());
        holder.prescriptionTextView.setText("Prescription: " + item.getPrescription());
        holder.progressTextView.setText("Progress: " + item.getProgress());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentDateTextView, doctorNameTextView, diseaseTextView, prescriptionTextView, progressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            diseaseTextView = itemView.findViewById(R.id.diseaseTextView);
            prescriptionTextView = itemView.findViewById(R.id.prescriptionTextView);
            progressTextView = itemView.findViewById(R.id.progressTextView);
        }
    }
}
