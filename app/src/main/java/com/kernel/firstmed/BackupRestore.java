package com.kernel.firstmed;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import android.view.View;

import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BackupRestore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        MaterialTextView backuptext = findViewById(R.id.backupText);
        backuptext.setText("Back up your patients and medicine data to local drive.\nYou can restore them when you reinstall FirestMed.\nMake sure you keep the backup safe to use it later.");
    }

    public void backup(View view) throws Exception {
        final String path = "/data/data/com.kernel.firstmed/databases/FirstMedDatabase.db";
        File dbfile = new File(path);
        FileInputStream fis = new FileInputStream(dbfile);
        File folder = new File(Environment.getExternalStorageDirectory(), "FirstMedBackup");
        boolean a = folder.mkdirs();
        File db = new File(folder.getPath() + "/FirstMedDatabase.db");
        FileOutputStream output = new FileOutputStream(db);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        fis.close();
    }

    public void close(View view) {
        finish();
    }

    public void restore(View view) throws Exception {

        File folder = new File(Environment.getExternalStorageDirectory(), "FirstMedBackup");
        File db = new File(folder.getPath() + "/backup.db");
        FileInputStream fis = new FileInputStream(db);

        String dir = getApplicationInfo().dataDir;
        File folder2 = new File(dir + "/databases");
        boolean b = folder2.mkdirs();
        File dbfile = new File(folder2, "FirstMedDatabase.db");
        FileOutputStream output = new FileOutputStream(dbfile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        fis.close();
    }

}