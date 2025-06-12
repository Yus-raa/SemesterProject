package com.example.medicalmanagementsystem;

public class Doctor {

    private String id;
    private String name;
    private String email;
    private String password;
    private String specialization;

    // Required empty constructor for Firebase
    public Doctor() {
    }

    // Full constructor
    public Doctor(String id, String name, String email, String password, String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.specialization = specialization;
    }

    // Getter and Setter methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}

