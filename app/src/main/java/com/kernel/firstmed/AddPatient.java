package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        if (!Name.equals("") && !Age.equals(""))
        {
            long rowId = db.addPatient(Name, Age, Gender);
            Intent intent = new Intent(this, PatientDetails.class);
            intent.putExtra("rowId", rowId);
            intent.putExtra("isNew", true);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Fill All The Fields",Toast.LENGTH_SHORT).show();
        }
    }
}