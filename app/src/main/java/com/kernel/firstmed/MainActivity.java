package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SearchPatient(View view) {
        startActivity(new Intent(this, SearchPatient.class));
    }

    public void AddPatient(View view) {
        startActivity(new Intent(this, AddPatient.class));
    }

    public void Prescription(View view) {
        startActivity(new Intent(this, PrescriptionActivity.class));
    }

    public void Medicine(View view) {
        startActivity(new Intent(this,MedicineData.class));
    }

    public void BackupRestore(View view) {
        startActivity(new Intent(this,BackupRestore.class));
    }
}