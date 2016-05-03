package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    static final String DATABASE_NAME = "mycompany.db";
    static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "companys";
    public static final String COL_ID = "_id";
    public static final String COL_COMPANY = "company";
    public static final String COL_LASTUPDATE = "lastupdate";
    public static final String COL_MEMO = "memo";
    public static final String COL_KANA = "kana";
    public static final String COL_SCHEDULETABLE = "scheduleTable";

    protected final Context context;

    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    //
    // SQLiteOpenHelper
    //
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + " ("
                            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + COL_COMPANY + " TEXT NOT NULL,"
                            + COL_LASTUPDATE + " TEXT NOT NULL,"
                            + COL_KANA + " TEXT NOT NULL,"
                            + COL_SCHEDULETABLE + " TEXT NOT NULL,"
                            + COL_MEMO + " TEXT NOT NULL);"
            );
        }

        @Override public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    //
    // Adapter Methods
    //
    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        dbHelper.close();
    }

    //
    // App Methods
    //
    public boolean deleteAllCompanys(){
        return db.delete(TABLE_NAME, null, null) > 0;
    }
    public boolean deleteCompany(int id){
        return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
    }
    public Cursor getAllCompanys(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    public void saveCompany(Company company)
    {
        ContentValues values = new ContentValues();
        values.put(COL_COMPANY, company.getCompany());
        values.put(COL_LASTUPDATE, company.getLastupdate());
        values.put(COL_MEMO,company.getMemo());
        values.put(COL_KANA,company.getKana());
        values.put(COL_SCHEDULETABLE,company.getScheduleDB());
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    public void update(Company company)
    {
        ContentValues values = new ContentValues();
        values.put(COL_COMPANY, company.getCompany());
        values.put(COL_LASTUPDATE, company.getLastupdate());
        values.put(COL_MEMO,company.getMemo());
        values.put(COL_KANA,company.getKana());
        db.update(TABLE_NAME, values,COL_ID + "=?", new String[]{""+ company.getId()});
    }



}