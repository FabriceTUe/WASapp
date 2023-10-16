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
    private static Notifier notifier;

    // Singleton:
    private static ClientLoopTask instance;
    private ClientLoopTask() {}
    public static synchronized ClientLoopTask getInstance(Context context) {
        if (instance == null) {
            instance = new ClientLoopTask();
            instance.context = context;
            notifier = new Notifier(context);
            instance.errorText =
                    "Check sensor connection.";
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

            if (temperature < targetTemperature &&
            secondsSinceLastNotification > 30 &&
            !hasBeenNotified) {
                secondsSinceLastNotification = 0;
                notifier.NotifyClose();
            } else if (temperature > targetTemperature &&
            secondsSinceLastNotification > 30 &&
            !hasBeenNotified) {
                secondsSinceLastNotification = 0;
                notifier.NotifyOpen();
            }

            try {
                Thread.sleep(5000); // sleep 5 seconds until next iteration...
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
