package com.example.myapplication.main_screen;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.TimeViewModel;
import com.example.myapplication.ui_components.CircularSeekBar;
import com.example.myapplication.utils.Timer;

/**
 * State1: WaitRestState
 */
class RestState extends MainActivity.HomepageState {

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
