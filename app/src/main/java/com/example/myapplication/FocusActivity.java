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
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

public class FocusActivity extends AppCompatActivity {
    private FocusLifeCycleObserver lifecycleObserver;
    private TimeSegment timeSegment;
    private TextView mMotivation;
    private Button mExitButton;
    private Button mOpenAppsButton;
    private TimerViewModel timerViewModel;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        //Linking the buttons
        mExitButton = findViewById(R.id.exit_button);
        mOpenAppsButton = findViewById(R.id.open_app);
        mMotivation = findViewById(R.id.motivation);
        
        //Setting an observer for lifecycle to monitor the stage the activity is currently in
        getLifecycle().addObserver(new LifecycleLogger("Lifecycle", "FocusActivity"));

        lifecycleObserver = new FocusLifeCycleObserver(this);
        getLifecycle().addObserver(lifecycleObserver);
        lifecycleObserver.enable();

        //Close button for timer (obsolete now)
        mExitButton.setOnClickListener(view -> {
            exit();
        });

        //Opening the whitelist apps on click
        mOpenAppsButton.setOnClickListener(view -> {
            lifecycleObserver.setUsingWhiteListApp(true);
            startActivity(new Intent(this, WhitelistActivity.class));
        });
        
        //Instantiating timer, observing the change in time and starting it
        timeSegment = findViewById(R.id.time_segment);
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        timerViewModel.getLeftTimeSecs().observe(this, t -> timeSegment.setTimeInSecs(t));
        startTimer();
    }
    
    //Starting timer countdown
    void startTimer() {
        timer = new Timer();
        //Retrieving time from settings
        timer.setTotalTimeInSec(SettingData.getInstance().getFocusTimeSecs());
        timer.setOnTick(t -> {
            timerViewModel.getLeftTimeSecs().setValue(t);
            Log.i("TimeSegment", ""+t);
        });
        timer.setOnFinish(this::exit);
        timer.start();
    }
    
    //Method to close timer
    void exit() {
        lifecycleObserver.disable();
        setResult(RESULT_OK);
        finish();
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
