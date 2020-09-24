package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private BarChart barChart;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        context = this;
        barChart = findViewById(R.id.barChart);
        AutoCompleteTextView textView = findViewById(R.id.dropdown_text);
        FirstMedDatabase db = new FirstMedDatabase(this);
        String[] timeline = {"Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menu_popup, timeline);
        textView.setAdapter(adapter);
        textView.setText("Weekly", false);
        List<ChartData> chartData = db.getChartData("Day");
        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < chartData.size(); i++) {
            barEntries.add(new BarEntry(Float.parseFloat(chartData.get(i).getDate()), Float.parseFloat(chartData.get(i).getCount())));
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "Patients");
        dataSet.setColor(Color.rgb(237, 28, 36));
        BarData barData = new BarData(dataSet);
        barChart.animateY(500);
        barChart.setData(barData);
        barChart.setFitBars(true);
        Description description = new Description();
        description.setText("Patients Per Day");
        barChart.setDescription(description);
        barChart.invalidate();

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setData("Day");
                        break;

                    case 1:
                        setData("Month");
                        break;
                    case 2:
                        setData("Year");
                        break;
                }
            }
        });

    }

    private void setData(String time) {
        FirstMedDatabase db = new FirstMedDatabase(context);
        List<ChartData> chartData = db.getChartData(time);
        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < chartData.size(); i++) {
            barEntries.add(new BarEntry(Float.parseFloat(chartData.get(i).getDate()), Float.parseFloat(chartData.get(i).getCount())));
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "Patients");
        dataSet.setColor(Color.rgb(237, 28, 36));
        BarData barData = new BarData(dataSet);
        barChart.animateY(500);
        barChart.setData(barData);
        barChart.setFitBars(true);
        Description description = new Description();
        description.setText("Patients Per " + time);
        barChart.setDescription(description);
        barChart.invalidate();
    }

    public void close(View view) {
        finish();
    }

    public void showHistory(View view) {

    }
}