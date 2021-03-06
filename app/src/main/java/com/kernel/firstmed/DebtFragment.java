package com.kernel.firstmed;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class DebtFragment extends Fragment {

    private int debt;
    private List<DebtPojo> debtHistory;
    private Context context;
    long RowId;

    public DebtFragment() {
        // Required empty public constructor
    }

    public DebtFragment(long rowId,Context context) {
        this.context = context;
        RowId = rowId;
        FirstMedDatabase db = new FirstMedDatabase(context);
        debt = db.getDebt(rowId);
        debtHistory = db.getDebtHistory(rowId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debt, null);
        final MaterialTextView balance = view.findViewById(R.id.balanceAmount);
        final MaterialButton addButton = view.findViewById(R.id.amtaddbtn);
        final MaterialButton deductButton = view.findViewById(R.id.amtdeductbtn);
        final TrasHistoryAdapter adapter = new TrasHistoryAdapter(getContext(), debtHistory);
        final RecyclerView recyclerView = view.findViewById(R.id.transhis);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        balance.setText("" + debt);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Add Amount");
                View alertLayout = getLayoutInflater().inflate(R.layout.alert_layout, null);
                final TextInputEditText amount = alertLayout.findViewById(R.id.amountTEXT);
                amount.setHint("Amount");
                builder.setView(alertLayout);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirstMedDatabase db = new FirstMedDatabase(getContext());
                        db.addDebt(RowId, Integer.parseInt(amount.getText().toString()));
                        balance.setText(""+db.getDebt(RowId));
                        List<DebtPojo> newlist = db.getDebtHistory(RowId);
                        TrasHistoryAdapter newadapter = new TrasHistoryAdapter(getContext(), newlist);
                        recyclerView.setAdapter(newadapter);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });

        deductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Deduct Amount");
                View alertLayout = getLayoutInflater().inflate(R.layout.alert_layout, null);
                final TextInputEditText amount = alertLayout.findViewById(R.id.amountTEXT);
                amount.setHint("Amount");
                builder.setView(alertLayout);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirstMedDatabase db = new FirstMedDatabase(getContext());
                        db.removeDebt(RowId, Integer.parseInt(amount.getText().toString()));
                        balance.setText(""+db.getDebt(RowId));
                        List<DebtPojo> newlist = db.getDebtHistory(RowId);
                        TrasHistoryAdapter newadapter = new TrasHistoryAdapter(getContext(), newlist);
                        recyclerView.setAdapter(newadapter);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });

        return view;
    }
}