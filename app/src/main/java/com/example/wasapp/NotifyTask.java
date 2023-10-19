package com.example.wasapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

public class NotifyTask extends AsyncTask<Void, Void, Void> {

    private long clock;
    private long lastTime;
    private TempStruct tempStruct;
    private Settings settings;
    private Notifier notifier;

    public NotifyTask(Context context) {
        clock = 3600 * 1000;
        lastTime = System.currentTimeMillis();
        settings = Settings.getInstance(context);
        notifier = new Notifier(context);
    }

    @Override
    protected Void doInBackground(Void[] voids) {

        while(!isCancelled()) {
            clock += (System.currentTimeMillis() - lastTime) / 1000;
            lastTime = System.currentTimeMillis();

            if (clock < settings.getMessageDelay()) {
                continue; // skip iteration
            } else {
                System.out.println(clock);
            }

            try {
                tempStruct = TemperatureRetriever.getTemperatures();
            } catch (IOException e) {
                // assume lost connection
                continue; // try again
            }

            if (tempStruct.interiorTemperature > tempStruct.interiorTemperature + settings.getTemperatureTolerance() &&
                    settings.getTargetTemperature() < tempStruct.interiorTemperature) {
                notifier.NotifyOpen(); // you stand to gain cooling by opening windows
                clock = 0; // reset the clock
            } else if (tempStruct.interiorTemperature > tempStruct.exteriorTemperature + settings.getTemperatureTolerance() &&
                    settings.getTargetTemperature() > tempStruct.interiorTemperature) {
                notifier.NotifyClose(); // you stand to retain heat by closing windows
                clock = 0;
            } else if (tempStruct.interiorTemperature < tempStruct.exteriorTemperature - settings.getTemperatureTolerance() &&
                    settings.getTargetTemperature() < tempStruct.interiorTemperature) {
                notifier.NotifyClose(); // you stand to retain cool air by closing windows
                clock = 0;
            } else if (tempStruct.interiorTemperature < tempStruct.exteriorTemperature - settings.getTemperatureTolerance() &&
                    settings.getTargetTemperature() > tempStruct.interiorTemperature) {
                notifier.NotifyOpen(); // you stand to gain heat by opening windows
                clock = 0;
            }

        }

        return null;
    }
}
