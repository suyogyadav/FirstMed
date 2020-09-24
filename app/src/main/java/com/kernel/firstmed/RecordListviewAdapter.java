package com.kernel.firstmed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
import java.util.List;

class RecordListviewAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> monthlist;
    HashMap<String, List<String>> daylist;

    RecordListviewAdapter(Context context, List<String> monthlist, HashMap<String, List<String>> daylist) {
        this.context = context;
        this.monthlist = monthlist;
        this.daylist = daylist;
    }

    @Override
    public int getGroupCount() {
        return monthlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return daylist.get(monthlist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return monthlist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return daylist.get(monthlist.get(groupPosition)).get(childPosition);
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
        String month = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_parent, null);
        }
        MaterialTextView dateView = convertView.findViewById(R.id.expandable_date);
        dateView.setText(month);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String day = (String) getChild(groupPosition,childPosition);
        String datetext = day.split("_n")[0];
        String counttext = day.split("_n")[1];

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.record_item, null);
        }
        MaterialTextView date = convertView.findViewById(R.id.date);
        MaterialTextView count = convertView.findViewById(R.id.count);
        date.setText(datetext);
        count.setText(counttext);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
