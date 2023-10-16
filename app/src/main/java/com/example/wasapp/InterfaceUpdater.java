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
    private String defaultText = "Window state optimal.";

    public InterfaceUpdater(Context context, TextView interiorTemperatureText,
                            TextView exteriorTemperatureText,
                            TextView targetTemperatureText,
                            TextView notificationText) {
        this.context = context;
        this.interiorTemperatureText = interiorTemperatureText;
        this.exteriorTemperatureText = exteriorTemperatureText;
        this.targetTemperatureText = targetTemperatureText;
        this.notificationText = notificationText;
        notifier = new Notifier(context);
    }

    public void updateInterface(Settings settings, float exteriorTemperature,
                                float interiorTemperature, int clock) {
        exteriorTemperatureText.setText("Exterior T: " + exteriorTemperature);
        interiorTemperatureText.setText("Interior T: " + interiorTemperature);
        targetTemperatureText.setText("Target T: " + settings.getTargetTemperature());

        if (clock < settings.getMessageDelay())
            return; // if the last message is too recent, immediately return

        if (interiorTemperature > exteriorTemperature + settings.getTemperatureTolerance() &&
        settings.getTargetTemperature() < interiorTemperature) {
            notifier.NotifyOpen(); // you stand to gain cooling by opening windows
            notificationText.setText(openWindowsText);
            return;
        } else if (interiorTemperature > exteriorTemperature + settings.getTemperatureTolerance() &&
        settings.getTargetTemperature() > interiorTemperature) {
            notifier.NotifyClose(); // you stand to retain heat by closing windows
            notificationText.setText(closeWindowsText);
            return;
        } else if (interiorTemperature < exteriorTemperature - settings.getTemperatureTolerance() &&
        settings.getTargetTemperature() < interiorTemperature) {
            notifier.NotifyClose(); // you stand to retain cool air by closing windows
            notificationText.setText(closeWindowsText);
            return;
        } else if (interiorTemperature < exteriorTemperature - settings.getTemperatureTolerance() &&
        settings.getTargetTemperature() > interiorTemperature) {
            notifier.NotifyOpen(); // you stand to gain heat by opening windows
            notificationText.setText(openWindowsText);
            return;
        }

        notificationText.setText(defaultText);
    }
}
