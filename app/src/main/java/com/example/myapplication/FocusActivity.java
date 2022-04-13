package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

public class FocusActivity extends AppCompatActivity {
    private FocusLifeCycleObserver lifecycleObserver;
    private TimeSegment timeSegment;
    private TextView mMotivation;
    private Button mExitButton;
    private Button mOpenAppsButton;
    private TimeViewModel timeViewModel;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        setResult(RESULT_OK);

        mExitButton = findViewById(R.id.exit_button);
        mExitButton.setOnClickListener(view -> new ExitFocusDialogFragment(result -> {
            if (result) {
                lifecycleObserver.disable();
                finish();
            }
        }).show(getSupportFragmentManager(), "Exit Focus"));

        mOpenAppsButton = findViewById(R.id.open_app);        mOpenAppsButton.setOnClickListener(view -> {
            lifecycleObserver.setUsingWhiteListApp(true);
            startActivity(new Intent(this, WhitelistActivity.class));
        });

        mMotivation = findViewById(R.id.motivation);
        mMotivation.setText(SharedData.getInstance().getMotivationMessage());


        lifecycleObserver = new FocusLifeCycleObserver(this);
        getLifecycle().addObserver(lifecycleObserver);
        lifecycleObserver.enable();


        mOpenAppsButton.setOnClickListener(view -> {
            lifecycleObserver.setUsingWhiteListApp(true);
            startActivity(new Intent(this, WhitelistActivity.class));
        });

        timeSegment = findViewById(R.id.time_segment);
        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);
        timeViewModel.getLeftTimeSecs().observe(this, t -> timeSegment.setTimeInSecs(t));
        startTimer();
    }

    void startTimer() {
        timer = new Timer();
        timer.setTotalTimeInSec(SharedData.getInstance().getFocusTimeSecs());
        timer.setOnTick(t -> {
            timeViewModel.getLeftTimeSecs().setValue(t);
            Log.i("TimeSegment", ""+t);
        });
        timer.setOnFinish(() -> {
                lifecycleObserver.disable();
                finish();
        });
        timer.start();
    }


    @Override
    public void onBackPressed() {}

}

class FocusLifeCycleObserver implements DefaultLifecycleObserver{
    private boolean shut;
    private boolean usingWhiteListApp;
    private boolean enabled;
    private final Context context;
    private SystemButtonBR systemButtonBR;

    public FocusLifeCycleObserver(Context _context) {
//        disable();
        usingWhiteListApp = false;
        context = _context;
        systemButtonBR = new SystemButtonBR(this::restartFocus);
    }

    private void restartFocus() {

        if(!shut) return;
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
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(shut) context.startActivity(restartFocus);

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
    public void onCreate(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onCreate(owner);
        NotificationManagerCompat.from(context).cancelAll();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        shut = false;
        if(!enabled) return;
        usingWhiteListApp = false;
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        shut = true;
        if(!enabled) return;
        if(isScreenOn() && !usingWhiteListApp)  {
            restartFocus();
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        shut = true;
        disable();
    }

    boolean isScreenOn() {
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        return powerManager.isInteractive();
    }
}
