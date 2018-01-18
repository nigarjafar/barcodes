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

import java.util.ArrayList;

public class ReadBarcode extends AppCompatActivity {

    private EditText readBarcodeID;
    private EditText readBarcodeName;
    private ArrayList<String> barcodeIDList;
    private ArrayList<String> barcodeNameList;
    private DBHelper db;
    private ArrayList<Integer> barcodeCountList;
    private EditText readBarcodeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_barcode);

        readBarcodeID = (EditText) findViewById(R.id.readBarcodeID);
        readBarcodeName = (EditText) findViewById(R.id.readBarcodeName);
        readBarcodeCount = (EditText) findViewById(R.id.readBarcodeCount);

        readBarcodeID.setEnabled(false);
        readBarcodeID.setFocusable(false);
        readBarcodeName.setEnabled(false);
        readBarcodeName.setFocusable(false);
        readBarcodeCount.setEnabled(false);
        readBarcodeCount.setFocusable(false);

        db = new DBHelper(this);
        Log.e("dbcreate","create");

        getSupportActionBar().setTitle("Read Barcode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void findBarcode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("SCAN BARCODE");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public void dialogBuilder(final IntentResult scanResult){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to add new barcode?").setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("NO","NO");
                        dialogInterface.cancel();
                        readBarcodeID.setText("NOT FOUND");
                        readBarcodeName.setText("NOT FOUND");
                        readBarcodeCount.setText("NOT FOUND");
                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("YES","YES");
                        Intent addBarcode = new Intent(ReadBarcode.this,AddBarcode.class);
                        addBarcode.putExtra("barcodeID",scanResult.getContents());
                        startActivity(addBarcode);
                    }
                });

        AlertDialog alert = dialog.create();
        alert.setTitle("NEW BARCODE");
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        String b_id = result.getContents();

        if(result!=null){
            if(result.getContents() != null){
                barcodeIDList = new ArrayList<>();
                barcodeNameList = new ArrayList<>();
                barcodeCountList = new ArrayList<>();
                Cursor data = db.readFromTable("*","");
                Log.e("data","read");

                if(data.moveToFirst()){
                    Log.e("data","first");
                    do{
                        String b_ID = data.getString(data.getColumnIndex(DBHelper.table_column_barcodeID));
                        String b_name = data.getString(data.getColumnIndex(DBHelper.table_column_barcodeName));
                        int b_count = data.getInt(data.getColumnIndex(DBHelper.table_column_count));

                        barcodeIDList.add(b_ID);
                        barcodeNameList.add(b_name);
                        barcodeCountList.add(b_count);
                        Log.e("data","do");
                    }while (data.moveToNext());

                    if(barcodeIDList.contains(b_id.trim())){
                        int index = barcodeIDList.indexOf(b_id);
                        readBarcodeID.setText(b_id);
                        readBarcodeName.setText(barcodeNameList.get(index));
                        Log.e("set","setted1");
                        readBarcodeCount.setText(barcodeCountList.get(index).toString());
                        Log.e("set","setted2");
                    }

                    else{
                        Log.e("else2","dialog");
                        dialogBuilder(result);
                    }
                }
                else{
                    Log.e("else1","dialog");
                    dialogBuilder(result);
                }
            }
            else{
                Toast.makeText(this, "Scan stopped", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
}