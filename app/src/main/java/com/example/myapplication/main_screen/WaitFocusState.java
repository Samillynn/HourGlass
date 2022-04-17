package com.example.myapplication.main_screen;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.SharedData;
import com.example.myapplication.models.TimeViewModel;
import com.example.myapplication.ui_components.CircularSeekBar;
import com.example.myapplication.utils.Timer;

/**
 * State1: WaitFocusState
 */
class WaitFocusState extends MainActivity.HomepageState {
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
