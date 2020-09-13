package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

public class SearchPatient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);
        FirstMedDatabase db = new FirstMedDatabase(this);
        RecyclerView listView = findViewById(R.id.recentlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(listView.getContext(),layoutManager.getOrientation());
        listView.addItemDecoration(decoration);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(new RecentAdapter(db.getPatient(),this));
    }

    public void close(View view) {
        finish();
    }

    public void sarchBtn(View view) {
    }
}