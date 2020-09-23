package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    MaterialTextView today;
    MaterialTextView month;
    MaterialTextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        today = findViewById(R.id.ta);
        month = findViewById(R.id.tb);
        total = findViewById(R.id.tc);
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

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date).split(" ")[0];
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirstMedDatabase db = new FirstMedDatabase(this);
        today.setText(""+db.getDayCount(getDateTime()));
        month.setText(""+db.getMonthCount(getDateTime()));
        total.setText(""+db.getPatientCount());
    }
}