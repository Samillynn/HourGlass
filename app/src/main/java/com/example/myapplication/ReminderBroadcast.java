package com.example.myapplication;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class ReminderBroadcast extends BroadcastReceiver{

    @Override
    public void onReceive(android.content.Context context, Intent intent) {
        //to go back to app after clicking on notification
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        //main code
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyUser")
                .setSmallIcon(R.drawable. ic_launcher_foreground)
                .setAutoCancel(true) //for going back to app
                .setContentIntent(contentIntent) //for going back to app
                .setContentTitle("Reminder")
                .setContentText("Time to get back to work!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200,builder.build());

    }
}
