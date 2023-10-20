package com.example.wasapp;

import android.content.Context;
import android.widget.TextView;

public class InterfaceUpdater {
    private Notifier notifier;
    private Context context;
    private TextView interiorTemperatureText;
    private TextView exteriorTemperatureText;
    private TextView targetTemperatureText;
    private TextView notificationText;

    private String openWindowsText = "Open your windows.";
    private String closeWindowsText = "Close your windows.";
    private String defaultText = "Change nothing.";

    private long lastTime;
    private long clock;

    public InterfaceUpdater(Context context, TextView interiorTemperatureText,
                            TextView exteriorTemperatureText,
                            TextView targetTemperatureText,
                            TextView notificationText) {
        this.context = context;
        this.interiorTemperatureText = interiorTemperatureText;
        this.exteriorTemperatureText = exteriorTemperatureText;
        this.targetTemperatureText = targetTemperatureText;
        this.notificationText = notificationText;
        this.lastTime = System.currentTimeMillis();
        this.clock = 0; // approximately corresponds to setting to '\infty'
        notifier = new Notifier(context);
    }

    public boolean updateInterface(Settings settings, float exteriorTemperature,
                                float interiorTemperature) {
        exteriorTemperatureText.setText("Exterior T: " + exteriorTemperature);
        interiorTemperatureText.setText("Interior T: " + interiorTemperature);
        targetTemperatureText.setText("Target T: " + settings.getTargetTemperature());

        clock += (System.currentTimeMillis() - lastTime) / 1000;
        lastTime = System.currentTimeMillis();

        if (interiorTemperature > exteriorTemperature + settings.getTemperatureTolerance() &&
        settings.getTargetTemperature() < interiorTemperature) {
            if (clock > settings.getMessageDelay()) {
                notifier.NotifyOpen(); // you stand to gain cooling by opening windows
                clock = 0; // reset the clock
            }
            notificationText.setText(openWindowsText);
            return true;
        } else if (interiorTemperature > exteriorTemperature + settings.getTemperatureTolerance() &&
        settings.getTargetTemperature() > interiorTemperature) {
            if (clock > settings.getMessageDelay()) {
                notifier.NotifyClose(); // you stand to retain heat by closing windows
                clock = 0;
            }
            notificationText.setText(closeWindowsText);
            return true;
        } else if (interiorTemperature < exteriorTemperature - settings.getTemperatureTolerance() &&
        settings.getTargetTemperature() < interiorTemperature) {
            if (clock > settings.getMessageDelay()) {
                notifier.NotifyClose(); // you stand to retain cool air by closing windows
                clock = 0;
            }
            notificationText.setText(closeWindowsText);
            return true;
        } else if (interiorTemperature < exteriorTemperature - settings.getTemperatureTolerance() &&
        settings.getTargetTemperature() > interiorTemperature) {
            if (clock > settings.getMessageDelay()) {
                notifier.NotifyOpen(); // you stand to gain heat by opening windows
                clock = 0;
            }
            notificationText.setText(openWindowsText);
            return true;
        }

        notificationText.setText(defaultText);
        return false;
    }
}
