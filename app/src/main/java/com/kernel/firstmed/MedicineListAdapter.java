package com.kernel.firstmed;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
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

    public class MedicineListViewHolder extends RecyclerView.ViewHolder
    {
        MaterialTextView MedicineName;
        ImageButton deletebtn;
        ImageButton editbtn;
        public MedicineListViewHolder(@NonNull View itemView) {
            super(itemView);
            MedicineName = itemView.findViewById(R.id.MedicineName);
            editbtn = itemView.findViewById(R.id.editbtn);
            deletebtn = itemView.findViewById(R.id.deletebtn);

            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                    builder.setTitle("Edit Medicine");
                    View alertView = View.inflate(context,R.layout.alert_layout,null);
                    final TextInputEditText text = alertView.findViewById(R.id.amountTEXT);
                    text.setText(medicineList.get(getAdapterPosition()));
                    text.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    text.requestFocus();
                    builder.setView(alertView);
                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirstMedDatabase db = new FirstMedDatabase(context);
                            db.updateMedicineName(medicineList.get(getAdapterPosition()),text.getText().toString());
                            medicineList.set(getAdapterPosition(),text.getText().toString());
                            notifyDataSetChanged();
                        }
                    });
                    builder.show();
                }
            });

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirstMedDatabase db = new FirstMedDatabase(context);
                    db.deleteMedicine(medicineList.get(getAdapterPosition()));
                    new MedicineListAdapter(db.getMedicineList(),context);
                    medicineList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

        }
    }
}
