package com.example.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    final static String sharedPreferencesFile = "com.example.myapplication";
    static SharedPreferences sharedPreferences;
    private long timeCountInMilliSeconds = 0;

    private TextView textViewInstruction;
    private ProgressBar progressBar;
    private TextView textViewTime;
    private EditText editTextMinute;
    private ImageButton startButton;
    private TextView bottomButtonText;
    private ImageButton finishRestButton;
    private ImageView settingButton;
    private Timer timer;
    private ImageButton bottomButton;

    private final String logTag = "Function called";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(sharedPreferencesFile, MODE_PRIVATE);
        getLifecycle().addObserver(new LifecycleLogger("Lifecycle", getClass().getName()));

        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        initViews();
        initListeners();
        finishRestListeners();
    }

    private void initViews() {
        Log.i(logTag, "initViews");
        textViewInstruction = (TextView) findViewById(R.id.textViewInstruction);
        String textInstruction = getString(R.string.set_relax);
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textViewInstruction.setText(spannable);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextMinute = (EditText) findViewById(R.id.editTextMinute);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        startButton = (ImageButton) findViewById(R.id.startButton);
        bottomButtonText = (TextView) findViewById(R.id.bottom_button_text);
        finishRestButton = (ImageButton) findViewById(R.id.finishRestButton);
        settingButton = (ImageView) findViewById(R.id.setting_button);
        bottomButton = (ImageButton) findViewById(R.id.button);
    }

    private void initListeners() {
        Log.i(logTag, "initListeners");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        settingButton.setOnClickListener(
                view -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void start() {
        Log.i(logTag, "start");
        setTimerValues();
        setProgressBarValues();
        startCountDownTimer();
        textViewInstruction.setText(R.string.ins_after_rest);
        bottomButtonText.setText(R.string.stop_rest);
        startButton.setVisibility(View.GONE);
        finishRestButton.setVisibility(View.VISIBLE);
    }

    public void startTimer() {}

    private void finishRestListeners() {
        Log.i(logTag, "finishRestListeners");
        finishRestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFocus();
                finishRestButton.setOnClickListener(
                        view2 -> startActivity(new Intent(getApplicationContext(), FocusActivity.class))
                );
            }
        });
    }

    private void setFocus() {
        Log.i(logTag, "setFocus");
        // startButton.setVisibility(View.VISIBLE);
        // finishRestButton.setVisibility(View.GONE);
        textViewTime.setText(hmsTimeFormatter(0));
        String textInstruction = getString(R.string.set_focus);
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textViewInstruction.setText(spannable);
        bottomButtonText.setText(R.string.start_focus);
        timeCountInMilliSeconds = 0;
        setProgressBarValues();
        editTextMinute.setEnabled(true);
        timer.clear();
    }

    private void setTimerValues() {
        Log.i(logTag, "setTimerValues");
        int time = 0;
        if (!editTextMinute.getText().toString().isEmpty()) {
            // fetching value from edit text and type cast to integer
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            // toast message to fill edit text
            Toast.makeText(getApplicationContext(), "Fill the relax time", Toast.LENGTH_LONG).show();
        }
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = (long) time * 60 * 1000;
    }

    private void startCountDownTimer() {
        timer = new Timer();
        timer.setOnTick(t -> {
            textViewTime.setText(hmsTimeFormatter(t*1000L));
            progressBar.setProgress(t);
        });
        timer.setOnFinish(() -> editTextMinute.setEnabled(true));
        timer.setTotalTimeInSec((int)timeCountInMilliSeconds/1000);
        timer.start();
    }


    private void setProgressBarValues() {
        Log.i(logTag, "setProgressBarValues");
        progressBar.setMax((int) timeCountInMilliSeconds / 1000);
        progressBar.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    private String hmsTimeFormatter(long milliSeconds) {
        Log.i(logTag, "hmsTimeFormatter");
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }

    public void changeState(HomepageState state) {
        textViewInstruction.setText(state.getInstructionText());
        bottomButton.setOnClickListener(state.getButtonClickListener());
        bottomButtonText.setText(state.getButtonText());

        timer.clear();
        timer = state.getTimer();
        if (timer != null) startTimer();
    }
}

class WaitRestState extends HomepageState {

    WaitRestState(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    CharSequence getInstructionText() {
        String textInstruction = activity.getString(R.string.set_relax);
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    @Override
    CharSequence getButtonText() {
        return activity.getString(R.string.start_rest);
    }

    @Override
    View.OnClickListener getButtonClickListener() {
        return view -> activity.changeState(new RestState(activity));
    }

    @Override
    Timer getTimer() {
        return null;
    }
}

class RestState extends HomepageState {

    RestState(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    CharSequence getInstructionText() {
        return activity.getString(R.string.ins_after_rest);
    }

    @Override
    CharSequence getButtonText() {
        return activity.getString(R.string.stop_rest);
    }

    @Override
    View.OnClickListener getButtonClickListener() {
        return view -> activity.changeState(new WaitFocusState(activity));
    }

    @Override
    Timer getTimer() {
        Timer timer = new Timer();
        timer.setOnTick(t -> {
            ((ProgressBar)activity.findViewById(R.id.progressBar)).setProgress(t);
            ((TextView)activity.findViewById(R.id.textViewTime)).setText("" + t/60 + ":" + t%60);
        });

        timer.setOnFinish(() -> {
            activity.changeState(new WaitFocusState(activity));
        });
        return timer;
    }
}

class WaitFocusState extends HomepageState {

    WaitFocusState(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    CharSequence getInstructionText() {
        String textInstruction = activity.getString(R.string.set_focus);
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    @Override
    CharSequence getButtonText() {
        return activity.getString(R.string.start_focus);
    }

    @Override
    View.OnClickListener getButtonClickListener() {
        return view -> activity.startActivity(new Intent(activity, FocusActivity.class));
    }

    @Override
    Timer getTimer() {
        Timer timer = new Timer();
        timer.setTotalTimeInSec(300);
        timer.setOnTick(t -> {

            if (t == 180) {
                activity.sendBroadcast(new Intent(activity, ReminderBroadcast.class));
            }
            if (t == 0) {
                activity.startActivity(new Intent(activity, FocusActivity.class));
            }
        });
        return timer;
    }
}

abstract class HomepageState {
    MainActivity activity;
    HomepageState(MainActivity mainActivity) {
        activity = mainActivity;
    }
    abstract CharSequence getInstructionText();
    abstract CharSequence getButtonText();
    abstract View.OnClickListener getButtonClickListener();
    abstract Timer getTimer();
}
