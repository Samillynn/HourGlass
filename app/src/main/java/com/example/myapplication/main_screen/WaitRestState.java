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

public class WaitRestState extends MainActivity.HomepageState {

    WaitRestState(MainActivity activity, TimeViewModel model) {
        super(activity, model);
    }

    @Override
    void changeInstructionTextView(TextView view) {
        String textInstruction = getActivity().getString(R.string.set_relax);
        SpannableString spannable = new SpannableString(textInstruction);
        spannable.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.blue)), 16, 21, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(spannable);
    }

    @Override
    void changeButtonTextView(TextView view) {
        view.setText(R.string.start_rest);
    }

    @Override
    void changeButton(View button) {
        button.setOnClickListener(view -> {
            getTimeViewModel().getTotalTimeSecs().setValue(getTimeViewModel().getLeftTimeSecs().getValue());
            getActivity().changeState(new RestState(getActivity(), getTimeViewModel()));
        });
    }

    @Override
    void changeTimer(Timer timer) {
    }

    @Override
    void changeSeekbar(CircularSeekBar seekBar) {
        seekBar.setDisablePointer(false);
        getTimeViewModel().getTotalTimeSecs().setValue(90 * 60);
        getTimeViewModel().getLeftTimeSecs().setValue(SharedData.getInstance().getDefaultRestTimeSecs());
    }
}
