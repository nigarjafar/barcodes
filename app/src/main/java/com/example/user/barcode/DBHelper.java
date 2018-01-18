package com.example.user.barcode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 11/24/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    final static int version = 1;
    final static String db_name = "barcode";
    final static String table_name = "barcode";
    final static String table_column_barcodeID = "barcodeID";
    final static String table_column_barcodeName = "barcodeName";
    final static String table_column_count = "count";

    public DBHelper(Context context) {
        super(context, db_name, null, version);
    }

    public void createTable(){
        Log.e("createTable","created");
        SQLiteDatabase db = getWritableDatabase();

        String createStr = "create table if not exists " + table_name
                + " (" + table_column_barcodeID + " TEXT primary key, "
                + table_column_barcodeName + " TEXT not null, "
                + table_column_count + " INTEGER not null)";
        db.execSQL(createStr);
    }

    public void insertTable(String barcodeID, String barcodeName,int barcodeCount){
        Log.e("insertTable","inserted");
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(table_column_barcodeID,barcodeID);
        values.put(table_column_barcodeName,barcodeName);
        values.put(table_column_count,barcodeCount);

        db.insert(table_name,null,values);
    }

    public Cursor readFromTable(String selection,String where){
        Log.e("readFrom","read");
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT "+selection+" FROM "+table_name +" " + where,null);
        Log.e("count","count = " +cursor.getCount());

        return cursor;
    }

    public void updateTable(int barcodeCount, String id){
        Log.e("update","update");
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(table_column_count, barcodeCount);

        db.update(table_name,values,table_column_barcodeID+"=?", new String[]{id});
        Log.e("update2","updated");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("oncreate","created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + table_name);
        onCreate(db);
    }
}