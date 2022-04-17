package com.example.myapplication.utils;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.function.IntConsumer;

public class Timer {
    private int leftTimeInSec = -1;
    private int totalTimeInSec;
    private IntConsumer onTick;
    private Runnable onFinish;
    private CountDownTimer countDownTimer;

    /** Initialise an countdown timer,
     Set the behaviors when each second passes and timer finishes,
     Start the countdown timer */
    public void start() {
        countDownTimer = new CountDownTimer(totalTimeInSec * 1000L, 500) {

            @Override
            public void onTick(long l) {
                Log.d("Timer", "onTick " + onTick + " " + l/1000);
                if (l / 1000 != leftTimeInSec) {
                    leftTimeInSec = (int)l / 1000;
                    if(onTick != null) onTick.accept(leftTimeInSec);
                }
            }

            @Override
            public void onFinish() {
                if(onFinish != null) onFinish.run();
            }
        }.start();
    }

    public void setTotalTimeInSec(int totalTimeInSec1) {
        totalTimeInSec = totalTimeInSec1;
    }

    // Cancel the timer
    public void cancel() {
        if (countDownTimer != null) countDownTimer.cancel();
    }

    // Cancel the timer and reset everything
    public void clear() {
        cancel();
        onTick = null;
        onFinish = null;
        leftTimeInSec = 0;
        totalTimeInSec = 0;
    }

    /** Each second passes, run the function taken */
    // IntConsumer callback: it takes an integer argument and doesn't return anything
    public void setOnTick(IntConsumer callback) {
        onTick = callback;
    }

    /** Each the timer finishes, run the function taken in */
    // Runnable callback: it takes no arguments and doesn't return anything
    public void setOnFinish(Runnable callback) {
        onFinish = callback;
    }

}
