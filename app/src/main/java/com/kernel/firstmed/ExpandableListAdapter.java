package com.kernel.firstmed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;


import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.List;

class ExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> datelist;
    HashMap<String, List<String>> medlist;
    boolean med;

    public ExpandableListAdapter(Context context, List<String> datelist, HashMap<String, List<String>> medlist,boolean med) {
        this.context = context;
        this.datelist = datelist;
        this.medlist = medlist;
        this.med = med;
    }

    @Override
    public int getGroupCount() {
        return datelist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.medlist.get(datelist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datelist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return medlist.get(datelist.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String date = (String) getGroup(groupPosition);
        date = getDateFormated(date);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_parent, null);
        }
        MaterialTextView dateView = convertView.findViewById(R.id.expandable_date);
        dateView.setText(date);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (med) {
            String MedicineName = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.med_layout, parent, false);
            }

            String[] medSplit = MedicineName.split("_n");
            MaterialTextView medname = convertView.findViewById(R.id.medname);
            MaterialTextView medqty = convertView.findViewById(R.id.medqty);
            MaterialTextView medtime = convertView.findViewById(R.id.medtime);

            medname.setText(medSplit[0]);
            medqty.setText(medSplit[1]);
            medtime.setText(medSplit[2]);
        }
        else {
            String MedicineName = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.disease_name_layout, parent, false);
            }

            MaterialTextView disname = convertView.findViewById(R.id.dis_name);
            disname.setText(MedicineName);
        }
        return convertView;
    }

    private String getDateFormated(String date)
    {
        String[] splitDate = date.split("-");
        switch (Integer.parseInt(splitDate[1]))
        {
            case 1:
                date = splitDate[2]+" January "+splitDate[0];
                break;
            case 2:
                date = splitDate[2]+" February "+splitDate[0];
                break;
            case 3:
                date = splitDate[2]+" March "+splitDate[0];
                break;
            case 4:
                date = splitDate[2]+" April "+splitDate[0];
                break;
            case 5:
                date = splitDate[2]+" May "+splitDate[0];
                break;
            case 6:
                date = splitDate[2]+" June "+splitDate[0];
                break;
            case 7:
                date = splitDate[2]+" July "+splitDate[0];
                break;
            case 8:
                date = splitDate[2]+" August "+splitDate[0];
                break;
            case 9:
                date = splitDate[2]+" September "+splitDate[0];
                break;
            case 10:
                date = splitDate[2]+" October "+splitDate[0];
                break;
            case 11:
                date = splitDate[2]+" November "+splitDate[0];
                break;
            case 12:
                date = splitDate[2]+" December "+splitDate[0];
                break;
        }
        return date;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
