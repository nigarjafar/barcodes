package com.example.user.barcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationPage extends AppCompatActivity {
    UserDb db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.db=new UserDb(this);
        this.db.CreateTable();

    }


    public void Register(View view) {
        Log.e("register","register");

        TextView errors=(TextView) findViewById(R.id.errors) ;
        errors.setText("");

        String name=((EditText) findViewById(R.id.newUserName)).getText().toString().trim();
        String surname=((EditText) findViewById(R.id.newUserSurname)).getText().toString().trim();
        String email=((EditText) findViewById(R.id.newUserEmail)).getText().toString().trim();
        String password=((EditText) findViewById(R.id.newUserPassword)).getText().toString().trim();
        String passwordConfirm=((EditText) findViewById(R.id.newUserPasswordConfirmations)).getText().toString().trim();
        Log.v("name",name);

        if(name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            errors.setText("All fields are required");
        }
        else{
            if(!isValidEmail(email)){
                errors.setText("Email address is not valid.");
            }
            else{
                if(!password.equals(passwordConfirm)){
                    errors.setText("Passwords do not match");
                }
                else{
                   boolean success= this.db.Register(name,surname,email,password);
                    if(!success){
                        errors.setText("This email address is already registered .");
                    }
                    else{
                        Intent intent = new Intent(this, LoginPage.class);
                        startActivity(intent);
                    }
                }

            }

        }

    }

    public void view(View view) {


        Log.e("register","vew1");
        StringBuilder users=this.db.Logint("nigar.c@code.edu.az","123456");
        Toast.makeText(this,users ,
                Toast.LENGTH_SHORT).show();

        Log.e("register","view2");
        users=this.db.Logint("nigar.c@code.edu.az","1234s56");
        Toast.makeText(this,users ,
                Toast.LENGTH_SHORT).show();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
