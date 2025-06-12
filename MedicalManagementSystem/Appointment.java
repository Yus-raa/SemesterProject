public class Appointment {

    public String appointmentId;
    public String patientId;
    public String doctorId;
    public String date;
    public String time;
    public String status; // pending / approved / rejected / completed
    public String prescription;
    public String disease;
    public String progress;
    public String billAmount;

    public Appointment() {
        // Default constructor required for calls to DataSnapshot.getValue(Appointment.class)
    }

    public Appointment(String appointmentId, String patientId, String doctorId, String date, String time, String status,
                       String prescription, String disease, String progress, String billAmount) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.prescription = prescription;
        this.disease = disease;
        this.progress = progress;
        this.billAmount = billAmount;
    }
}

