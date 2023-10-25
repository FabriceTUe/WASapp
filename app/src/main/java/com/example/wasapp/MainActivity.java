package com.example.wasapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        Button settingsButton = findViewById(R.id.settingsButton);
        Button configurationButton = findViewById(R.id.configureButton);

        // set the onclick listener for settings button (see method below)
        settingsButton.setOnClickListener(this);
        Settings settings = Settings.getInstance(this);

        // set onclick listener for config button:
        configurationButton.setOnClickListener(new ConfigButtonHandler());

        clientLoopTask = ClientLoopTask.getInstance(this, interiorTemperatureText,
                exteriorTemperatureText, targetTemperatureText, notificationText, settings);
        clientLoopTask.execute();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,
                SettingsActivity.class); // intend to switch to settings...
        startActivity(intent); // start the next activity.
    }

    @Override
    public void onBackPressed() {
        // ignore any back press (keep simple)
    }

    @Override
    protected void onDestroy() {
        clientLoopTask.cancel(true); // close down the background task...
        super.onDestroy();
    }

    private class ConfigButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getBaseContext(),
                    SettingsActivity.class); // intend to switch to settings...
            startActivity(intent); // start the next activity.
        }
    }
}