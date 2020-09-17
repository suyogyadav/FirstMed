package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Collections;
import java.util.List;

public class PatientDetails extends AppCompatActivity {

    private MaterialTextView pName;
    private MaterialTextView pAge;
    private MaterialTextView pLastDate;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        pName = findViewById(R.id.pname);
        pAge = findViewById(R.id.Page);
        pLastDate = findViewById(R.id.PlastDate);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        long rowId = getIntent().getLongExtra("rowId", 0);
        boolean isNew = getIntent().getBooleanExtra("isNew", false);
        if (rowId != 0 && isNew) {
            newPatient(rowId);
        }
        if (rowId != 0 && !isNew) {
            oldPatient(rowId);
        }
    }

    private void newPatient(long rowId) {
        FirstMedDatabase db = new FirstMedDatabase(this);
        PatientPOJO patient = db.getSinglePatient(rowId);
        pName.setText(patient.getName());
        pAge.setText("Age : " + patient.getAge());
        pLastDate.setText(patient.getDate());
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        pagerAdapter.addFragments(new NoRecordFragment(), "MEDICINE");
        pagerAdapter.addFragments(new NoRecordFragment(), "DISEASE");
        pagerAdapter.addFragments(new NoRecordFragment(), "DEBT");
        viewPager.setAdapter(pagerAdapter);
    }

    private void oldPatient(long rowId) {
        FirstMedDatabase db = new FirstMedDatabase(this);
        PatientPOJO patient = db.getSinglePatient(rowId);
        List<MedicinePOJO> medicines = db.getMedicine(rowId);
        Collections.reverse(medicines);
        int dept = db.getDebt(rowId);
        List<DebtPojo> debtHistory = db.getDebtHistory(rowId);

        pName.setText(patient.getName());
        pAge.setText("Age : " + patient.getAge());
        pLastDate.setText(patient.getDate());

        if (!medicines.isEmpty()) {
            tabLayout.setupWithViewPager(viewPager);
            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
            pagerAdapter.addFragments(new MedicineFragment(medicines), "MEDICINE");
            pagerAdapter.addFragments(new DiseaseFragment(medicines), "DISEASE");
            pagerAdapter.addFragments(new DebtFragment(dept,debtHistory), "DEBT");
            viewPager.setAdapter(pagerAdapter);
        }
        else {
            tabLayout.setupWithViewPager(viewPager);
            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
            pagerAdapter.addFragments(new NoRecordFragment(), "MEDICINE");
            pagerAdapter.addFragments(new NoRecordFragment(), "DISEASE");
            pagerAdapter.addFragments(new NoRecordFragment(), "DEBT");
            viewPager.setAdapter(pagerAdapter);
        }

    }

    public void close(View view) {
        finish();
    }

    public void NewBill(View view) {
        Intent intent = new Intent(this, NewBill.class);
        intent.putExtra("rowId", getIntent().getLongExtra("rowId", 0));
        startActivity(intent);
    }
}