package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        barChart = findViewById(R.id.barChart);
        FirstMedDatabase db = new FirstMedDatabase(this);
        List<ChartData> chartData = db.getChartData();
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i=0;i<chartData.size();i++)
        {
          barEntries.add(new BarEntry(Integer.parseInt(chartData.get(i).getDate()),Integer.parseInt(chartData.get(i).getCount())));
        }
        BarDataSet dataSet = new BarDataSet(barEntries,"Patients");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);
        barChart.animateY(500);
        barChart.setData(barData);
        barChart.setFitBars(true);

        Description description = new Description();
        description.setText("Patients Per Day");
        barChart.setDescription(description);
        barChart.invalidate();

    }

    public void close(View view) {
        finish();
    }
}