package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MedicineData extends AppCompatActivity {

    AutoCompleteTextView medicineName;
    RecyclerView medList;
    List<String> medicineList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_data);
        context = this;
        medicineName = findViewById(R.id.MedicineSearchBar);
        FirstMedDatabase db = new FirstMedDatabase(this);
        medicineList = db.getMedicineList();
        medList = findViewById(R.id.MedicineList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(medList.getContext(),layoutManager.getOrientation());
        medList.addItemDecoration(decoration);
        medList.setLayoutManager(layoutManager);
        medList.setAdapter(new MedicineListAdapter(medicineList,this));

        medicineName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String abc = s.toString();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filter(abc);
                    }
                },500);
            }
        });

    }

    void filter(String abc)
    {
        List<String> temp = new ArrayList<>();
        for (int i=0;i< medicineList.size();i++)
        {
            if (medicineList.get(i).contains(abc))
            {
                temp.add(medicineList.get(i));
            }
        }
        medList.setAdapter(new MedicineListAdapter(temp,this));
    }


    public void close(View view) {
        finish();
    }

    public void sarchBtn(View view) {
    }

    public void addMedicine(View view) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Add Medicine");
        View alertView = getLayoutInflater().inflate(R.layout.alert_layout,null);
        final TextInputEditText name = alertView.findViewById(R.id.amountTEXT);
        name.setHint("Medicine Name");
        builder.setView(alertView);
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirstMedDatabase db = new FirstMedDatabase(context);
                db.addMedList(name.getText().toString());
                medicineList = db.getMedicineList();
                medList.setAdapter(new MedicineListAdapter(medicineList,context));
            }
        });
        builder.show();
    }
}