package com.example.user.barcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddBarcode extends AppCompatActivity{

    private EditText addBarcodeID;
    private EditText addBarcodeName;
    private EditText addBarcodeCount;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barcode);

        addBarcodeID = (EditText) findViewById(R.id.addBarcodeID);
        addBarcodeName = (EditText) findViewById(R.id.addBarcodeName);
        addBarcodeCount = (EditText) findViewById(R.id.addBarcodeCount);

        addBarcodeID.setEnabled(false);
        addBarcodeID.setFocusable(false);

        this.db = new DBHelper(this);
        db.createTable();

        getSupportActionBar().setTitle("Add Barcode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String extraID = intent.getStringExtra("barcodeID");
        if(extraID != null){
            Log.e("extra","id = " +extraID);
            addBarcodeID.setText(extraID);
        }
    }

    public void readBarcode(View view) {
        Log.e("add","read");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("SCAN");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);

        if(result != null){
            if(result.getContents() != null){
                Log.v("resultScan",result.getContents());

                Cursor data = db.readFromTable("*","where "+DBHelper.table_column_barcodeID+"="+result.getContents());
                Log.e("data","data count: "+data.getCount());

                if(data.moveToFirst()){
                    Log.e("move first","basladi");
                    final String id = data.getString(data.getColumnIndex(DBHelper.table_column_barcodeID));
                    final String name = data.getString(data.getColumnIndex(DBHelper.table_column_barcodeName));
                    final int count = data.getInt(data.getColumnIndex(DBHelper.table_column_count));

                    Log.e("move first","bitti");

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    String message = id + " already exists with count "+ count+". Do you want to update count?";

                    builder.setMessage(message).setCancelable(false)
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.e("builder","no");
                                    dialogInterface.cancel();

                                    addBarcodeName.setFocusable(true);
                                    addBarcodeName.setEnabled(true);
                                }
                            })
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.e("builder","yes");
                                    addBarcodeID.setText(id);
                                    addBarcodeID.setEnabled(false);
                                    addBarcodeID.setFocusable(false);

                                    addBarcodeName.setText(name);
                                    addBarcodeName.setFocusable(false);
                                    addBarcodeName.setEnabled(false);

                                    addBarcodeCount.setText("");
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.setTitle("COUNT UPDATING");
                    dialog.show();
                }
                else{
                    Log.e("data","no");
                    addBarcodeID.setText(result.getContents());
                    addBarcodeName.setFocusable(true);
                    addBarcodeName.setEnabled(true);
                }

            }else{
                Toast.makeText(this, "Scanning cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void saveBarcode(View view) {
        if(addBarcodeID.length() != 0 && addBarcodeName.length() != 0 && addBarcodeCount.length() != 0){
            int barcode_count = Integer.parseInt(addBarcodeCount.getText().toString().trim());
            Log.e("barcode count",""+barcode_count);
            String where = "where "+ DBHelper.table_column_barcodeID + "="+ addBarcodeID.getText().toString();
            Log.e("db count",""+db.readFromTable("*",where).getCount());

            if(db.readFromTable("*",where).getCount() == 0){
                db.insertTable(addBarcodeID.getText().toString().trim(),addBarcodeName.getText().toString().trim(),barcode_count);
                Toast.makeText(this, "NEW BARCODE ADDED", Toast.LENGTH_SHORT).show();
                Log.e("insert","inserted");
                db.close();

                addBarcodeID.setText("");
                addBarcodeName.setText("");
                addBarcodeCount.setText("");

                addBarcodeName.requestFocus();
            }
            else{
                db.updateTable(barcode_count,addBarcodeID.getText().toString().trim());
                Toast.makeText(this, "BARCODE UPDATED", Toast.LENGTH_SHORT).show();
                Log.e("updatetable","updated");
                db.close();

                addBarcodeID.setText("");
                addBarcodeName.setText("");
                addBarcodeCount.setText("");

                addBarcodeName.requestFocus();
            }

        }else{
            Toast.makeText(this, "NO EMPTY FIELD ALLOWED", Toast.LENGTH_SHORT).show();
        }
    }
}