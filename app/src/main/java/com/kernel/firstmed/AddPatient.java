package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddPatient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
    }

    public void close(View view) {
        finish();
    }

    public void submitBtn(View view) {
        startActivity(new Intent(this,PatientDetails.class));
    }
}