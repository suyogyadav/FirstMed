package com.kernel.firstmed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PatientPOJO> patients;
    Context ctx;

    RecentAdapter(List<PatientPOJO> patients, Context context) {
        this.patients = patients;
        ctx = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recent_patient, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListViewHolder listViewHolder = (ListViewHolder) holder;
        listViewHolder.pname.setText(patients.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pname;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            pname = itemView.findViewById(R.id.pnamelist);
        }

        @Override
        public void onClick(View v) {
            Log.i("ABCD","Inside OnClick");
            Intent intent = new Intent(ctx, PatientDetails.class);
            intent.putExtra("rowId", (long) (patients.get(getAdapterPosition()).id));
            intent.putExtra("isNew", false);
            ctx.startActivity(intent);
        }
    }
}
