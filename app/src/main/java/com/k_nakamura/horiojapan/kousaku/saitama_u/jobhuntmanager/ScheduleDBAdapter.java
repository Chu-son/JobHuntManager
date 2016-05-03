package com.k_nakamura.horiojapan.kousaku.saitama_u.jobhuntmanager;

import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDBAdapter {
    static final String DATABASE_NAME = "schedule.db";
    static final int DATABASE_VERSION = 1;

    public static int num = 0;

    String tableName;

    public static final String COL_ID = "_id";
    public static final String COL_SCHEDULENAME = "scheduleName";
    public static final String COL_DATE = "date";
    public static final String COL_PLACE = "place";
    public static final String COL_MEMO = "memo";


    protected final Context context;

    protected ScheduleDatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public ScheduleDBAdapter(Context context){
        this.context = context;
    }

    //
    // SQLiteOpenHelper
    //
    private static class ScheduleDatabaseHelper extends SQLiteOpenHelper {
        String tableName;
        public ScheduleDatabaseHelper(Context context,String tableName) {
            super(context, tableName+".db", null, DATABASE_VERSION);
            this.tableName = tableName;
        }

        @Override public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE " + tableName + " ("
                            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + COL_SCHEDULENAME + " TEXT NOT NULL,"
                            + COL_DATE + " TEXT NOT NULL,"
                            + COL_PLACE + " TEXT NOT NULL,"
                            + COL_MEMO + " TEXT NOT NULL);"
            );
        }

        @Override public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
            onCreate(db);
        }
    }

    //
    // Adapter Methods
    //
    public ScheduleDBAdapter open(String tableName) {
        this.tableName = tableName;

        dbHelper = new ScheduleDatabaseHelper(this.context,tableName);
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
        return db.delete(tableName, null, null) > 0;
    }
    public boolean deleteCompany(int id){
        return db.delete(tableName, COL_ID + "=" + id, null) > 0;
    }
    public Cursor getAllCompanys(){
        return db.query(tableName, null, null, null, null, null, null);
    }
    public void saveSchedule(String name,String date , String place)
    {
        ContentValues values = new ContentValues();
        values.put(COL_SCHEDULENAME, name);
        values.put(COL_DATE, date);
        values.put(COL_PLACE,place);
        values.put(COL_MEMO,"");
        db.insertOrThrow(tableName, null, values);
    }

    /*public void updateSchedule(String name,String date , String place)
    {
        ContentValues values = new ContentValues();
        values.put(COL_SCHEDULENAME, name);
        values.put(COL_DATE, date);
        values.put(COL_PLACE,place);
        values.put(COL_MEMO,"");
        db.update(tableName, values,COL_ID + "=?", new String[]{""+ company.getId()});
    }*/
}