package com.example.medicalmanagementsystem;

public class AppointmentDisplay {

    private String appointmentDate;
    private String patientName;
    private String reason;
    private String disease;
    private String progress;

    public AppointmentDisplay() {}

    public AppointmentDisplay(String appointmentDate, String patientName, String reason, String disease, String progress) {
        this.appointmentDate = appointmentDate;
        this.patientName = patientName;
        this.reason = reason;
        this.disease = disease;
        this.progress = progress;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getReason() {
        return reason;
    }

    public String getDisease() {
        return disease;
    }

    public String getProgress() {
        return progress;
    }
}
