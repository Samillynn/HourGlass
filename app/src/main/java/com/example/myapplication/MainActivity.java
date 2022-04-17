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

        // inflate and initialize everything
        setContentView(R.layout.activity_main);
        SharedData.getInstance().initialize(this);
        init();

        // creates a TimeViewModel in the first time and return it
        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);
        // observe the updated total time from the timeViewModel and update to circularBar
        timeViewModel.getTotalTimeSecs().observe(this, totalTime -> {
            circularSeekBar.setMax(totalTime);
        });

        // observe the updated left time from the timeViewModel and update to circularBar & timeSegment
        timeViewModel.getLeftTimeSecs().observe(this, leftTimeSecs -> {
            circularSeekBar.setProgress(leftTimeSecs);
            timeSegment.setTimeInSecs(leftTimeSecs);
        });

        // listen the change of circularBar and update it to the timeViewModel
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

        // When the activity is created, enter the WaitRestState
        changeState(new WaitRestState(this, timeViewModel));
    }

    /** initialization */
    public void init() {
        initActionBar();
        initViews();
    }

    /** initialize the custom action bar */
    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.homepage_action_bar);
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

    /** After the "START FOCUS" button is pressed */
    public void startFocus() {
        // clear the previous timer data
        timer.clear();
        // get the updated focus time set from timeViewModel and update it to the SharedData pool so that the data can be accessed by the FocusActivity.
        SharedData.getInstance().setFocusTimeSecs(timeViewModel.getLeftTimeSecs().getValue());
        // Enter into the FocusActivity
        startActivityForResult(new Intent(this, FocusActivity.class), 0);
    }

    /** When coming back from other activities, the state will be changed to WaitRestState */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        changeState(new WaitRestState(this, timeViewModel));
    }

    /** update the visual elements based on the current state */
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

/** State1: WaitRestState */
class WaitRestState extends HomepageState {
    // Constructor
    WaitRestState(MainActivity activity, TimeViewModel model) {
        super(activity, model);
    }

    // Change the instruction text
    @Override
    void changeInstructionTextView(TextView view) {
        String textInstruction = activity.getString(R.string.set_relax);
        // Highlight the key words
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(spannable);
    }

    // Change the button text
    @Override
    void changeButtonTextView(TextView view) {
        view.setText(R.string.start_rest);
    }

    // Change the behaviors of the button
    @Override
    void changeButton(View button) {
        button.setOnClickListener(view -> {
            model.getTotalTimeSecs().setValue(model.getLeftTimeSecs().getValue());
            activity.changeState(new RestState(activity, model));
        });
    }

    // This state will not change the timer
    @Override
    void changeTimer(Timer timer) {
    }

    // Change the params of the circularBar
    @Override
    void changeSeekbar(CircularSeekBar seekBar) {
        seekBar.setDisablePointer(false);
        model.getTotalTimeSecs().setValue(90 * 60);
        model.getLeftTimeSecs().setValue(SharedData.getInstance().getDefaultRestTimeSecs());
    }
}

/** State1: WaitRestState */
class RestState extends HomepageState {

    // Constructor
    RestState(MainActivity activity, TimeViewModel model) {
        super(activity, model);
    }

    // Change the instruction text
    @Override
    void changeInstructionTextView(TextView view) {
        view.setText(R.string.ins_after_rest);
    }

    // Change the button text
    @Override
    void changeButtonTextView(TextView view) {
        view.setText(R.string.stop_rest);
    }

    // Change the behaviors of the button
    @Override
    void changeButton(View button) {
        button.setOnClickListener(view -> activity.changeState(new WaitFocusState(activity, model)));
    }

    // Change the params of the timer and its related behaviors
    @Override
    void changeTimer(Timer timer) {
        timer.setTotalTimeInSec(model.getTotalTimeSecs().getValue());
        timer.setOnTick(t -> model.getLeftTimeSecs().setValue(t));
        // if time up, a pop-up reminder will be sent to the user and the state will be changed
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

/** State1: WaitFocusState */
class WaitFocusState extends HomepageState {
    final int TIME_BEFORE_FORCE_FOCUS_SECS = 30;

    // Constructor
    WaitFocusState(MainActivity activity, TimeViewModel model) {
        super(activity, model);
    }

    // Change the instruction text
    @Override
    void changeInstructionTextView(TextView view) {
        String textInstruction = activity.getString(R.string.set_focus);
        // highlight the key words
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(spannable);
    }

    // Change the button text
    @Override
    void changeButtonTextView(TextView view) {
        view.setText(R.string.start_focus);
    }

    // Change the behaviors of the button
    @Override
    void changeButton(View button) {
        button.setOnClickListener(view -> activity.startFocus());
    }

    // Reset the timer based on the data from the SharedData pool
    @Override
    void changeTimer(Timer timer) {
        int snoozeTimeSecs = SharedData.getInstance().getDefaultSnoozeTimeSecs();
        timer.setTotalTimeInSec(snoozeTimeSecs);
        // when the timer finishes, run startFocus()
        timer.setOnFinish(() -> activity.startFocus());
    }

    // Change the params of the circularBar
    @Override
    void changeSeekbar(CircularSeekBar seekBar) {
        seekBar.setDisablePointer(false);
        model.getTotalTimeSecs().setValue(90 * 60);
        model.getLeftTimeSecs().setValue(SharedData.getInstance().getDefaultFocusTimeSecs());
    }
}

/** Abstract class HomepageState */
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
