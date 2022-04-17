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
 * State1: WaitRestState
 */
class WaitRestState extends MainActivity.HomepageState {
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
