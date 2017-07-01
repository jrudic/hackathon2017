package com.projectx.jovanrudic.mhydrabanking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jovanrudic on 7/1/17.
 */

public class LocationDBHelper extends SQLiteOpenHelper {

    public static final String LATITUTE = "latitute";
    public static final String LONGITUTE = "longitute";
    public static final String TIME_STAMP = "timeStamp";
    private static LocationDBHelper sInstance = null;
    private Context mContext;

    public LocationDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public static synchronized LocationDBHelper getLocationDataBaseHelper(Context context) {
        if (sInstance == null) {
            sInstance = new LocationDBHelper(context.getApplicationContext(), DATABASE_NAME,
                    null, DATABASE_VERSION);
            sInstance.getWritableDatabase();
        }
        return sInstance;
    }

    class Row extends Object {
        public long _Id;
        public String timeStamp;
        public String latitute;
        public String longitute;
    }

    private static final String DATABASE_CREATE =
            "create table LOCATIONS(_id integer primary key autoincrement, "
                    + "timeStamp text not null,"
                    + "latitute text not null, "
                    + "longitute text not null "
                    +");";

    private static final String DATABASE_NAME = "HYDRADATABASE.db";

    private static final String DATABASE_TABLE = "LOCATIONS";

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database;

    public void close() {
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            database = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void createRow(String timeStamp, String latitute, String longitute) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("timeStamp", timeStamp);
        initialValues.put("latitute", latitute);
        initialValues.put("longitute", longitute);
        database.insert(DATABASE_TABLE, null, initialValues);
    }

    public void deleteRow(long rowId) {
        database.delete(DATABASE_TABLE, "_id=" + rowId, null);
    }

    public List<Row> fetchAllRows() {
        ArrayList<Row> ret = new ArrayList<>();
        try {
            Cursor c =
                    database.query(DATABASE_TABLE, new String[] {
                            "_id", TIME_STAMP, LATITUTE, LONGITUTE}, null, null, null, null, null);
            int numRows = c.getColumnCount();
            c.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
                Row row = new Row();
                row._Id = c.getLong(0);
                row.timeStamp = c.getString(1);
                row.latitute = c.getString(2);
                row.longitute = c.getString(2);
                ret.add(row);
                c.moveToNext();
            }
        } catch (SQLException e) {
            //JR
        }
        return ret;
    }
}
