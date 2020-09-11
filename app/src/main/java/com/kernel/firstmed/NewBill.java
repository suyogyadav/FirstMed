package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class NewBill extends AppCompatActivity {

    private AutoCompleteTextView dropdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);
        String[] droplist = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};
        ArrayAdapter<String> dropadapter = new ArrayAdapter<>(this,R.layout.drop_down_layout,droplist);
        dropdown = findViewById(R.id.dropdown);
        dropdown.setAdapter(dropadapter);
    }

    public void close(View view) {
        finish();
    }
}