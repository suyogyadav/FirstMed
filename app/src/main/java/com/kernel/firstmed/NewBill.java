package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.google.android.material.textfield.TextInputEditText;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewBill extends AppCompatActivity {

    private TextInputEditText diseaseName;
    private AutoCompleteTextView medicineName;
    private TextInputEditText qty;
    private RecyclerView medList;
    private ArrayList<String> meds;
    private MedListAdapter adapter;
    private Chip beforeDinner;
    private Chip afterDinner;
    private ChipGroup abc;
    private Chip morning;
    private Chip afternoon;
    private Chip night;
    private Context context;
    private WebView parentWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        FirstMedDatabase db = new FirstMedDatabase(this);
        setContentView(R.layout.activity_new_bill);
        parentWebView = new WebView(this);
        diseaseName = findViewById(R.id.edtdes);
        medicineName = findViewById(R.id.edtmed);
        qty = findViewById(R.id.edtqty);
        medList = findViewById(R.id.medlist);
        beforeDinner = findViewById(R.id.beforedinner);
        afterDinner = findViewById(R.id.afterdinner);
        morning = findViewById(R.id.morning);
        afternoon = findViewById(R.id.Afternoon);
        night = findViewById(R.id.Night);
        abc = findViewById(R.id.timegrp);
        meds = new ArrayList<>();
        medicineName.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,db.getMedicineList()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(medList.getContext(), layoutManager.getOrientation());
        medList.addItemDecoration(decoration);
        medList.setLayoutManager(layoutManager);
        adapter = new MedListAdapter(meds);
        medList.setAdapter(adapter);
        diseaseName.requestFocus();
    }

    public void AddMeds(View view) {
        if (diseaseName.getText().toString().equals("") ) {

            diseaseName.requestFocus();
            Toast.makeText(this, "Disease Cannot Be Empty", Toast.LENGTH_SHORT).show();
        }

        if (meds.isEmpty() && medicineName.getText().toString().equals("") && qty.getText().toString().equals("")) {
            medicineName.requestFocus();
            Toast.makeText(this, "Add At Least One Medicine with Qty", Toast.LENGTH_SHORT).show();
        }

        if (!diseaseName.getText().toString().equals("") && !medicineName.getText().toString().equals("") && !qty.getText().toString().equals(""))
        {
            FirstMedDatabase db = new FirstMedDatabase(this);
            db.addMedList(medicineName.getText().toString());
            String med = getmeds();
            meds.add(med);
            adapter.notifyDataSetChanged();

            medicineName.setText("");
            qty.setText("");
            beforeDinner.setChecked(false);
            afterDinner.setChecked(true);
            morning.setChecked(true);
            afternoon.setChecked(false);
            night.setChecked(false);
            medicineName.requestFocus();
        }
    }

    public void saveAndPrint(View view) {
        if (!meds.isEmpty()) {
            showAlert();
        }
        else {
            Toast.makeText(this,"No Medicines Added",Toast.LENGTH_SHORT).show();
        }
    }

    public void showAlert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Payment Method");
        View alertLayouyt = getLayoutInflater().inflate(R.layout.alert_dialoge_custom_layout, null);
        final TextInputEditText amount = alertLayouyt.findViewById(R.id.edtamount);
        amount.requestFocus();
        final ChipGroup cashGroup = alertLayouyt.findViewById(R.id.cashchipgroup);
        builder.setView(alertLayouyt);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (amount.getText().toString().equals(""))
                {
                    amount.requestFocus();
                    Toast.makeText(context, "Set Price", Toast.LENGTH_LONG).show();
                }
                else {
                    FirstMedDatabase db = new FirstMedDatabase(context);
                    db.addMedicine(meds, getIntent().getLongExtra("rowId", 0), diseaseName.getText().toString());
                    switchAction(cashGroup.getCheckedChipId(), Integer.parseInt(amount.getText().toString()));
                    db.addCount(getDateTime(),1);
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void switchAction(int id, int amount) {
        switch (id) {
            case R.id.cashswitch:
                Toast.makeText(this, "Printing In Process", Toast.LENGTH_SHORT).show();
                break;

            case R.id.adddebtswitch:
                FirstMedDatabase db = new FirstMedDatabase(this);
                db.addDebt(getIntent().getLongExtra("rowId", 0), amount);
                Toast.makeText(this, "Added To Debt", Toast.LENGTH_SHORT).show();
                break;

            case R.id.removedebtswitch:
                FirstMedDatabase database = new FirstMedDatabase(this);
                database.removeDebt(getIntent().getLongExtra("rowId", 0), amount);
                Toast.makeText(this, "Deducted from Debt", Toast.LENGTH_SHORT).show();
                break;
        }
        printReciept();
        startActivity(new Intent(this,MainActivity.class));
    }

    public String getmeds() {
        String med = medicineName.getText().toString() + "_n" + qty.getText().toString() + "_n" + "";
        List<Integer> ids = abc.getCheckedChipIds();
        String abcd = "";
        for (int i = 0; i < ids.size(); i++) {
            if (beforeDinner.isChecked()) {
                abcd = abcd + ((Chip) findViewById(ids.get(i))).getText() + " Before Dinner";
            }
            if (afterDinner.isChecked()) {
                abcd = abcd + ((Chip) findViewById(ids.get(i))).getText() + " After Dinner";
            }
            abcd = abcd + "\n";
        }
        med = med + abcd;
        return med;
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date).split(" ")[0];
    }

    private void printReciept()
    {
        long rowId = getIntent().getLongExtra("rowId", 0);
        WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
                parentWebView = null;
            }
        });
        FirstMedDatabase db = new FirstMedDatabase(this);
        List<MedicinePOJO> medicinePOJOS = db.getMedicine(rowId);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < medicinePOJOS.get(0).getOld_med().size(); i++) {
            builder.append("<tr>")
                    .append("<td style=\"width:45%\">")
                    .append((medicinePOJOS.get(0).getOld_med().get(i)).split("_n")[0])
                    .append("</td>")
                    .append("<td style=\"width:10%\">")
                    .append((medicinePOJOS.get(0).getOld_med().get(i)).split("_n")[1])
                    .append("</td>")
                    .append("<td style=\"width:45%\">")
                    .append((medicinePOJOS.get(0).getOld_med().get(i)).split("_n")[2])
                    .append("</td>")
                    .append("</tr>");
        }

        String beforname = "<table style=\"width:100%\"><tr><td style=\"width:70%\"><span style=\"font-weight: bold;\">Patient Name : </span>";
        String befordate = "</td><td style=\"width:30%\"><span style=\"font-weight: bold;\">Date : </span>";
        String afterdate = "</td></tr></table>";
        String TableHeader = "<tr><th>Medicine</th><th>Qty</th><th>Time</th></tr>";
        String footer = "<div style=\"position: absolute; bottom: 0; right: 0; width: 50%; text-align:right; font-weight: bold;\">Dr.Suyog Yadav</div>";
        String PatientName = db.getSinglePatient(medicinePOJOS.get(0).getPid()).getName();
        String Date = getDateTime();
        String htmlDocument = "<html><head><style>th, td {padding: 5px;text-align: left;}</style></head><body><h1>Dr. Suyog Yadav</h1><h3>MBBS</h3><p>550/5, East side Of S.T.Stand ,</br>M.P.Patil Hospital Road , Sangli</br>Phone - 8806035350</p><hr>"+beforname+PatientName+befordate+Date+afterdate+"</br><table style=\"width:100%\">"+ TableHeader + builder.toString() + "</table>"+footer+"</body></html>";

        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
        parentWebView = webView;
    }

    private void createWebPrintJob(View view) {
        PrintManager manager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + "Document";
        PrintDocumentAdapter adapter = parentWebView.createPrintDocumentAdapter(jobName);
        PrintJob printJob = manager.print(jobName, adapter,
                new PrintAttributes.Builder().build());
    }

    public void close(View view) {
        finish();
    }
}