package com.example.wasapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    // create client to send/receive web requests/responses:
    private OkHttpClient httpClient = new OkHttpClient();
    private String url = "https://192.168.43.82/temperature"; // store url of ESP8266
    private String temperature;
    private ClientLoopTask notifierTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set content of activity:
        setContentView(R.layout.activity_main);
        // Get handle on textview:
        TextView temperatureText = findViewById(R.id.temperatureText);
        notifierTask = ClientLoopTask.getInstance(this);
        notifierTask.setTargetTemperature(-1);
        notifierTask.setNotificationText(temperatureText);
        notifierTask.execute();

//        // Get temperature by querying ESP8266:
//        RetrieveTemperatureTask temperatureTask =
//                new RetrieveTemperatureTask(); // create task to get temperature
//        temperatureTask.execute();
//        try {
//            temperature = temperatureTask.get(); // actually get the temperature
//            Log.d("DEBUG:", temperature);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        temperatureText.setText(temperature); // set the text to temperature
    }

    @Override
    protected void onDestroy() {
        notifierTask.cancel(true); // close down the background task...
        super.onDestroy();
    }
}