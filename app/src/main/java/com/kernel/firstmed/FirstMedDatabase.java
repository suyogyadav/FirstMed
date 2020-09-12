package com.kernel.firstmed;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class FirstMedDatabase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FirstMedDatabase.db";

    public FirstMedDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_PATIENT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + PatientTable.TABLE_NAME + " ("
                + PatientTable._ID + " INTEGER PRIMARY KEY, "
                + PatientTable.NAME_COLUMN + " TEXT NOT NULL, "
                + PatientTable.AGE_COLUMN + " TEXT NOT NULL, "
                + PatientTable.GENDER_COLUMN + " TEXT NOT NULL, "
                + PatientTable.DATE_COLUMN + " TEXT NOT NULL)";

        //final String CREATE_MEDICINE_TABLE = "";
        //final String CREATE_DEPT_TABLE = "";
        db.execSQL(CREATE_PATIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void addPatient(String name,String age,String gender)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(PatientTable.NAME_COLUMN,name);
        values.put(PatientTable.AGE_COLUMN,age);
        values.put(PatientTable.GENDER_COLUMN,gender);
        values.put(PatientTable.DATE_COLUMN,getDateTime());
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



    public static class PatientTable implements BaseColumns {
        public static final String TABLE_NAME = "Patient_Table";
        public static final String NAME_COLUMN = "Name";
        public static final String AGE_COLUMN = "Age";
        public static final String GENDER_COLUMN = "Gender";
        public static final String DATE_COLUMN = "Date";

    }
}
