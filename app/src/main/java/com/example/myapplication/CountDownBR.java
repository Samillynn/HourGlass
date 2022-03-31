package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CountDownBR extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(CountDownService.TAG, "CountDownBR received");
        context.startService(new Intent(context, CountDownService.class).putExtra("countdown", 10*1000));
    }
}
