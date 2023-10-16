package com.example.wasapp;

public class Settings {
    float targetTemperature;
    float temperatureTolerance;
    int messageDelay;

    // Singleton
    private static Settings instance;
    private Settings() {}
    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        return instance;
    }

    public void setTargetTemperature(float targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public void setTemperatureTolerance(float temperatureTolerance) {
        this.temperatureTolerance = temperatureTolerance;
    }

    public void setMessageDelay(int messageDelay) {
        this.messageDelay = messageDelay;
    }
}
