package com.example.user.barcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class BarcodeMenu extends AppCompatActivity {
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSessionManager(getApplicationContext());
        setContentView(R.layout.activity_barcode_menu);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.Logout:
                Intent intent = new Intent(this, LoginPage.class);
                startActivity(intent);
                break;
            case R.id.ChangePassword:
                Intent changePass = new Intent(this, ChangePassword.class);
                startActivity(changePass);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void toAddBarcodePage(View view) {
        Intent addBarcodePage = new Intent(this, AddBarcode.class);
        startActivity(addBarcodePage);
    }

    public void toReadBarcodePage(View view) {
        Intent readBarcodePage = new Intent(this, ReadBarcode.class);
        startActivity(readBarcodePage);
    }

}
