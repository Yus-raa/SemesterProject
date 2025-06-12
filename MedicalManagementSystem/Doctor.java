public class Doctor {

    public String doctorId;
    public String name;
    public String specialization;
    public String phone;
    public String email;

    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(Doctor.class)
    }

    public Doctor(String doctorId, String name, String specialization, String phone, String email) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
    }
}
