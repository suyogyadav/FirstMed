package com.kernel.firstmed;

import android.content.Context;
import android.graphics.Color;
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
        viewHolder.date.setText(getDateFormated(transhis.get(position).getDate()));
        if (transhis.get(position).getTrans().equals("Added"))
        {
            viewHolder.amount.setText("+ "+transhis.get(position).getDebt());
            viewHolder.amount.setTextColor(Color.parseColor("#3DB311"));
        }else {
            viewHolder.amount.setText("- "+transhis.get(position).getDebt());
            viewHolder.amount.setTextColor(Color.parseColor("#ED1C24"));
        }
    }

    @Override
    public int getItemCount() {
        return transhis.size();
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
