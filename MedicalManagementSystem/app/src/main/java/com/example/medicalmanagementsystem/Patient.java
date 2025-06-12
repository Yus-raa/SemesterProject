package com.example.medicalmanagementsystem;

public class Patient {
    private String id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String gender;
    private String dob;
    private String emergencyContactName;
    private String emergencyContactPhone;

    // Required empty constructor for Firebase
    public Patient() {
    }

    // Full constructor
    public Patient(String id, String fullName, String email, String password, String phone,
                   String gender, String dob, String emergencyContactName, String emergencyContactPhone) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
    }

    // Getter and Setter methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
}
