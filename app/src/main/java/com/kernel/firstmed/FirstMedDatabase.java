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

        final String CREATE_MEDICINE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + MedicineTable.TABLE_NAME + " ("
                        + MedicineTable._ID + " INTEGER PRIMARY KEY, "
                        + MedicineTable.PATIENT_ID_COLUMN + " INTEGER NOT NULL, "
                        + MedicineTable.DATE_COLUMN + " TEXT NOT NULL, "
                        + MedicineTable.OLD_MEDICINE_COLUMN + " TEXT NOT NULL, "
                        + MedicineTable.OLD_DISEASE_COLUMN + " TEXT NOT NULL)";

        //final String CREATE_DEPT_TABLE = "";
        db.execSQL(CREATE_PATIENT_TABLE);
        db.execSQL(CREATE_MEDICINE_TABLE);
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

    public void addMedicine(List<String> meds, long rowID, String Diseases) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < meds.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(MedicineTable.PATIENT_ID_COLUMN, "" + rowID);
            values.put(MedicineTable.DATE_COLUMN, getDateTime());
            values.put(MedicineTable.OLD_MEDICINE_COLUMN, meds.get(i));
            values.put(MedicineTable.OLD_DISEASE_COLUMN, Diseases);
            db.insert(MedicineTable.TABLE_NAME, null, values);
        }
        db.close();
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

    public List<MedicinePOJO> getMedicine(long rowId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {MedicineTable.PATIENT_ID_COLUMN, MedicineTable.DATE_COLUMN, MedicineTable.OLD_MEDICINE_COLUMN, MedicineTable.OLD_DISEASE_COLUMN};
        String selection = MedicineTable.PATIENT_ID_COLUMN + " = ?";
        String[] selectionArgs = {"" + rowId};
        String sortOrder = MedicineTable.DATE_COLUMN + " DESC";
        Cursor cursor = db.query(MedicineTable.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        List<MedicinePOJO> medicinelist = new ArrayList<>();
        cursor.moveToFirst();
        while(cursor.moveToNext())
        {
            List<String> medlist = new ArrayList<>();
            MedicinePOJO medicine = new MedicinePOJO();
            medicine.setPid(cursor.getInt(0));
            String Date = cursor.getString(1);
            medicine.setDate(Date);
            medicine.setOld_des(cursor.getString(3));
            do {
                medlist.add(cursor.getString(2));
                cursor.moveToNext();
            }while (Date.equals(cursor.getString(1)));
            medicine.setOld_med(medlist);
            medicinelist.add(medicine);
            medlist.clear();
        }
        cursor.close();
        db.close();
        return medicinelist;
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

    public static class MedicineTable implements BaseColumns {
        public static final String TABLE_NAME = "Medicine_History_Table";
        public static final String PATIENT_ID_COLUMN = "Pid";
        public static final String DATE_COLUMN = "Date";
        public static final String OLD_MEDICINE_COLUMN = "OldMed";
        public static final String OLD_DISEASE_COLUMN = "OldDes";
    }
}
