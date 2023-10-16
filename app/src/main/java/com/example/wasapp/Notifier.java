package com.example.wasapp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class Notifier {
    Context context;
    String channelID;
    String channelName;
    String channelDescription;

    public Notifier(Context context) {
        this.context = context;
        channelID = "WASChannel";
        channelName = "WAS Channel";
        channelDescription = "Window Alarm System notifications";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // check if channesl are needed
            int importance = NotificationManager.IMPORTANCE_DEFAULT; // set def. importance
            NotificationChannel channel =
                    new NotificationChannel(channelID,
                            channelName, importance); // create a channel obj
            channel.setDescription(channelDescription); // set channel descr.
            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class); // create notification manager
            notificationManager.createNotificationChannel(channel); // use manager to create channel
        }
    }

    private void sendNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define.
        try {
            notificationManager.notify(0, builder.build());
            // we leave notification ID at 0 because we do not need to manage multiple notifications...
        } catch (SecurityException e) {
            Log.println(Log.INFO, "stdout", "Skipping notification because of insufficient permissions...");
        }
    }

    public void NotifyOpen() {
        sendNotification("WAS: open your windows",
                "To save on the energy bill and the environment, " +
                        "it is best to open your windows.");
    }

    public void NotifyClose() {
        sendNotification("WAS: close your windows",
                "To save on the energy bill and the environment, " +
                        "it is best to close your windows.");
    }
}
