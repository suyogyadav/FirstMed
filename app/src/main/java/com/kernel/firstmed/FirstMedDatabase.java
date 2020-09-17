package com.kernel.firstmed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

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

        final String CREATE_DEPT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + DeptTable.TABLE_NAME + " ("
                        + DeptTable._ID + " INTEGER PRIMARY KEY, "
                        + DeptTable.PATIENT_ID_COLUMN + " INTEGER NOT NULL, "
                        + DeptTable.DEPT_COLUMN + " TEXT NOT NULL)";

        final String CREATE_DEPT_HISTORY_TABLE =
                "CREATE TABLE IF NOT EXISTS " + DeptHistoryTable.TABLE_NAME + " ("
                        + DeptHistoryTable._ID + " INTEGER PRIMARY KEY, "
                        + DeptHistoryTable.PATIENT_ID_COLUMN + " INTEGER NOT NULL, "
                        + DeptHistoryTable.DATE_COLUMN + " TEXT NOT NULL, "
                        + DeptHistoryTable.DEPT_COLUMN + " INTEGER NOT NULL, "
                        + DeptHistoryTable.TRANS_COLUMN + " TEXT NOT NULL)";

        db.execSQL(CREATE_PATIENT_TABLE);
        db.execSQL(CREATE_MEDICINE_TABLE);
        db.execSQL(CREATE_DEPT_TABLE);
        db.execSQL(CREATE_DEPT_HISTORY_TABLE);
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

    public void addDebt(long rowId, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeptHistoryTable.PATIENT_ID_COLUMN, rowId);
        values.put(DeptHistoryTable.DATE_COLUMN, getDateTime());
        values.put(DeptHistoryTable.DEPT_COLUMN, amount);
        values.put(DeptHistoryTable.TRANS_COLUMN, "ADEED");
        db.insert(DeptHistoryTable.TABLE_NAME, null, values);

        String[] projection = {DeptTable.PATIENT_ID_COLUMN, DeptTable.DEPT_COLUMN};
        String selection = DeptTable.PATIENT_ID_COLUMN + " =? ";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            int oldAmt = cursor.getInt(1);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DeptTable.DEPT_COLUMN, (oldAmt + amount));
            String selection1 = DeptTable.PATIENT_ID_COLUMN + " LIKE ?";
            String[] selectionArgs1 = {"" + rowId};
            db.update(DeptTable.TABLE_NAME, contentValues, selection1, selectionArgs1);
        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put(DeptTable.PATIENT_ID_COLUMN, rowId);
            contentValues.put(DeptTable.DEPT_COLUMN, (amount));
            db.insert(DeptTable.TABLE_NAME, null, contentValues);
        }

        cursor.close();
        db.close();
    }

    public void removeDebt(long rowId, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeptHistoryTable.PATIENT_ID_COLUMN, rowId);
        values.put(DeptHistoryTable.DATE_COLUMN, getDateTime());
        values.put(DeptHistoryTable.DEPT_COLUMN, amount);
        values.put(DeptHistoryTable.TRANS_COLUMN, "Removed");
        db.insert(DeptHistoryTable.TABLE_NAME, null, values);

        String[] projection = {DeptTable.PATIENT_ID_COLUMN, DeptTable.DEPT_COLUMN};
        String selection = DeptTable.PATIENT_ID_COLUMN + " =? ";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            int oldAmt = cursor.getInt(1);
            int newamt = oldAmt - amount;
            ContentValues contentValues = new ContentValues();
            if (newamt >= 0) {
                contentValues.put(DeptTable.DEPT_COLUMN, (newamt));
            } else {
                contentValues.put(DeptTable.DEPT_COLUMN, 0);
            }
            String selection1 = DeptTable.PATIENT_ID_COLUMN + " LIKE ?";
            String[] selectionArgs1 = {"" + rowId};
            db.update(DeptTable.TABLE_NAME, contentValues, selection1, selectionArgs1);
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
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + MedicineTable.DATE_COLUMN + " FROM " + MedicineTable.TABLE_NAME + " WHERE " + MedicineTable.PATIENT_ID_COLUMN + " = " + rowId, null);
        List<MedicinePOJO> medicinelist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                List<String> medlist = new ArrayList<>();

                MedicinePOJO medicine = new MedicinePOJO();
                medicine.setPid((int) rowId);
                medicine.setDate(cursor.getString(0));

                String date = cursor.getString(0);

                String[] projection1 = {MedicineTable.OLD_MEDICINE_COLUMN, MedicineTable.OLD_DISEASE_COLUMN};
                String selection1 = MedicineTable.PATIENT_ID_COLUMN + " = ?" + " AND " + MedicineTable.DATE_COLUMN + " = ?";
                String[] selectionArgs1 = {String.valueOf(rowId), date};
                Cursor cursor1 = db.query(MedicineTable.TABLE_NAME, projection1, selection1, selectionArgs1, null, null, null);

                if (cursor1.moveToFirst()) {
                    medlist.clear();
                    medicine.setOld_des(cursor1.getString(1));
                    do {
                        medlist.add(cursor1.getString(0));
                        Log.i("XYZ", cursor1.getString(0));
                    } while (cursor1.moveToNext());
                    cursor1.close();
                }
                medicine.setOld_med(medlist);
                medicinelist.add(medicine);
                Log.i("poiu", "" + medicine.getOld_med().size());
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return medicinelist;
    }

    public int getDebt(long rowId) {
        int debt = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {DeptTable.PATIENT_ID_COLUMN, DeptTable.DEPT_COLUMN};
        String selection = DeptTable.PATIENT_ID_COLUMN + " =? ";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(DeptTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            debt = cursor.getInt(1);
        } else {
            debt = 0;
        }
        return debt;
    }

    public List<DebtPojo> getDebtHistory(long rowId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {DeptHistoryTable.PATIENT_ID_COLUMN, DeptHistoryTable.DATE_COLUMN, DeptHistoryTable.DEPT_COLUMN, DeptHistoryTable.TRANS_COLUMN};
        String selection = DeptHistoryTable.PATIENT_ID_COLUMN + " =? ";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(DeptHistoryTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        List<DebtPojo> debtHistory = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                DebtPojo debt = new DebtPojo();
                debt.setPid(cursor.getInt(0));
                debt.setDate(cursor.getString(1));
                debt.setDebt(cursor.getInt(2));
                debt.setTrans(cursor.getString(3));
                debtHistory.add(debt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return debtHistory;
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

    public static class DeptTable implements BaseColumns {
        public static final String TABLE_NAME = "Dept_Table";
        public static final String PATIENT_ID_COLUMN = "Pid";
        public static final String DEPT_COLUMN = "Dept";
    }

    public static class DeptHistoryTable implements BaseColumns {
        public static final String TABLE_NAME = "Dept_History_Table";
        public static final String PATIENT_ID_COLUMN = "Pid";
        public static final String DATE_COLUMN = "Date";
        public static final String DEPT_COLUMN = "Dept";
        public static final String TRANS_COLUMN = "Trans";
    }
}
