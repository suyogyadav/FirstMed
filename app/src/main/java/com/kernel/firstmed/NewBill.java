package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.google.android.material.textfield.TextInputEditText;


import java.util.ArrayList;
import java.util.List;

public class NewBill extends AppCompatActivity {

    private TextInputEditText diseaseName;
    private TextInputEditText medicineName;
    private TextInputEditText qty;
    private RecyclerView medList;
    private ArrayList<String> meds;
    private MedListAdapter adapter;
    private Chip beforeDinner;
    private Chip afterDinner;
    private ChipGroup abc;
    private Chip morning;
    private Chip afternoon;
    private Chip night;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);
        diseaseName = findViewById(R.id.edtdes);
        medicineName = findViewById(R.id.edtmed);
        qty = findViewById(R.id.edtqty);
        medList = findViewById(R.id.medlist);
        beforeDinner = findViewById(R.id.beforedinner);
        afterDinner = findViewById(R.id.afterdinner);
        morning = findViewById(R.id.morning);
        afternoon = findViewById(R.id.Afternoon);
        night = findViewById(R.id.Night);
        abc = findViewById(R.id.timegrp);
        meds = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(medList.getContext(), layoutManager.getOrientation());
        medList.addItemDecoration(decoration);
        medList.setLayoutManager(layoutManager);
        adapter = new MedListAdapter(meds);
        medList.setAdapter(adapter);
        diseaseName.requestFocus();
    }

    public void AddMeds(View view) {
        String med = getmeds();
        meds.add(med);
        adapter.notifyDataSetChanged();
        medicineName.setText("");
        qty.setText("");
        beforeDinner.setChecked(false);
        afterDinner.setChecked(false);
        morning.setChecked(false);
        afternoon.setChecked(false);
        night.setChecked(false);
        medicineName.requestFocus();
    }

    public void saveAndPrint(View view) {
        FirstMedDatabase db = new FirstMedDatabase(this);
        db.addMedicine(meds, getIntent().getLongExtra("rowId", 0), diseaseName.getText().toString());
        showAlert();
        //startActivity(new Intent(this, MainActivity.class));
    }

    public void showAlert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Payment Method");
        View alertLayouyt = getLayoutInflater().inflate(R.layout.alert_dialoge_custom_layout, null);
        final TextInputEditText amount = alertLayouyt.findViewById(R.id.edtamount);
        final ChipGroup cashGroup = alertLayouyt.findViewById(R.id.cashchipgroup);
        builder.setView(alertLayouyt);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switchAction(cashGroup.getCheckedChipId(), Integer.parseInt(amount.getText().toString()));
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void switchAction(int id, int amount) {
        switch (id) {
            case R.id.cashswitch:
                Toast.makeText(this, "Printing In Process", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adddebtswitch:
                FirstMedDatabase db = new FirstMedDatabase(this);
                db.addDebt(getIntent().getLongExtra("rowId", 0), amount);
                Toast.makeText(this, "Added To Debt", Toast.LENGTH_SHORT).show();
                break;

            case R.id.removedebtswitch:
                FirstMedDatabase database = new FirstMedDatabase(this);
                database.removeDebt(getIntent().getLongExtra("rowId", 0), amount);
                Toast.makeText(this, "Deducted from Debt", Toast.LENGTH_SHORT).show();
                break;
        }
        finish();
    }

    public String getmeds() {
        String med = med = medicineName.getText().toString() + "_n" + qty.getText().toString() + "_n" + "";
        List<Integer> ids = abc.getCheckedChipIds();
        String abcd = "";
        for (int i = 0; i < ids.size(); i++) {
            if (beforeDinner.isChecked()) {
                abcd = abcd + ((Chip) findViewById(ids.get(i))).getText() + " Before Dinner";
            }
            if (afterDinner.isChecked()) {
                abcd = abcd + ((Chip) findViewById(ids.get(i))).getText() + " After Dinner";
            }
            abcd = abcd + "\n";
        }
        med = med + abcd;
        return med;
    }

    public void close(View view) {
        finish();
    }
}