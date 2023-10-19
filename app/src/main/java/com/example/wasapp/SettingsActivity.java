package com.example.wasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Slider temperatureBar;
    Slider toleranceBar;
    EditText delayEditText;
    Button applyButton;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get a handle on saved settings:
        settings = Settings.getInstance(this);

        // get a handle on UI elements:
        temperatureBar = findViewById(R.id.temperatureBar);
        toleranceBar = findViewById(R.id.toleranceBar);
        delayEditText = findViewById(R.id.delayEditText);
        applyButton = findViewById(R.id.applyButton);

        // register 'save settings' routine to apply button:
        applyButton.setOnClickListener(this);

        // set input elements to current values:
        temperatureBar.setValue(settings.getTargetTemperature());
        toleranceBar.setValue(settings.getTemperatureTolerance());
        delayEditText.setText(String.valueOf(settings.getMessageDelay()));
    }

    // subroutine that is executed on button press:
    @Override
    public void onClick(View view) {
        // fetch inputted values:
        float targetTemperature = temperatureBar.getValue();
        float temperatureTolerance = toleranceBar.getValue();
        int messageDelay;
        try {
            messageDelay = Integer.parseInt(delayEditText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Message delay must be integer.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // configure settings object:
        settings.setTargetTemperature(targetTemperature);
        settings.setTemperatureTolerance(temperatureTolerance);
        settings.setMessageDelay(messageDelay);
        settings.save(this); // save settings...

        finish(); // kill current activity and return to previous
    }

    @Override
    public void onBackPressed() {
        // ignore any back press (keep simple)
    }
}