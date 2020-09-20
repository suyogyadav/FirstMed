package com.kernel.firstmed;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicineFragment extends Fragment {

    private List<MedicinePOJO> medicines;
    private List<String> dateList;
    private HashMap<String,List<String>> medecineitems;
    public MedicineFragment() {
    }
    public MedicineFragment(List<MedicinePOJO> medicines) {
        this.medicines=medicines;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateList = new ArrayList<>();
        medecineitems = new HashMap<>();
        setdata();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine, container, false);
        ExpandableListView listView = view.findViewById(R.id.expandable_meds);
        ExpandableListAdapter adapter = new ExpandableListAdapter(getContext(),dateList,medecineitems,true);
        listView.setAdapter(adapter);
        return view;
    }

    private void setdata()
    {
        for (int i=0;i<medicines.size();i++)
        {
            dateList.add(medicines.get(i).getDate());
            medecineitems.put(medicines.get(i).getDate(),medicines.get(i).getOld_med());
        }

    }
}