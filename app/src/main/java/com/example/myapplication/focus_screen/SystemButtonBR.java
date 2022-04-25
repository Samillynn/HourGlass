package com.example.myapplication.focus_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * A broadcast receiver which runs a callback whenever HOME or MENU button is pressed
 */
public class SystemButtonBR extends BroadcastReceiver {
    final String SYSTEM_DIALOG_REASON_KEY = "reason";
    final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    final Runnable callback;

    public SystemButtonBR(Runnable _callback) {
        callback = _callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            // run callback when HOME is pressed
            if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                Log.i("CountDown", "Home Pressed");
                callback.run();
            } else if(reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
            // run callback when RECENT_APPS(MENU) is pressed
                Log.i("CountDown", "Overview Pressed");
                callback.run();
            }
        }
    }
}
