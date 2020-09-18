package com.kernel.firstmed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

class TrasHistoryAdapter extends RecyclerView.Adapter {

    List<DebtPojo> transhis;
    Context ctx;

    public TrasHistoryAdapter(Context ctx,List<DebtPojo> transhis)
    {
        this.ctx = ctx;
        this.transhis = transhis;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trans_history_layout, parent, false);
        return new TransViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TrasHistoryAdapter.TransViewHolder viewHolder = (TransViewHolder) holder;
        viewHolder.title.setText(transhis.get(position).getTrans());
        viewHolder.date.setText(transhis.get(position).getDate());
        viewHolder.amount.setText("+"+transhis.get(position).getDebt());
    }

    @Override
    public int getItemCount() {
        return transhis.size();
    }

    public class TransViewHolder extends RecyclerView.ViewHolder{

        MaterialTextView title;
        MaterialTextView date;
        MaterialTextView amount;

        public TransViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            date = itemView.findViewById(R.id.dateText);
            amount = itemView.findViewById(R.id.amountTxt);
        }
    }
}
