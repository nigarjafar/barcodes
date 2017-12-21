package com.example.user.barcode;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ReadBarcode extends AppCompatActivity {


    private EditText readBarcodeID;
    private EditText readBarcodeName;
    private ArrayList<String> barcodeIDList;
    private ArrayList<String> barcodeNameList;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_barcode);

        readBarcodeID = (EditText) findViewById(R.id.readBarcodeID);
        readBarcodeName = (EditText) findViewById(R.id.readBarcodeName);


        Log.v("dbcreate","create");
        db = new DBHelper(this);

        readBarcodeID.setEnabled(false);
        readBarcodeID.setFocusable(false);

        readBarcodeName.setEnabled(false);
        readBarcodeName.setFocusable(false);

    }

    public void backBarcodeMenu(View view) {
        Intent barcodeMenu = new Intent(this,BarcodeMenu.class);
        startActivity(barcodeMenu);
    }

    public void findBarcode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("SCAN");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        Log.v("beforeOooo","b");
        integrator.setOrientationLocked(false);
        Log.v("afterO","a");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("finding","1");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        String whereBarcodeID = result.getContents();
        Log.e("finding","2");

        if(result!=null){
            Log.e("finding","3");
            if(result.getContents() != null){
                Log.e("finding","4");
                barcodeIDList = new ArrayList<>();
                barcodeNameList = new ArrayList<>();
                Log.e("finding","jdjjd");
                Cursor datalar = db.readFromTable(whereBarcodeID);
                Log.e("finding","jdjjoooooood");
                Log.e("log",""+datalar.getCount());
                if(datalar.moveToFirst()){
                    Log.e("finding","5");
                    do{
                        Log.e("finding","6");
                        String b_ID = datalar.getString(datalar.getColumnIndex(DBHelper.table_column_barcodeID));
                        String b_name = datalar.getString(datalar.getColumnIndex(DBHelper.table_column_barcodeName));

                        barcodeIDList.add(b_ID);
                        barcodeNameList.add(b_name);
                    }while (datalar.moveToNext());
                    Log.e("finding","7");
                    if(barcodeIDList.contains(result.getContents())){
                        Log.e("finding","8");
                        int index = barcodeIDList.indexOf(result.getContents());
                        Log.e("finding","asdfh");
                        readBarcodeID.setText(result.getContents());
                        readBarcodeName.setText(barcodeNameList.get(index));
                        Log.e("finding","9");
                    }

                    else{
                        Toast.makeText(this, "Barcode cannot find", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "Barcode cannot find", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Scan stopped", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}