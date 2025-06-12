package com.example.medicalmanagementsystem;

public class TreatmentHistoryItem {
    private String appointmentDate;
    private String doctorId;
    private String doctorName;
    private String disease;
    private String prescription;
    private String progress;

    public TreatmentHistoryItem() {
    }

    public TreatmentHistoryItem(String appointmentDate, String doctorId, String disease, String prescription, String progress) {
        this.appointmentDate = appointmentDate;
        this.doctorId = doctorId;
        this.disease = disease;
        this.prescription = prescription;
        this.progress = progress;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDisease() {
        return disease;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getProgress() {
        return progress;
    }
}
