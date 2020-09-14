package com.kernel.firstmed;

import android.content.Context;
import android.util.Log;
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

    public ExpandableListAdapter(Context context, List<String> datelist, HashMap<String, List<String>> medlist) {
        this.context = context;
        this.datelist = datelist;
        this.medlist = medlist;
    }

    @Override
    public int getGroupCount() {
        return datelist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return medlist.get(datelist.get(groupPosition)).size();
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
        String MedicineName = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.med_layout, parent);
        String[] medSplit = MedicineName.split("_n");
        MaterialTextView medname = convertView.findViewById(R.id.medname);
        MaterialTextView medqty = convertView.findViewById(R.id.medqty);
        MaterialTextView medtime = convertView.findViewById(R.id.medtime);
        medname.setText(medSplit[0]);
        medqty.setText(medSplit[1]);
        medtime.setText(medSplit[2]);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
