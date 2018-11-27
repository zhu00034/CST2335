package com.example.xinji.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME="StartActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // Lab3 - step 3
        Log.i(ACTIVITY_NAME, "In onCreate()");

        // Lab3 - step 7
        Button startButton = (Button) findViewById(R.id.button);

        // Lab 4 - step 13
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 50);
            }
        });

        // Lab4 - step 2
        Button chatButton = (Button) findViewById(R.id.startChart_button);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);

                startActivity(intent);
            }
        });

        Button weather_button = (Button)findViewById(R.id.weather_button);
        weather_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                startActivity(intent);
            }
        });

        // Lab 8 - step 7, create a test toolbar button
        Button toolbar_button = (Button)findViewById(R.id.toolbar_button);
        toolbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicks Test Toolbar");
                Intent intent = new Intent(StartActivity.this, TestToolbar.class);
                startActivity(intent);
            }
        });
}
    // Lab3 - step 6
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data){

        // Lab -step 11
        if(requestCode == 50 && responseCode == Activity.RESULT_OK){
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");

            Toast toast = Toast.makeText(this, messagePassed, Toast.LENGTH_LONG );
            toast.show();
        }
    }


    // Lab3 - step 2
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
