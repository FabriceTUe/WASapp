package com.example.wasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class ConfigureActivity extends AppCompatActivity implements View.OnClickListener {

    EditText exteriorUrlText;
    EditText interiorUrlText;
    Button setButton;
    Settings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        // load settings:
        settings = Settings.getInstance(this);

        // Getting handle on UI elements:
        exteriorUrlText = findViewById(R.id.interiorUrlEditText);
        interiorUrlText = findViewById(R.id.exteriorUrlEditText);
        setButton = findViewById(R.id.setButton);

        // set the text to current url:
        exteriorUrlText.setText(settings.getExteriorUrl());
        interiorUrlText.setText(settings.getInteriorUrl());

        // register listener with button:
        setButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        // get input:
        String exteriorUrl = exteriorUrlText.getText().toString();
        String interiorUrl = interiorUrlText.getText().toString();

        if (!exteriorUrl.startsWith("http://") || !interiorUrl.startsWith("http://")) {
            Toast.makeText(this, "Please enter a valid URL.", Toast.LENGTH_LONG).show();
            return;
        }

        // save input:
        settings.setExteriorUrl(exteriorUrl);
        settings.setInteriorUrl(interiorUrl);
        settings.save(this);

        finish(); // kill this activity and return to previous
    }

    @Override
    public void onBackPressed() {

    }
}