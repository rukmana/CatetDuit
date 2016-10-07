package nursulaeman.catetduit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nur on 26/09/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "catetduit.db";

    public static final String TABLE_NAME_INCOME = "incomes";
    public static final String COL_IN_ID = "ID";
    public static final String COL_IN_DES = "DESCRIPTION";
    public static final String COL_IN_AMO = "AMOUNT";
    public static final String COL_IN_DAT = "DATE";
    public static final String COL_IN_CDT = "CDATE";
    public static final String COL_IN_UDT = "UDATE";

    public static final String TABLE_NAME_EXPENSES = "expenses";
    public static final String COL_EX_ID = "ID";
    public static final String COL_EX_DES = "DESCRIPTION";
    public static final String COL_EX_AMO = "AMOUNT";
    public static final String COL_EX_DAT = "DATE";
    public static final String COL_EX_CDT = "CDATE";
    public static final String COL_EX_UDT = "UDATE";

    public static final String TABLE_NAME_TMP = "tmp";
    public static final String COL_TMP_ID = "ID";
    public static final String COL_TMP = "TMP";

    public static final String TABLE_CREATE_INCOME = "CREATE TABLE " + TABLE_NAME_INCOME + " ( " +
            COL_IN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_IN_DES + " TEXT, " +
            COL_IN_AMO + " TEXT, " +
            COL_IN_DAT + " TEXT, " +
            COL_IN_CDT + " TEXT, " +
            COL_IN_UDT + " TEXT );";

    public static final String TABLE_CREATE_EXPENSES = "CREATE TABLE " + TABLE_NAME_EXPENSES + " ( " +
            COL_EX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_EX_DES + " TEXT, " +
            COL_EX_AMO + " TEXT, " +
            COL_EX_DAT + " TEXT, " +
            COL_EX_CDT + " TEXT, " +
            COL_EX_UDT + " TEXT );";

    public static final String TABLE_CREATE_TMP = "CREATE TABLE " + TABLE_NAME_TMP + " ( " +
            COL_TMP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TMP + " INTEGER );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_INCOME);
        db.execSQL(TABLE_CREATE_EXPENSES);
        db.execSQL(TABLE_CREATE_TMP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TMP);
        onCreate(db);
    }

    public boolean saveIncome(String desc, String amount, String date, String cdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_IN_DES, desc);
        content_values.put(COL_IN_AMO, amount);
        content_values.put(COL_IN_DAT, date);
        content_values.put(COL_IN_CDT, cdate);
        long result = db.insert(TABLE_NAME_INCOME, null, content_values);
        return result != -1;
    }

    public boolean saveExpense(String desc, String amount, String date, String cdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_EX_DES, desc);
        content_values.put(COL_EX_AMO, amount);
        content_values.put(COL_EX_DAT, date);
        content_values.put(COL_EX_CDT, cdate);
        long result = db.insert(TABLE_NAME_EXPENSES, null, content_values);
        return result != -1;
    }

    public boolean saveTmp(Integer tmp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_TMP, tmp);
        long result = db.insert(TABLE_NAME_TMP, null, content_values);
        return result != -1;
    }

    public Cursor listIncome() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor income = db.rawQuery("SELECT * FROM " + TABLE_NAME_INCOME, null);
        return income;
    }

    public Cursor listExpense() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor expense = db.rawQuery("SELECT * FROM " + TABLE_NAME_EXPENSES, null);
        return expense;
    }

    public Cursor listTmp() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor expense = db.rawQuery("SELECT * FROM " + TABLE_NAME_TMP, null);
        return expense;
    }

    public boolean updateIncome(String id, String desc, String amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_IN_ID, id);
        content_values.put(COL_IN_DES, desc);
        content_values.put(COL_IN_AMO, amount);
        content_values.put(COL_IN_UDT, date);
        db.update(TABLE_NAME_INCOME, content_values, "ID = ? ", new String[]{id});
        return true;
    }

    public boolean updateExpense(String id, String desc, String amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_EX_ID, id);
        content_values.put(COL_EX_DES, desc);
        content_values.put(COL_EX_AMO, amount);
        content_values.put(COL_EX_UDT, date);
        db.update(TABLE_NAME_EXPENSES, content_values, "ID = ? ", new String[]{id});
        return true;
    }

    public boolean updateTmp(String id, Integer tmp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_TMP_ID, id);
        content_values.put(COL_TMP, tmp);
        db.update(TABLE_NAME_TMP, content_values, "ID = ? ", new String[]{id});
        return true;
    }

    public Integer deleteIncome(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_INCOME, "ID = ?", new String[] {id});
    }

    public Integer deleteExpense(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_EXPENSES, "ID = ?", new String[] {id});
    }

}
