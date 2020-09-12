package com.kernel.firstmed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

class MedListAdapter extends RecyclerView.Adapter {

    private ArrayList<String> MedList;
    public MedListAdapter(ArrayList<String> medList)
    {
        MedList = medList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.med_layout,parent,false);
        return new MedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MedsViewHolder medsViewHolder = (MedsViewHolder) holder;
        String[] med = MedList.get(position).split("_n");
        medsViewHolder.medName.setText(med[0]);
        medsViewHolder.medQty.setText(med[1]);
        medsViewHolder.medTime.setText(med[2]);
    }

    @Override
    public int getItemCount() {
        return MedList.size();
    }

    public class MedsViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView medName;
        private MaterialTextView medQty;
        private MaterialTextView medTime;

        public MedsViewHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.medname);
            medQty = itemView.findViewById(R.id.medqty);
            medTime = itemView.findViewById(R.id.medtime);
        }
    }
}
