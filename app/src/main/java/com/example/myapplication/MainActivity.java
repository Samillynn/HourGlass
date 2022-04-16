package com.example.myapplication;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


public class MainActivity extends AppCompatActivity {

    private TextView textViewInstruction;
    private TextView bottomButtonText;
    private ImageView settingButton;
    private Timer timer = new Timer();
    private ImageButton bottomButton;
    private TimeViewModel timeViewModel;
    private TimeSegment timeSegment;
    private CircularSeekBar circularSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new LifecycleLogger("Lifecycle", getClass().getName()));

        setContentView(R.layout.activity_main);
        SharedData.getInstance().initialize(this);
        init();

        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);
        timeViewModel.getTotalTimeSecs().observe(this, totalTime -> {
            circularSeekBar.setMax(totalTime);
        });

        timeViewModel.getLeftTimeSecs().observe(this, leftTimeSecs -> {
            circularSeekBar.setProgress(leftTimeSecs);
            timeSegment.setTimeInSecs(leftTimeSecs);
        });

        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if (fromUser) {
                    timeViewModel.getLeftTimeSecs().setValue((int) progress);
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {}
        });
        changeState(new WaitRestState(this, timeViewModel));
    }

    public void init() {
        initActionBar();
        initViews();
    }


    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
    }

    private void initViews() {
        textViewInstruction = (TextView) findViewById(R.id.textViewInstruction);
        timeSegment = (TimeSegment) findViewById(R.id.time_segment);
        bottomButtonText = (TextView) findViewById(R.id.bottom_button_text);
        bottomButton = (ImageButton) findViewById(R.id.button);
        circularSeekBar = (CircularSeekBar) findViewById(R.id.circular_seekbar);

        settingButton = (ImageView) findViewById(R.id.setting_button);
        settingButton.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    public void startFocus() {
        timer.clear();
        SharedData.getInstance().setFocusTimeSecs(timeViewModel.getLeftTimeSecs().getValue());
        startActivityForResult(new Intent(this, FocusActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        changeState(new WaitRestState(this, timeViewModel));
    }

    public void changeState(HomepageState state) {

        state.changeInstructionTextView(textViewInstruction);
        state.changeButtonTextView(bottomButtonText);
        state.changeButton(bottomButton);
        state.changeSeekbar(circularSeekBar);

        timer.clear();
        state.changeTimer(timer);
        timer.start();
    }
}

class WaitRestState extends HomepageState {

    WaitRestState(MainActivity activity, TimeViewModel model) {
        super(activity, model);
    }

    @Override
    void changeInstructionTextView(TextView view) {
        String textInstruction = activity.getString(R.string.set_relax);
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(spannable);
    }

    @Override
    void changeButtonTextView(TextView view) {
        view.setText(R.string.start_rest);
    }

    @Override
    void changeButton(View button) {
        button.setOnClickListener(view -> {
            model.getTotalTimeSecs().setValue(model.getLeftTimeSecs().getValue());
            activity.changeState(new RestState(activity, model));
        });
    }

    @Override
    void changeTimer(Timer timer) {
    }

    @Override
    void changeSeekbar(CircularSeekBar seekBar) {
        seekBar.setDisablePointer(false);
        model.getTotalTimeSecs().setValue(90 * 60);
        model.getLeftTimeSecs().setValue(SharedData.getInstance().getDefaultRestTimeSecs());
    }
}

class RestState extends HomepageState {


    RestState(MainActivity activity, TimeViewModel model) {
        super(activity, model);
    }

    @Override
    void changeInstructionTextView(TextView view) {
        view.setText(R.string.ins_after_rest);
    }

    @Override
    void changeButtonTextView(TextView view) {
        view.setText(R.string.stop_rest);
    }

    @Override
    void changeButton(View button) {
        button.setOnClickListener(view -> activity.changeState(new WaitFocusState(activity, model)));
    }

    @Override
    void changeTimer(Timer timer) {
        timer.setTotalTimeInSec(model.getTotalTimeSecs().getValue());
        timer.setOnTick(t -> model.getLeftTimeSecs().setValue(t));
        timer.setOnFinish(() -> {
            activity.sendBroadcast(new Intent(activity, ReminderBroadcast.class));
            activity.changeState(new WaitFocusState(activity, model));
        });
    }

    @Override
    void changeSeekbar(CircularSeekBar view) {
        view.setDisablePointer(true);
    }
}

class WaitFocusState extends HomepageState {
    final int TIME_BEFORE_FORCE_FOCUS_SECS = 120;

    WaitFocusState(MainActivity activity, TimeViewModel model) {
        super(activity, model);
    }

    @Override
    void changeInstructionTextView(TextView view) {
        String textInstruction = activity.getString(R.string.set_focus);
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(spannable);
    }

    @Override
    void changeButtonTextView(TextView view) {
        view.setText(R.string.start_focus);
    }

    @Override
    void changeButton(View button) {
        button.setOnClickListener(view -> activity.startFocus());
    }

    @Override
    void changeTimer(Timer timer) {
        int snoozeTimeSecs = SharedData.getInstance().getDefaultSnoozeTimeSecs();
        timer.setTotalTimeInSec(snoozeTimeSecs + TIME_BEFORE_FORCE_FOCUS_SECS);
        timer.setOnTick(t -> {
            if (t == TIME_BEFORE_FORCE_FOCUS_SECS) {
                activity.sendBroadcast(new Intent(activity, ReminderBroadcast.class));
            }
            if (t == 0) {
                activity.startActivity(new Intent(activity, FocusActivity.class));
            }
        });
    }

    @Override
    void changeSeekbar(CircularSeekBar seekBar) {
        seekBar.setDisablePointer(false);
        model.getTotalTimeSecs().setValue(90 * 60);
        model.getLeftTimeSecs().setValue(SharedData.getInstance().getDefaultFocusTimeSecs());
    }
}

abstract class HomepageState {
    MainActivity activity;
    TimeViewModel model;

    HomepageState(MainActivity activity, TimeViewModel model) {
        this.model = model;
        this.activity = activity;
    }

    abstract void changeInstructionTextView(TextView view);

    abstract void changeButton(View button);

    abstract void changeButtonTextView(TextView view);

    abstract void changeTimer(Timer timer);

    abstract void changeSeekbar(CircularSeekBar view);
}
