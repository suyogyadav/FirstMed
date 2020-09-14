package com.kernel.firstmed;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.HashMap;
import java.util.List;

class ExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> datelist;
    HashMap<String,List<String>> medlist;

    public  ExpandableListAdapter( Context context, List<String> datelist, HashMap<String,List<String>> medlist)
    {
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
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
