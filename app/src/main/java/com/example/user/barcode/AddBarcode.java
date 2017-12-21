package com.example.user.barcode;

import android.content.Intent;
import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.widget.Toast.*;

public class AddBarcode extends AppCompatActivity{

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private EditText addBarcodeID;
    private EditText addBarcodeName;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barcode);

        addBarcodeID = (EditText) findViewById(R.id.addBarcodeID);
        addBarcodeName = (EditText) findViewById(R.id.addBarcodeName);

        addBarcodeID.setEnabled(false);
        addBarcodeID.setFocusable(false);

        this.db = new DBHelper(this);
        db.createTable();

//        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
//        surfaceHolder = surfaceView.getHolder();

    }

    public void backBarcodeMenu(View view) {
        Intent barcodeMenu = new Intent(this,BarcodeMenu.class);
        startActivity(barcodeMenu);
    }

    public void readBarcode(View view) {
        Log.e("add","read");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("SCAN");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        Log.v("beforeO","b");
        integrator.setOrientationLocked(false);
        Log.v("afterO","a");
        integrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result != null){
            if(result.getContents() != null){
                Log.v("resultScan",result.getContents());
                addBarcodeID.setText(result.getContents().toString());
            }else{
                Toast.makeText(this, "Scanning cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void saveBarcode(View view) {
        if(addBarcodeID.length() != 0 && addBarcodeName.length() != 0 ){
            db.insertTable(addBarcodeID.getText().toString(),addBarcodeName.getText().toString());
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            db.close();

            addBarcodeID.setText("");
            addBarcodeID.requestFocus();
            addBarcodeName.setText("");

        }else{
            Toast.makeText(this, "No empty field allowed", Toast.LENGTH_SHORT).show();
        }
    }
}