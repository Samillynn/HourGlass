package com.example.myapplication.main_screen;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.TimeViewModel;
import com.example.myapplication.ui_components.CircularSeekBar;
import com.example.myapplication.utils.Timer;

class RestState extends MainActivity.HomepageState {


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
        button.setOnClickListener(view -> getActivity().changeState(new WaitFocusState(getActivity(), getTimeViewModel())));
    }

    @Override
    void changeTimer(Timer timer) {
        timer.setTotalTimeInSec(getTimeViewModel().getTotalTimeSecs().getValue());
        timer.setOnTick(t -> getTimeViewModel().getLeftTimeSecs().setValue(t));
        timer.setOnFinish(() -> {
            getActivity().sendBroadcast(new Intent(getActivity(), ReminderBroadcast.class));
            getActivity().changeState(new WaitFocusState(getActivity(), getTimeViewModel()));
        });
    }

    @Override
    void changeSeekbar(CircularSeekBar view) {
        view.setDisablePointer(true);
    }
}
