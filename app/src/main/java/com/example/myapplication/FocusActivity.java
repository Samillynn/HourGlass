package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class FocusActivity extends AppCompatActivity {
    private FocusLifeCycleObserver lifecycleObserver;
    private TextView mCountdown;
    private CountDownTimer mCountDownTimer;
    private EditText mTimerInput;
    private Button mExitButton;
    private Button mOpenAppsButton;
    public static boolean isTimerRunning;
    private boolean timerRunning;
    private long timeLeftinMillis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        Intent intent = getIntent();
        mCountdown = findViewById(R.id.textViewCountdown);
        timeLeftinMillis = intent.getIntExtra("Time", 0);
        startTimer();
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FF8FC0FF"));

        getLifecycle().addObserver(new LifecycleLogger("Lifecycle", "FocusActivity"));

        lifecycleObserver = new FocusLifeCycleObserver(this);
        getLifecycle().addObserver(lifecycleObserver);
        lifecycleObserver.enable();

        mExitButton = findViewById(R.id.exit_button);
        mOpenAppsButton = findViewById(R.id.open_app);

        mExitButton.setBackgroundColor(Color.parseColor("#FFFFD27E"));
        mExitButton.setTextColor(Color.parseColor("#FF000000"));
        mOpenAppsButton.setBackgroundColor(Color.parseColor("#FFFFD27E"));
        mOpenAppsButton.setTextColor(Color.parseColor("#FF000000"));

        mExitButton.setVisibility(View.INVISIBLE);
        mExitButton.setOnClickListener(view -> {
            lifecycleObserver.disable();
            finish();


            //mCountdown.setVisibility(View.INVISIBLE);

        });

        mOpenAppsButton.setOnClickListener(view -> {
            lifecycleObserver.setUsingWhiteListApp(true);
            startActivity(new Intent(this, WhitelistActivity.class));
        });

        findViewById(R.id.exit_button).setVisibility(View.VISIBLE);
    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(timeLeftinMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", 0, 0, 0);
                mCountdown.setText(timeLeftFormatted);
                timerRunning = false;
                findViewById(R.id.exit_button).setVisibility(View.VISIBLE);
                //focusMode();
                isTimerRunning = false;

            }
        }.start();
        timerRunning = true;
        isTimerRunning = true;
        //mTimerInput.setVisibility(View.INVISIBLE);
    }

    public void updateCountDownText() {
        int hours = (int) (timeLeftinMillis / 1000) / 3600;
        int minutes = (int) (timeLeftinMillis / 1000) % 3600 / 60;
        int seconds = (int) (timeLeftinMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        mCountdown.setText(timeLeftFormatted);
    }

    @Override
    public void onBackPressed() {}


    public void openTimer(){
        Intent intent = new Intent(this, Timer.class);
        startActivity(intent);
    }
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
