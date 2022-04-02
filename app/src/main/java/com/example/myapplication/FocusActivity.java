package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

public class FocusActivity extends AppCompatActivity {
    private FocusLifeCycleObserver lifecycleObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        getLifecycle().addObserver(new LifecycleLogger("Lifecycle", "FocusActivity"));

        lifecycleObserver = new FocusLifeCycleObserver(this, getLifecycle());
        getLifecycle().addObserver(lifecycleObserver);
        lifecycleObserver.enable();

        findViewById(R.id.exit_button).setOnClickListener(view -> {
            lifecycleObserver.disable();
            finish();
        });

        ((Button)findViewById(R.id.whitelist)).setOnClickListener(view -> {
            lifecycleObserver.setUsingWhiteListApp(true);
            startActivity(new Intent(this, WhitelistActivity.class));
        });

    }


    @Override
    public void onBackPressed() {}
}

class FocusLifeCycleObserver implements DefaultLifecycleObserver{
    private boolean usingWhiteListApp;
    private boolean enabled;
    private final Context context;
    private SystemButtonBR systemButtonBR;
    private Lifecycle lifecycle;

    public FocusLifeCycleObserver(Context _context, Lifecycle lifecycle) {
//        disable();
        usingWhiteListApp = false;
        context = _context;
        systemButtonBR = new SystemButtonBR(this::restartFocus);
        this.lifecycle = lifecycle;
    }

    private void restartFocus() {

        if(lifecycle.getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            return;
        }
        Intent restartFocus = new Intent(context, FocusActivity.class);
        restartFocus.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent restartPending = PendingIntent.getActivity(context, 0, restartFocus, PendingIntent.FLAG_IMMUTABLE);
        try {
            restartPending.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        // if the pending intent does not work
        // especially when home is pressed

        if(!lifecycle.getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.startActivity(restartFocus);
        }

        Log.i("Lifecycle", "Focus Restarted");
    }

    public void enable() {
        this.enabled = true;
        context.registerReceiver(systemButtonBR, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    public void disable() {
        this.enabled = false;
        try {
            context.unregisterReceiver(systemButtonBR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUsingWhiteListApp(boolean usingWhiteListApp) {
        this.usingWhiteListApp = usingWhiteListApp;
    }


    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        if(!enabled) return;
        usingWhiteListApp = false;
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if(!enabled) return;
        if(isScreenOn() && !usingWhiteListApp)  {
            restartFocus();
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        disable();
    }

    boolean isScreenOn() {
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        return powerManager.isInteractive();
    }

}
