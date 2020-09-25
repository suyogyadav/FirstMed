package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RecordActivity extends AppCompatActivity {

    List<String> monthnames;
    HashMap<String,List<String>> dayitems;
    String Year = getDateTime().split("-")[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        monthnames = new ArrayList<>();
        dayitems = new HashMap<>();
        setData();
        Year = getIntent().getStringExtra("Year");
        ExpandableListView listView = findViewById(R.id.recordListView);
        listView.setAdapter(new RecordListviewAdapter(this,monthnames,dayitems));
    }

    private void setData()
    {
        FirstMedDatabase db = new FirstMedDatabase(this);
        List<ChartData> monthdata = db.getChartData("Month",Year);
        for (int i = 0;i<monthdata.size();i++)
        {
            monthnames.add(monthdata.get(i).getDate());
            List<ChartData> daydata = db.getDayData(monthdata.get(i).getDate(),Year);
            List<String> days = new ArrayList<>();
            for (int j=0;j<daydata.size();j++) {
                days.add(daydata.get(j).getDate()+"_n"+daydata.get(j).getCount());
            }
            dayitems.put(monthdata.get(i).getDate(),days);
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date).split(" ")[0];
    }

    public void close(View view) {
        finish();
    }
}