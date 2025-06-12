package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GenerateBillActivity extends AppCompatActivity {

    Spinner appointmentSpinner;
    EditText consultationFeeEditText, medicineChargesEditText, otherChargesEditText;
    Button generateBillBtn;

    DatabaseReference databaseReference;
    String doctorId;

    ArrayList<String> appointmentList = new ArrayList<>();
    ArrayList<String> appointmentIds = new ArrayList<>();
    ArrayList<String> patientIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_bill);

        appointmentSpinner = findViewById(R.id.appointmentSpinner);
        consultationFeeEditText = findViewById(R.id.consultationFeeEditText);
        medicineChargesEditText = findViewById(R.id.medicineChargesEditText);
        otherChargesEditText = findViewById(R.id.otherChargesEditText);
        generateBillBtn = findViewById(R.id.generateBillBtn);

        doctorId = getIntent().getStringExtra("doctorId");
        if (doctorId == null || doctorId.isEmpty()) {
            Toast.makeText(this, "Error: Missing doctor ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        loadAppointments();

        generateBillBtn.setOnClickListener(v -> generateBill());
    }

    private void loadAppointments() {
        databaseReference.child("Appointments")
                .orderByChild("doctorId").equalTo(doctorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        appointmentList.clear();
                        appointmentIds.clear();
                        patientIds.clear();

                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            String appointmentId = appointmentSnapshot.child("appointmentId").getValue(String.class);
                            String appointmentDate = appointmentSnapshot.child("appointmentDate").getValue(String.class);
                            String patientId = appointmentSnapshot.child("patientId").getValue(String.class);

                            // Build display string
                            String display = "Appointment ID: " + appointmentId + "\nDate: " + appointmentDate + "\nPatient ID: " + patientId;

                            appointmentList.add(display);
                            appointmentIds.add(appointmentId);
                            patientIds.add(patientId);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                GenerateBillActivity.this,
                                android.R.layout.simple_spinner_item,
                                appointmentList
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        appointmentSpinner.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(GenerateBillActivity.this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                        Log.e("GenerateBill", "DatabaseError", error.toException());
                    }
                });
    }

    private void generateBill() {
        int selectedIndex = appointmentSpinner.getSelectedItemPosition();
        if (selectedIndex < 0 || selectedIndex >= appointmentIds.size()) {
            Toast.makeText(this, "Please select an appointment", Toast.LENGTH_SHORT).show();
            return;
        }

        String appointmentId = appointmentIds.get(selectedIndex);
        String patientId = patientIds.get(selectedIndex);

        String consultationFeeStr = consultationFeeEditText.getText().toString().trim();
        String medicineChargesStr = medicineChargesEditText.getText().toString().trim();
        String otherChargesStr = otherChargesEditText.getText().toString().trim();

        if (consultationFeeStr.isEmpty() || medicineChargesStr.isEmpty() || otherChargesStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double consultationFee = Double.parseDouble(consultationFeeStr);
        double medicineCharges = Double.parseDouble(medicineChargesStr);
        double otherCharges = Double.parseDouble(otherChargesStr);
        double totalAmount = consultationFee + medicineCharges + otherCharges;

        String billId = UUID.randomUUID().toString();

        String billDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Map<String, Object> billData = new HashMap<>();
        billData.put("billId", billId);
        billData.put("doctorId", doctorId);
        billData.put("patientId", patientId);
        billData.put("appointmentId", appointmentId);
        billData.put("consultationFee", consultationFee);
        billData.put("medicineCharges", medicineCharges);
        billData.put("otherCharges", otherCharges);
        billData.put("totalAmount", totalAmount);
        billData.put("billDate", billDate);

        databaseReference.child("Bills").child(billId).setValue(billData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Bill generated successfully!", Toast.LENGTH_LONG).show();
                    finish(); // Return to DoctorHome
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to generate bill", Toast.LENGTH_SHORT).show();
                    Log.e("GenerateBill", "Error saving bill", e);
                });
    }
}
