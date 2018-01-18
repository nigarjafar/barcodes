package com.example.user.barcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

public class LoginPage extends AppCompatActivity {
    UserDb db;
    UserSessionManager session;

    LoginButton loginButton;
    TextView status;
    CallbackManager callbackManager;
    ProgressDialog mDialog;
    ImageView imgAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        session = new UserSessionManager(getApplicationContext());
        this.db=new UserDb(this);
        imgAvatar= (ImageView) findViewById(R.id.avatar) ;
        status=(TextView)findViewById(R.id.status);
        callbackManager = CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.facebook_login);
        loginButton.setReadPermissions(Arrays.asList("public_profile,email,user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog=new ProgressDialog(LoginPage.this);
                mDialog.setMessage("Retrieving data...");
                mDialog.show();

                String accessToken=loginResult.getAccessToken().getToken();

                GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                       getData(object);
                    }
                });
                Bundle parameters=new Bundle();
                parameters.putString("fields","id,email,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

       if(AccessToken.getCurrentAccessToken()!=null){
            status.setText("Already logged in. ID:"+(AccessToken.getCurrentAccessToken().getUserId()) );
        }



    }

    private void getData(JSONObject object) {
        try{
            URL profile_piture=new URL("https://graph.facebook.com/"+object.getString("id")+"/pictures?width=250&height=250");
            Picasso.with(this).load(profile_piture.toString()).into(imgAvatar);
            status.setText(object.getString("email") );
        }
        catch (Exception e){

        }
    }


    public void toRegisterPage(View view) {
        Intent registerPage = new Intent(this, RegistrationPage.class);
        startActivity(registerPage);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
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

    public void deleteStatus(View view) {
        status.setText("");
    }
}
