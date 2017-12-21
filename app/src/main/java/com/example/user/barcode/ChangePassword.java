package com.example.user.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePassword extends AppCompatActivity {
    UserSessionManager session;
    UserDb db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSessionManager(getApplicationContext());
        setContentView(R.layout.activity_change_password);
        Log.e("change","1");

      //  TextView txt=(TextView) findViewById(R.id.test);
        this.db=new UserDb(this);
      //  txt.setText(session.getUserEmail());


    }

    public void Change(View view) {

        Log.e("change","2");
        Log.e("change","3");

        TextView errors=(TextView) findViewById(R.id.errors) ;
        errors.setText("");

        String email=session.getUserEmail();
        String oldpassword=((EditText) findViewById(R.id.oldPass)).getText().toString().trim();
        String newPassword=((EditText) findViewById(R.id.newPass)).getText().toString().trim();
        String confPassword=((EditText) findViewById(R.id.reNewpass)).getText().toString().trim();
        Log.v("name","djdjjd");

        if(oldpassword.isEmpty() || newPassword.isEmpty() || email.isEmpty() || confPassword.isEmpty() ) {
            errors.setText("All fields are required");
        }
        else{

                if(!newPassword.equals(confPassword)){
                    errors.setText("Passwords do not match");
                }
                else{
                    boolean success=  db.ChangePassword(email,oldpassword,newPassword);

                    if(!success){
                        errors.setText("Old password doesn't belong to this account.");
                    }
                    else{
                        TextView successText=(TextView) findViewById(R.id.success) ;
                        successText.setText("Password is changed successfully");
                        ((EditText) findViewById(R.id.oldPass)).setText("");
                        ((EditText) findViewById(R.id.newPass)).setText("");
                        ((EditText) findViewById(R.id.reNewpass)).setText("");
                    }
                }



        }
    }
}
