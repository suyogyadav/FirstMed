package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.yearlist);
        FirstMedDatabase db = new FirstMedDatabase(this);
        List<ChartData>data = db.getChartData("Year");
        Collections.reverse(data);
        GridLayoutManager manager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new YearAdapter(data,this));
    }

    public void close(View view) {
        finish();
    }
}