package com.example.wasapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class ClientLoopTask extends AsyncTask<Void, Void, Void> {
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
            instance.notificationText = notificationText;
        }
        return instance;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (!isCancelled()) { // we run as long as we're not cancelled...
            // update temperature
            TempStruct tempStruct;
            try {
                tempStruct = TemperatureRetriever.getTemperatures();
            } catch (Exception e) {
                notificationText.setText(String.valueOf(errorText));
                continue; // try again immediately
            }

            interfaceUpdater.updateInterface(settings, tempStruct.exteriorTemperature,
                    tempStruct.interiorTemperature);

            try {
                Thread.sleep(5000); // sleep 5 seconds until next iteration...
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
