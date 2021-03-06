package com.kernel.firstmed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiseaseFragment extends Fragment {

    private List<MedicinePOJO> medicines;
    private List<String> dateList;
    private HashMap<String,List<String>> medecineitems;

    public DiseaseFragment() {
        // Required empty public constructor
    }

    public  DiseaseFragment(List<MedicinePOJO> medicines)
    {
        this.medicines = medicines;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateList = new ArrayList<>();
        medecineitems = new HashMap<>();
        setdata();
    }

    private void setdata() {

        for (int i=0;i<medicines.size();i++)
        {
            dateList.add(medicines.get(i).getDate());
            List<String> dis = new ArrayList<>();
            dis.add(medicines.get(i).getOld_des());
            medecineitems.put(medicines.get(i).getDate(),dis);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease, container, false);
        ExpandableListView listView = view.findViewById(R.id.expandable_dis);
        ExpandableListAdapter adapter = new ExpandableListAdapter(getContext(),dateList,medecineitems,false);
        listView.setAdapter(adapter);
        return view;
    }
}