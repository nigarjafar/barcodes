package com.example.user.barcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {
    UserDb db;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        session = new UserSessionManager(getApplicationContext());
        this.db=new UserDb(this);
    }

    public void toRegisterPage(View view) {
        Intent registerPage = new Intent(this, RegistrationPage.class);
        startActivity(registerPage);
    }



    public void Login(View view) {
        String email=((EditText) findViewById(R.id.userEmail)).getText().toString().trim();
        String password=((EditText) findViewById(R.id.userPassword)).getText().toString().trim();

        if(db.Login(email,password)){
            Log.e("login","1");
          //   Storing login value as TRUE
        session.createUserLoginSession(email);
            Log.e("login","2");
        // Storing email in pref
            Log.e("login","3");

        Intent barcodeMenu = new Intent(this, BarcodeMenu.class);

            startActivity(barcodeMenu);

        }
        else{
            TextView errors=(TextView) findViewById(R.id.login_errors) ;
            errors.setText("Access Denied");
        }
    }
}
