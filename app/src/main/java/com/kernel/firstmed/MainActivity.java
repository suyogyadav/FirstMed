package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void SearchPatient(View view) {
     //   startActivity(new Intent(this, SearchPatient.class));
    }

    public void patientData(View view) {
       // startActivity(new Intent(this, PatientData.class));
    }

    public void AddPatient(View view) {
        //startActivity(new Intent(this, AddPatient.class));
    }
}