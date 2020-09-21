package com.kernel.firstmed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineListViewHolder> {

    List<String> medicineList;
    Context context;

    MedicineListAdapter(List<String> medicineList,Context context)
    {
        this.medicineList = medicineList;
        this.context = context;
        Log.i("abcgd",""+this.medicineList.size());
    }

    @NonNull
    @Override
    public MedicineListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.medicine_list, parent, false);
        return new MedicineListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineListViewHolder holder, int position) {
        holder.MedicineName.setText(medicineList.get(position));
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MedicineListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        MaterialTextView MedicineName;
        ImageButton deletebtn;
        public MedicineListViewHolder(@NonNull View itemView) {
            super(itemView);
            MedicineName = itemView.findViewById(R.id.MedicineName);
            deletebtn = itemView.findViewById(R.id.deletebtn);
            deletebtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FirstMedDatabase db = new FirstMedDatabase(context);
            db.deleteMedicine(medicineList.get(getAdapterPosition()));
            new MedicineListAdapter(db.getMedicineList(),context);
            medicineList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }
    }
}
