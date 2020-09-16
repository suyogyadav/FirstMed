package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
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
        //showAlert();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void showAlert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Payment Method");
        builder.show();
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