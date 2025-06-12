package com.example.medicalmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientBillsHistoryActivity extends AppCompatActivity {

    TextView billsHistoryTextView;

    DatabaseReference databaseReference;
    String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_bills_history);

        billsHistoryTextView = findViewById(R.id.billsHistoryTextView);

        patientId = getIntent().getStringExtra("patientId");
        if (patientId == null || patientId.isEmpty()) {
            Toast.makeText(this, "Error: Missing patient ID!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://medicalmanagementsystem-8a4a4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        loadBillsHistory();
    }

    private void loadBillsHistory() {
        billsHistoryTextView.setText("Loading your bills...");

        StringBuilder billsBuilder = new StringBuilder();

        databaseReference.child("Bills")
                .orderByChild("patientId").equalTo(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        billsBuilder.append("---- Your Bills ----\n");

                        for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                            String billId = billSnapshot.child("billId").getValue(String.class);
                            Double totalAmount = billSnapshot.child("totalAmount").getValue(Double.class);
                            String billDate = billSnapshot.child("billDate").getValue(String.class);
                            String appointmentId = billSnapshot.child("appointmentId").getValue(String.class);
                            String doctorId = billSnapshot.child("doctorId").getValue(String.class);

                            billsBuilder.append("Bill ID: ").append(billId).append("\n")
                                    .append("Date: ").append(billDate).append("\n")
                                    .append("Appointment ID: ").append(appointmentId).append("\n")
                                    .append("Doctor ID: ").append(doctorId).append("\n")
                                    .append("Total Amount: ").append(totalAmount).append("\n\n");
                        }

                        billsHistoryTextView.setText(billsBuilder.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("PatientBillsHistory", "Error loading bills", error.toException());
                    }
                });
    }
}
