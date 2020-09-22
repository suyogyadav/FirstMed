package com.kernel.firstmed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class BackupRestore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
    }

    public void backup(View view) throws Exception{
        final String path = "/data/data/com.kernel.firstmed/databases/FirstMedDatabase.db";
        File dbfile = new File(path);
        FileInputStream fis = new FileInputStream(dbfile);
        File folder = new File(Environment.getExternalStorageDirectory(),"FirstMedBackup");
        boolean a = folder.mkdirs();
        File db = new File(folder.getPath()+"/backup.db");
        FileOutputStream output = new FileOutputStream(db);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        fis.close();
    }

    public void close(View view) {
        finish();
    }
}