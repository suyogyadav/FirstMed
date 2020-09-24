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
                        + PatientTable.DEBT_COLUMN + " TEXT NOT NULL)";

        final String CREATE_MEDICINE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + MedicineTable.TABLE_NAME + " ("
                        + MedicineTable._ID + " INTEGER PRIMARY KEY, "
                        + MedicineTable.PATIENT_ID_COLUMN + " INTEGER NOT NULL, "
                        + MedicineTable.DATE_COLUMN + " TEXT NOT NULL, "
                        + MedicineTable.OLD_MEDICINE_COLUMN + " TEXT NOT NULL, "
                        + MedicineTable.OLD_DISEASE_COLUMN + " TEXT NOT NULL)";

        final String CREATE_DEPT_HISTORY_TABLE =
                "CREATE TABLE IF NOT EXISTS " + DeptHistoryTable.TABLE_NAME + " ("
                        + DeptHistoryTable._ID + " INTEGER PRIMARY KEY, "
                        + DeptHistoryTable.PATIENT_ID_COLUMN + " INTEGER NOT NULL, "
                        + DeptHistoryTable.DATE_COLUMN + " TEXT NOT NULL, "
                        + DeptHistoryTable.DEPT_COLUMN + " INTEGER NOT NULL, "
                        + DeptHistoryTable.TRANS_COLUMN + " TEXT NOT NULL)";

        final String CREATE_MEDICINE_LIST_TABLE =
                "CREATE TABLE IF NOT EXISTS " + MedicineListTable.TABLE_NAME + " ("
                        + MedicineListTable._ID + " INTEGER PRIMARY KEY, "
                        + MedicineListTable.MEDICINE_NAME + " TEXT NOT NULL)";

        final String CREATE_PATIENT_COUNT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + PatientCountTable.TABLE_NAME + " ("
                        + PatientCountTable._ID + " INTEGER PRIMARY KEY, "
                        + PatientCountTable.YEAR_COLUMN + " TEXT NOT NULL, "
                        + PatientCountTable.MONTH_COLUMN + " TEXT NOT NULL, "
                        + PatientCountTable.DATE_COLUMN + " TEXT NOT NULL, "
                        + PatientCountTable.COUNT_COLUMN + " TEXT NOT NULL)";

        db.execSQL(CREATE_PATIENT_TABLE);
        db.execSQL(CREATE_MEDICINE_TABLE);
        db.execSQL(CREATE_DEPT_HISTORY_TABLE);
        db.execSQL(CREATE_MEDICINE_LIST_TABLE);
        db.execSQL(CREATE_PATIENT_COUNT_TABLE);
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
        values.put(PatientTable.DEBT_COLUMN, "" + 0);
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

    public void addMedList(String med) {
        if (!isMedAvilable(med)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MedicineListTable.MEDICINE_NAME, med);
            db.insert(MedicineListTable.TABLE_NAME, null, values);
            db.close();
        }
    }

    public boolean isMedAvilable(String med) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {MedicineListTable.MEDICINE_NAME};
        Cursor cursor = db.query(MedicineListTable.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (med.equals(cursor.getString(0))) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return false;
    }


    public void addCount(String date, int count) {
        SQLiteDatabase db = getWritableDatabase();

        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];

        String[] projection = {PatientCountTable.YEAR_COLUMN, PatientCountTable.MONTH_COLUMN, PatientCountTable.DATE_COLUMN, PatientCountTable.COUNT_COLUMN};
        String selection = PatientCountTable.DATE_COLUMN + " =? ";
        String[] selectionArgs = {day};

        Cursor cursor = db.query(PatientCountTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(PatientCountTable.COUNT_COLUMN, Integer.parseInt(cursor.getString(3)) + count);
            db.update(PatientCountTable.TABLE_NAME, values, selection, selectionArgs);
        } else {
            ContentValues values = new ContentValues();
            values.put(PatientCountTable.YEAR_COLUMN, year);
            values.put(PatientCountTable.MONTH_COLUMN, month);
            values.put(PatientCountTable.DATE_COLUMN, day);
            values.put(PatientCountTable.COUNT_COLUMN, "" + 1);
            db.insert(PatientCountTable.TABLE_NAME, null, values);
        }
        cursor.close();
        db.close();
    }

    public void addDebt(long rowId, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeptHistoryTable.PATIENT_ID_COLUMN, rowId);
        values.put(DeptHistoryTable.DATE_COLUMN, getDateTime());
        values.put(DeptHistoryTable.DEPT_COLUMN, amount);
        values.put(DeptHistoryTable.TRANS_COLUMN, "Added");
        db.insert(DeptHistoryTable.TABLE_NAME, null, values);

        String[] projection = {PatientTable._ID, PatientTable.DEBT_COLUMN};
        String selection = PatientTable._ID + " =? ";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int oldAmt = cursor.getInt(1);
            ContentValues contentValues = new ContentValues();
            contentValues.put(PatientTable.DEBT_COLUMN, (oldAmt + amount));
            String selection1 = PatientTable._ID + " LIKE ?";
            String[] selectionArgs1 = {"" + rowId};
            db.update(PatientTable.TABLE_NAME, contentValues, selection1, selectionArgs1);
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
        values.put(DeptHistoryTable.TRANS_COLUMN, "Deducted");
        db.insert(DeptHistoryTable.TABLE_NAME, null, values);

        String[] projection = {PatientTable._ID, PatientTable.DEBT_COLUMN};
        String selection = PatientTable._ID + " =? ";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int oldAmt = cursor.getInt(1);
            int newamt = oldAmt - amount;
            ContentValues contentValues = new ContentValues();
            contentValues.put(PatientTable.DEBT_COLUMN, Math.max(newamt, 0));
            String selection1 = PatientTable._ID + " LIKE ?";
            String[] selectionArgs1 = {"" + rowId};
            db.update(PatientTable.TABLE_NAME, contentValues, selection1, selectionArgs1);
        }
        cursor.close();
        db.close();
    }


    public List<PatientPOJO> getPatient() {
        List<PatientPOJO> patients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {PatientTable._ID, PatientTable.NAME_COLUMN, PatientTable.AGE_COLUMN, PatientTable.GENDER_COLUMN, PatientTable.DEBT_COLUMN};
        String sortOrder = PatientTable._ID + " DESC";
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, null, null, null, null, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                PatientPOJO patient = new PatientPOJO();
                patient.setId(cursor.getInt(0));
                patient.setName(cursor.getString(1));
                patient.setAge(cursor.getString(2));
                patient.setGender(cursor.getString(3));
                patient.setDebt(cursor.getString(4));
                patients.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return patients;
    }

    public PatientPOJO getSinglePatient(long rowId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {PatientTable._ID, PatientTable.NAME_COLUMN, PatientTable.AGE_COLUMN, PatientTable.GENDER_COLUMN, PatientTable.DEBT_COLUMN};
        String selection = PatientTable._ID + " = ?";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        PatientPOJO patient = new PatientPOJO();
        patient.setId(cursor.getInt(0));
        patient.setName(cursor.getString(1));
        patient.setAge(cursor.getString(2));
        patient.setDebt(cursor.getString(3));
        cursor.close();
        db.close();
        return patient;
    }

    public List<String> getMedicineList() {
        List<String> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {MedicineListTable.MEDICINE_NAME};
        Cursor cursor = db.query(MedicineListTable.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String medicine = cursor.getString(0);
                medicines.add(medicine);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return medicines;
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
        String[] projection = {PatientTable._ID, PatientTable.DEBT_COLUMN};
        String selection = PatientTable._ID + " =? ";
        String[] selectionArgs = {"" + rowId};
        Cursor cursor = db.query(PatientTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            debt = cursor.getInt(1);
        }
        cursor.close();
        db.close();
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

    public int getDayCount(String date) {

        SQLiteDatabase db = getReadableDatabase();

        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];

        String[] projection = {PatientCountTable.YEAR_COLUMN, PatientCountTable.MONTH_COLUMN, PatientCountTable.DATE_COLUMN, PatientCountTable.COUNT_COLUMN};
        String selection = PatientCountTable.YEAR_COLUMN + " =? " + "AND " + PatientCountTable.MONTH_COLUMN + " =? " + "AND " + PatientCountTable.DATE_COLUMN + " =? ";
        String[] selectionArgs = {year, month, day};

        Cursor cursor = db.query(PatientCountTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            return Integer.parseInt(cursor.getString(3));
        } else {
            return 0;
        }
    }

    public int getMonthCount(String date) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + PatientCountTable.MONTH_COLUMN + ", COUNT(" + PatientCountTable.COUNT_COLUMN + ") FROM " + PatientCountTable.TABLE_NAME + " GROUP BY " + PatientCountTable.MONTH_COLUMN;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0).equals(date.split("-")[1])) {
                    int cnt = Integer.parseInt(cursor.getString(1));
                    cursor.close();
                    db.close();
                    return cnt;
                }
            } while (cursor.moveToNext());
        }
        return 0;
    }

    public List<ChartData> getChartData()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<ChartData> data = new ArrayList<>();
        String[] projection = {PatientCountTable.DATE_COLUMN,PatientCountTable.COUNT_COLUMN};
        String selection = PatientCountTable.MONTH_COLUMN + " =? ";
        String[] selectionArgs = {""+getDateTime().split( "-")[1]};
        Cursor cursor = db.query(PatientCountTable.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
        if (cursor.moveToNext())
        {
            do {
                ChartData chartData = new ChartData();
                chartData.setDate(cursor.getString(0));
                chartData.setCount(cursor.getString(1));
                data.add(chartData);
            }while (cursor.moveToNext());

            cursor.close();
            db.close();
            return data;
        }
        return data;
    }

    public int getPatientCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {PatientTable.NAME_COLUMN};
        Cursor cursor = db.query(PatientTable.TABLE_NAME,projection,null,null,null,null,null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return cnt;
    }

    public void deletePatient(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = PatientTable._ID + " LIKE ?";
        String[] selectionArgs = {"" + rowId};

        db.delete(PatientTable.TABLE_NAME, selection, selectionArgs);

        selection = MedicineTable.PATIENT_ID_COLUMN + " LIKE ?";
        db.delete(MedicineTable.TABLE_NAME, selection, selectionArgs);

        selection = DeptHistoryTable.PATIENT_ID_COLUMN + " LIKE ?";
        db.delete(DeptHistoryTable.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void deleteMedicine(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = MedicineListTable.MEDICINE_NAME + " LIKE ?";
        String[] selectionArgs = {name};
        int deleted = db.delete(MedicineListTable.TABLE_NAME, selection, selectionArgs);
        Log.i("abgd", "" + deleted);
        db.close();
    }

    public void updateMedicineName(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MedicineListTable.MEDICINE_NAME, newName);

        String selection = MedicineListTable.MEDICINE_NAME + " LIKE ?";
        String[] selectionArgs = {oldName};

        db.update(MedicineListTable.TABLE_NAME, values, selection, selectionArgs);
        db.close();
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
        public static final String DEBT_COLUMN = "Debt";

    }

    public static class MedicineTable implements BaseColumns {
        public static final String TABLE_NAME = "Medicine_History_Table";
        public static final String PATIENT_ID_COLUMN = "Pid";
        public static final String DATE_COLUMN = "Date";
        public static final String OLD_MEDICINE_COLUMN = "OldMed";
        public static final String OLD_DISEASE_COLUMN = "OldDes";
    }

    public static class DeptHistoryTable implements BaseColumns {
        public static final String TABLE_NAME = "Dept_History_Table";
        public static final String PATIENT_ID_COLUMN = "Pid";
        public static final String DATE_COLUMN = "Date";
        public static final String DEPT_COLUMN = "Dept";
        public static final String TRANS_COLUMN = "Trans";
    }

    public static class MedicineListTable implements BaseColumns {
        public static final String TABLE_NAME = "Medicine_List_Table";
        public static final String MEDICINE_NAME = "MedicineName";
    }

    public static class PatientCountTable implements BaseColumns {
        public static final String TABLE_NAME = "Patient_Count_Table";
        public static final String YEAR_COLUMN = "Year";
        public static final String MONTH_COLUMN = "Month";
        public static final String DATE_COLUMN = "Date";
        public static final String COUNT_COLUMN = "Count";
    }
}
