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
    private ClientLoopTask clientLoopTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set content of activity:
        setContentView(R.layout.activity_main);
        // Get handle on textview:
        TextView interiorTemperatureText = findViewById(R.id.interiorTemperatureText);
        TextView exteriorTemperatureText = findViewById(R.id.exteriorTemperatureText);
        TextView targetTemperatureText = findViewById(R.id.targetTemperatureText);
        TextView notificationText = findViewById(R.id.notificationText);
        Settings settings = Settings.getInstance();

        clientLoopTask = ClientLoopTask.getInstance(this, interiorTemperatureText,
                exteriorTemperatureText, targetTemperatureText, notificationText, settings);
        clientLoopTask.execute();
    }

    @Override
    protected void onDestroy() {
        clientLoopTask.cancel(true); // close down the background task...
        super.onDestroy();
    }
}