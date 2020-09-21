package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchPatient extends AppCompatActivity {

    private AutoCompleteTextView searchText;
    private long rowId = 0;
    RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);
        FirstMedDatabase db = new FirstMedDatabase(this);
         listView = findViewById(R.id.recentlist);
        searchText = findViewById(R.id.searchbartxt);
        searchText.requestFocus();
        searchText.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getAutoFillHint()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(listView.getContext(), layoutManager.getOrientation());
        listView.addItemDecoration(decoration);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(new RecentAdapter(db.getPatient(), this));
    }

    private List<String> getAutoFillHint() {
        FirstMedDatabase db = new FirstMedDatabase(this);
        List<PatientPOJO> patients = db.getPatient();
        List<String> namelist = new ArrayList<String>();

        for (int i = 0; i < patients.size(); i++) {
            namelist.add(patients.get(i).getName());
        }
        return namelist;
    }

    public void close(View view) {
        finish();
    }

    public void sarchBtn(View view) {

        if (!searchText.getText().toString().equals("")) {
            if (patientSearch()) {
                Intent intent = new Intent(this, PatientDetails.class);
                intent.putExtra("rowId", rowId);
                intent.putExtra("isNew", false);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Patient Not Found", Toast.LENGTH_SHORT).show();
            }
        } else {
            searchText.requestFocus();
            Toast.makeText(this, "Search Should Not Be Empty", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean patientSearch() {
        FirstMedDatabase db = new FirstMedDatabase(this);
        List<PatientPOJO> patients = db.getPatient();
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getName().equals(searchText.getText().toString())) {
                rowId = patients.get(i).getId();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirstMedDatabase db = new FirstMedDatabase(this);
        listView.setAdapter(new RecentAdapter(db.getPatient(), this));
    }
}