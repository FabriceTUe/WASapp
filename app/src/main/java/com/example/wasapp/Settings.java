package com.example.wasapp;

public class Settings {
    private float targetTemperature;
    private float temperatureTolerance;
    private int messageDelay;

    // Singleton
    private static Settings instance;
    private Settings() {}
    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
            instance.targetTemperature = -1; // set defaults...
            instance.temperatureTolerance = 0;
            instance.messageDelay = 30;
        }

        return instance;
    }

    public void setTargetTemperature(float targetTemperature) {
        this.targetTemperature = targetTemperature;
    }
    public float getTargetTemperature() {
        return targetTemperature;
    }

    public void setTemperatureTolerance(float temperatureTolerance) {
        this.temperatureTolerance = temperatureTolerance;
    }
    public float getTemperatureTolerance() {
        return temperatureTolerance;
    }

    public void setMessageDelay(int messageDelay) {
        this.messageDelay = messageDelay;
    }
    public int getMessageDelay() {
        return messageDelay;
    }
}
