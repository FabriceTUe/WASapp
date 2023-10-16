package com.example.wasapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class ClientLoopTask extends AsyncTask<Void, Void, Void> {
    private float targetTemperature;
    private boolean hasBeenNotified;
    private int secondsSinceLastNotification;
    private static long elapsed;
    private String errorText;
    private TextView notificationText;
    private Context context;
    private Notifier notifier;
    private InterfaceUpdater interfaceUpdater;
    private Settings settings;

    // Singleton:
    private static ClientLoopTask instance;
    private ClientLoopTask() {}
    public static synchronized ClientLoopTask getInstance(Context context,
                                                          TextView interiorTemperatureText,
                                                          TextView exteriorTemperatureText,
                                                          TextView targetTemperatureText,
                                                          TextView notificationText,
                                                          Settings settings) {
        if (instance == null) {
            instance = new ClientLoopTask();
            instance.context = context;
            instance.notifier = new Notifier(context);
            instance.interfaceUpdater = new InterfaceUpdater(context,
                    interiorTemperatureText, exteriorTemperatureText,
                    targetTemperatureText, notificationText);
            instance.errorText =
                    "Check sensor connection.";
            instance.settings = settings;
            elapsed = System.currentTimeMillis();
        }
        return instance;
    }

    public void setTargetTemperature(float targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public void setNotificationText(TextView notificationText) {
        this.notificationText = notificationText;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) { // we run in an infinite loop...
            // update clock
            secondsSinceLastNotification += (System.currentTimeMillis() - elapsed) / 1000;
            elapsed = System.currentTimeMillis();

            // update temperature
            float temperature;
            try {
                temperature = TemperatureRetriever.retrieveTemperature();
            } catch (Exception e) {
                notificationText.setText(String.valueOf(errorText));
                continue; // try again immediately
            }

            interfaceUpdater.updateInterface(settings, temperature,
                    10, secondsSinceLastNotification); // update interface

            try {
                Thread.sleep(5000); // sleep 5 seconds until next iteration...
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
