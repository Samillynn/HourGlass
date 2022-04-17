package com.example.myapplication.main_screen;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.models.SharedData;
import com.example.myapplication.models.TimeViewModel;
import com.example.myapplication.focus_screen.FocusActivity;
import com.example.myapplication.settings_screen.SettingsActivity;
import com.example.myapplication.ui_components.CircularSeekBar;
import com.example.myapplication.utils.LifecycleLogger;
import com.example.myapplication.ui_components.TimeSegment;
import com.example.myapplication.utils.Timer;


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

    abstract static class HomepageState {
        private final MainActivity activity;
        private final TimeViewModel timeViewModel;

        HomepageState(MainActivity activity, TimeViewModel model) {
            this.timeViewModel = model;
            this.activity = activity;
        }

        abstract void changeInstructionTextView(TextView view);

        abstract void changeButton(View button);

        abstract void changeButtonTextView(TextView view);

        abstract void changeTimer(Timer timer);

        abstract void changeSeekbar(CircularSeekBar view);

        public MainActivity getActivity() {
            return activity;
        }

        public TimeViewModel getTimeViewModel() {
            return timeViewModel;
        }

    }
}

