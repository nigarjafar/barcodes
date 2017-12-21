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

    public DBHelper(Context context) {
        super(context, db_name, null, version);
    }

    public void createTable(){
        Log.v("createTable","created");
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("drop table if exists " + table_name);
        String createStr = "create table if not exists " + table_name
                + " (" + table_column_barcodeID + " TEXT primary key, "
                + table_column_barcodeName + " TEXT not null)";
        db.execSQL(createStr);
    }

    public void insertTable(String barcodeID, String barcodeName){
        Log.v("insertTable","inserted");
        SQLiteDatabase db = getWritableDatabase();
//        String insertStr = "insert into table " + table_name + " values('"+barcodeID+"','"+barcodeName+"');";
//        db.execSQL(insertStr);
        ContentValues values = new ContentValues();
        values.put(table_column_barcodeID,barcodeID);
        values.put(table_column_barcodeName,barcodeName);

        db.insert(table_name,null,values);
    }

    public Cursor readFromTable(String whereBarcodeID){
        Log.v("readFrom","read");
        SQLiteDatabase db = getWritableDatabase();
        Log.v("readFrom","read1111");

        //Cursor cursor = db.rawQuery("SELECT * FROM "+table_name+" WHERE "+table_column_barcodeID+" = \'"+whereBarcodeID+"\'",null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+table_name,null);

        Log.v("readFrom","read2222222");
       // db.close();
        Log.v("readFrom","read333333");
        if(cursor.getCount()==1){
            Log.v("readFrom","ddd");
        }

        Log.v("readFrom",""+cursor.getCount());
        return cursor;
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