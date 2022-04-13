package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TimeSegment extends ConstraintLayout {
    TextView minutesView;
    TextView secondsView;
    TextView colonView;
    float textSize;
    int textColor;
    Drawable timeBackground;


    public TimeSegment(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.time_segment, this);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.TimeSegment, 0, 0);
        init(a);
    }

    void init(TypedArray a) {
        initAttributes(a);
        initViews();
        customizeViews();
    }

    void initViews() {
        minutesView = findViewById(R.id.textViewMin);
        secondsView = findViewById(R.id.textViewSec);
        setMinutes(0);
        setSeconds(0);

        colonView = findViewById(R.id.textViewColon);
    }

    void initAttributes(TypedArray a) {
        textSize = a.getDimension(R.styleable.TimeSegment_android_textSize, 16);
        timeBackground = a.getDrawable(R.styleable.TimeSegment_ts_time_background);
        a.getColor(R.styleable.TimeSegment_android_textColor, getResources().getColor(R.color.black));
    }

    void customizeViews() {
        // background
        minutesView.setBackground(timeBackground);
        secondsView.setBackground(timeBackground);

        // text size
        minutesView.setTextSize(textSize);
        secondsView.setTextSize(textSize);
        colonView.setTextSize(textSize);
    }

    void setTimeInSecs(int seconds) {
        setMinutes(seconds/60);
        setSeconds(seconds%60);
    }

    void setMinutes(int minutes) {
        String minuteText = String.valueOf(minutes);
        if (minuteText.length() < 2)
            minuteText = '0' + minuteText;
        minutesView.setText(minuteText);
    }

    void setSeconds(int seconds) {
        String secondsText = String.valueOf(seconds);
        if (secondsText.length() < 2)
            secondsText = '0' + secondsText;
        secondsView.setText(secondsText);
    }
}
