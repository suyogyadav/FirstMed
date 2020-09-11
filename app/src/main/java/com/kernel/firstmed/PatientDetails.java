package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PatientDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
    }

    public void close(View view) {
        finish();
    }

    public void NewBill(View view) {
        startActivity(new Intent(this,NewBill.class));
    }
}