package com.example.myapplication.main_screen;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;
import com.example.myapplication.main_screen.MainActivity;
import com.example.myapplication.models.SharedData;


public class ReminderBroadcast extends BroadcastReceiver{

    @Override
    public void onReceive(android.content.Context context, Intent intent) {
        //to go back to app after clicking on notification
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        //main code
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyUser")
                .setSmallIcon(R.drawable. ic_launcher_foreground)
                .setAutoCancel(true) //for going back to app
                .setContentIntent(contentIntent) //for going back to app
                .setContentTitle("Reminder")
                .setContentText("Time to get back to work! " + SharedData.getInstance().getMotivationMessage())
                .setDefaults(NotificationCompat.DEFAULT_SOUND|NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200,builder.build());

    }
}
