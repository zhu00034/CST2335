package com.example.xinji.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends Activity {
    //Lab6 -step 6
    private static final String URLString =
            "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";


    private static final String ACTIVITY_NAME="WeatherForecast";
    private ProgressBar progressBar;
    private TextView currentTemperature;
    private TextView minTemperature;
    private TextView maxTemperature;
    private TextView windSpeedText;
    private ImageView weather_imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        //Lab6 - step4
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        currentTemperature = (TextView)findViewById(R.id.currentTemperature);
        minTemperature = (TextView)findViewById(R.id.minTemperature);
        maxTemperature = (TextView)findViewById(R.id.maxTemperature);
        windSpeedText = (TextView)findViewById(R.id.windSpeed);
        weather_imageView = (ImageView)findViewById(R.id.weather_imageView);
        progressBar.setVisibility(View.VISIBLE);

        //to start the AsyncTask thread, create an object and execute it
        ForecastQuery forecast = new ForecastQuery();
        forecast.execute(URLString);

    }
    //Get Bitmap from Url with HttpURLConnection
    public static Bitmap getImage(URL url){

        Log.i(ACTIVITY_NAME, "In getImage");
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    //Lab6 -step 10
    //check if the image file exists
    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    //Lab6 -step 5
    private class ForecastQuery extends AsyncTask<String, Integer, String>{
        String currentTemp;
        String minTemp;
        String maxTemp;
        String windSpeed;
        Bitmap icon;
        String iconName;

        //Lab6 -step 7
        @Override
        protected String doInBackground(String... urls) {

            Log.i(ACTIVITY_NAME, " In doInBackGround");
            try {
                //set up connection
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                //instantiate the parser
                XmlPullParser parser = Xml.newPullParser();
                //the inputStream object is the one we get from the HttpURLConnection
                InputStream stream = conn.getInputStream();
                parser.setInput(stream, null);

                //Lab6 -step 8,9
                //iterate through the xml tags
                while(parser.next() != XmlPullParser.END_DOCUMENT){
                    if(parser.getEventType() != XmlPullParser.START_TAG){
                        continue;
                    }
                    String name = parser.getName();
                    //start looking for the entry tag
                    if(name.equals("temperature")){
                        currentTemp = parser.getAttributeValue(null, "value" );
                        publishProgress(25);
                        minTemp = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        maxTemp = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    }
                    if(name.equals("speed")){
                        windSpeed = parser.getAttributeValue(null, "value");
                        publishProgress(85);
                    }
                    if(name.equals("weather")){
                        iconName = parser.getAttributeValue(null, "icon");
                        String iconFileName = iconName + ".png";

                        //if image file exists, read it from the disk
                        if(fileExistance(iconFileName)){
                            FileInputStream inputStream = null;
                            try{
                                inputStream = openFileInput(iconFileName);
                            }catch(FileNotFoundException e){
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(inputStream);
                            Log.i(ACTIVITY_NAME + iconFileName, "Image is found locally");
                        }
                        //if image file doesnot exists, re-download it, build the url for the icons
                        //icons show cloudy, sunny, or raining etc
                        else{
                            URL imageURL = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            icon = getImage(imageURL);
                            //Lab6 -step 10
                            //save the Bitmag object to local application storage
                            FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME+iconFileName, "Image is downloaded");

                        }
                        //call publishProgress() to show that progress is completed
                        publishProgress(100);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        //Lab6 -step 11
        @Override
        protected void onProgressUpdate(Integer ...value){

            Log.i(ACTIVITY_NAME, "in onProgressUpdate");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);

        }
        //Lab6 -step 12
        //update the GUI components with the min,max,currentTemp and windspeed read from XML
        //update the Imageview with the Bitmap that you downloaded
        @Override
        protected void onPostExecute(String result){
            Log.i(ACTIVITY_NAME ,"In onPostExecute");
            currentTemperature.setText("current:" + currentTemperature.getText() + " " + currentTemp + " \u2103");
            minTemperature.setText("min:" +minTemperature.getText()+ " " + minTemp + " \u2103");
            maxTemperature.setText("max:" + maxTemperature.getText() + " " + maxTemp + " \u2103");
            windSpeedText.setText("Wind:" + windSpeedText.getText() + " " + windSpeed + " km/h");
            weather_imageView.setImageBitmap(icon);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }
}

