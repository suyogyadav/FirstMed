package com.kernel.firstmed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

class YearAdapter extends RecyclerView.Adapter<YearAdapter.YearViewHolder> {

    List<ChartData> yearList;
    Context context;

    YearAdapter(List<ChartData> yearList,Context context)
    {
        this.yearList = yearList;
        this.context = context;
    }

    @NonNull
    @Override
    public YearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_list_item, parent, false);
        return new YearViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull YearViewHolder holder, int position) {
        holder.year.setText(yearList.get(position).getDate());
        String countText = "Count : " + yearList.get(position).getCount();
        holder.count.setText(countText);
    }

    @Override
    public int getItemCount() {
        return yearList.size();
    }

    class YearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        MaterialCardView cardView;
        MaterialTextView year;
        MaterialTextView count;
        Context ctx;

        public YearViewHolder(@NonNull View itemView , Context ctx) {
            super(itemView);
            this.ctx = ctx;
            cardView  = itemView.findViewById(R.id.cardview);
            year = itemView.findViewById(R.id.yearText);
            count = itemView.findViewById(R.id.countText);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("shuru","called");
            Intent intent = new Intent(context,RecordActivity.class);
            intent.putExtra("Year",yearList.get(getAdapterPosition()).getDate());
            ctx.startActivity(intent);
        }
    }
}
