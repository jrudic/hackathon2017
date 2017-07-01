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
    private Context mContext;

    public LocationDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
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

    public static final String DATABASE_NAME = "HYDRADATABASE.db";

    private static final String DATABASE_TABLE = "LOCATIONS";

    public static final int DATABASE_VERSION = 1;

    private SQLiteDatabase mDatabase;

    public void close() {
        mDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            mDatabase = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
         mDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void createRow(String timeStamp, String latitute, String longitute) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("timeStamp", timeStamp);
        initialValues.put("latitute", latitute);
        initialValues.put("longitute", longitute);
       if(mDatabase != null) {
           mDatabase.insert(DATABASE_TABLE, null, initialValues);
       }
    }

    public void deleteRow(long rowId) {
        mDatabase.delete(DATABASE_TABLE, "_id=" + rowId, null);
    }

    public List<Row> fetchAllRows() {
        ArrayList<Row> ret = new ArrayList<>();
        try {
            Cursor c =
                    mDatabase.query(DATABASE_TABLE, new String[] {
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
