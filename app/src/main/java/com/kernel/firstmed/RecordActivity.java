package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    List<String> monthnames;
    HashMap<String,List<String>> dayitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        monthnames = new ArrayList<>();
        dayitems = new HashMap<>();
        setData();

        ExpandableListView listView = findViewById(R.id.recordListView);
        listView.setAdapter(new RecordListviewAdapter(this,monthnames,dayitems));
    }

    private void setData()
    {
        FirstMedDatabase db = new FirstMedDatabase(this);
        List<ChartData> monthdata = db.getChartData("Month");
        for (int i = 0;i<monthdata.size();i++)
        {
            monthnames.add(monthdata.get(i).getDate());
            List<ChartData> daydata = db.getDayData(monthdata.get(i).getDate());
            List<String> days = new ArrayList<>();
            for (int j=0;j<daydata.size();j++) {
                days.add(daydata.get(j).getDate()+"_n"+daydata.get(j).getCount());
            }
            dayitems.put(monthdata.get(i).getDate(),days);
        }
    }

    public void close(View view) {
        finish();
    }
}