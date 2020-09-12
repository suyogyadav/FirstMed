package com.kernel.firstmed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecentAdapter extends RecyclerView.Adapter {

    List<PatientPOJO> patients;

    RecentAdapter(List<PatientPOJO> patients) {
        this.patients = patients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recent_patient,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListViewHolder listViewHolder = (ListViewHolder) holder;
        listViewHolder.pname.setText(patients.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView pname;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            pname = itemView.findViewById(R.id.pnamelist);
        }
    }
}
