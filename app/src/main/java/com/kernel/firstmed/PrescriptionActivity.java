package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrescriptionActivity extends AppCompatActivity {

    private WebView parentWebView;
    private TextInputEditText patientName;
    private TextInputEditText subject;
    private TextInputEditText discription;
    private String mainDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        patientName = findViewById(R.id.edtpname);
        subject = findViewById(R.id.edtsubject);
        discription = findViewById(R.id.edtdis);
        parentWebView = new WebView(this);
        mainDate = getDateTime();
    }

    public void close(View view) {
        finish();
    }

    public void printJob(View view) {
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
                parentWebView = null;
            }
        });

        String beforname = "<table style=\"width:100%\"><tr><td style=\"width:70%\"><span style=\"font-weight: bold;\">Patient Name : </span>";
        String befordate = "</td><td style=\"width:30%\"><span style=\"font-weight: bold;\">Date : </span>";
        String afterdate = "</td></tr></table>";
        String footer = "<div style=\"position: absolute; bottom: 0; right: 0; width: 50%; text-align:right; font-weight: bold;\">Dr.Suyog Yadav</div>";
        String PatientName = patientName.getText().toString();
        String subjecttext = subject.getText().toString();
        String subjecthtml = "<p><span style=\"font-weight: bold;\">Subject : </span>" + subjecttext + "</p>";
        String Date = mainDate;
        String Dis = discription.getText().toString().replaceAll("/n", "</br>");
        String htmlDocument = "<html><head><style>th, td {padding: 5px;text-align: left;}</style></head><body><h1>Dr. Suyog Yadav</h1><h3>MBBS</h3><p>550/5, East side Of S.T.Stand ,</br>M.P.Patil Hospital Road , Sangli</br>Phone - 8806035350</p><hr>" + beforname + PatientName + befordate + Date + afterdate + "</br>" + subjecthtml + "</br><p>" + Dis + "</p>" + footer + "</body></html>";
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
        parentWebView = webView;
    }

    private void createWebPrintJob(View view) {
        PrintManager manager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + "Document";
        PrintDocumentAdapter adapter = parentWebView.createPrintDocumentAdapter(jobName);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME);
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
        PrintJob printJob = manager.print(jobName, adapter,
                builder.build());
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date).split(" ")[0];
    }


    public void datePicker(View view) {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select A Date");
        final MaterialDatePicker picker = builder.build();
        picker.show(getSupportFragmentManager(), "DATE_PICKER");
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                mainDate = picker.getHeaderText();
            }
        });
    }
}