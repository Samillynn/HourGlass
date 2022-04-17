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

    public void cancel() {
        if (countDownTimer != null) countDownTimer.cancel();
    }
    public void clear() {
        cancel();
        onTick = null;
        onFinish = null;
        leftTimeInSec = 0;
        totalTimeInSec = 0;
    }
    public void setOnTick(IntConsumer callback) {
        onTick = callback;
    }

    public void setOnFinish(Runnable callback) {
        onFinish = callback;
    }

}
