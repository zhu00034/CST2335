package com.example.xinji.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    private static final String ACTIVITY_NAME="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Lab3 - step 3
        Log.i(ACTIVITY_NAME, "In onCreate()");

        // Lab3 - step 4
        final EditText loginEmail = (EditText)findViewById(R.id.loginEmail);
        final SharedPreferences sharedPref =
                getSharedPreferences("info", Context.MODE_PRIVATE);
        String emailName = sharedPref.getString("DefaultEmail", "email@domain.com");
        loginEmail.setText(emailName);

        Button loginButton =(Button)findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                String myEmail = loginEmail.getText().toString();

                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("DefaultEmail",myEmail);

                editor.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);

                startActivity(intent);

            }
        });
    }

    // Lab3 -  step 2
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }



}
