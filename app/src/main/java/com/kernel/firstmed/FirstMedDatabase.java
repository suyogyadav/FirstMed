package com.kernel.firstmed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public long addPatient(String name, String age, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PatientTable.NAME_COLUMN, name);
        values.put(PatientTable.AGE_COLUMN, age);
        values.put(PatientTable.GENDER_COLUMN, gender);
        values.put(PatientTable.DATE_COLUMN, getDateTime());
        long rowId = db.insert(PatientTable.TABLE_NAME, null, values);
        db.close();
        return rowId;
    }

    public List<PatientPOJO> getPatient() {
        List<PatientPOJO> patients = new ArrayList<PatientPOJO>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {PatientTable._ID, PatientTable.NAME_COLUMN, PatientTable.AGE_COLUMN, PatientTable.GENDER_COLUMN, PatientTable.DATE_COLUMN};
        String sortOrder = PatientTable._ID + " DESC";
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                PatientPOJO patient = new PatientPOJO();
                patient.setId(cursor.getInt(0));
                patient.setName(cursor.getString(1));
                patient.setAge(cursor.getString(2));
                patient.setDate(cursor.getString(3));
                patients.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return patients;
    }

    public PatientPOJO getSinglePatient(long rowId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {PatientTable._ID, PatientTable.NAME_COLUMN, PatientTable.AGE_COLUMN, PatientTable.GENDER_COLUMN, PatientTable.DATE_COLUMN};
        String selection = PatientTable._ID + " = ?";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        PatientPOJO patient = new PatientPOJO();
        patient.setId(cursor.getInt(0));
        patient.setName(cursor.getString(1));
        patient.setAge(cursor.getString(2));
        patient.setDate(cursor.getString(3));
        cursor.close();
        db.close();
        return patient;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date).split(" ")[0];
    }


    public static class PatientTable implements BaseColumns {
        public static final String TABLE_NAME = "Patient_Table";
        public static final String NAME_COLUMN = "Name";
        public static final String AGE_COLUMN = "Age";
        public static final String GENDER_COLUMN = "Gender";
        public static final String DATE_COLUMN = "Date";

    }
}
