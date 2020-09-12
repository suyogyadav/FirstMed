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
        FirstMedDatabase db = new FirstMedDatabase(this);
        db.addPatient("Suyog","22","MALE");
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
}