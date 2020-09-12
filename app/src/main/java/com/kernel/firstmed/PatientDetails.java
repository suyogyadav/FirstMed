package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

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
        } else {
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
        pagerAdapter.addFragments(new NoRecordFragment(),"MEDICINE");
        pagerAdapter.addFragments(new NoRecordFragment(),"DISEASE");
        pagerAdapter.addFragments(new NoRecordFragment(),"DEPT");
        viewPager.setAdapter(pagerAdapter);
    }

    private void oldPatient(long rowId) {

    }

    public void close(View view) {
        finish();
    }

    public void NewBill(View view) {
        Intent intent = new Intent(this,NewBill.class);
        intent.putExtra("rowId",getIntent().getLongExtra("rowId",0));
        startActivity(intent);
    }
}