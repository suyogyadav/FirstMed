package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class AddPatient extends AppCompatActivity {

    private TextInputEditText nameText;
    private TextInputEditText ageText;
    private SwitchCompat genderSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        nameText = findViewById(R.id.pnameedttxt);
        ageText = findViewById(R.id.ageedttxt);
        genderSwitch = findViewById(R.id.genderswitch);
//        int[][] states = new int[][] {
//                new int[] { android.R.attr.state_checked}, // enabled
//                new int[] {-android.R.attr.state_checked}, // unchecked
//        };
//        int[] colors = new int[] {
//                R.color.colorPrimaryDark,
//                R.color.colorWhite
//        };
//        genderSwitch.setTrackTintList(new ColorStateList(states,colors));
    }

    public void close(View view) {
        finish();
    }

    public void submitBtn(View view) {
        FirstMedDatabase db = new FirstMedDatabase(this);
        String Name = nameText.getText().toString();
        String Age = ageText.getText().toString();
        String Gender = "Male";
        if (!genderSwitch.isChecked()) {

            Gender = "Female";
        }
        long rowId = db.addPatient(Name, Age, Gender);
        Intent intent = new Intent(this, PatientDetails.class);
        intent.putExtra("rowId", rowId);
        intent.putExtra("isNew",true);
        startActivity(intent);
    }
}