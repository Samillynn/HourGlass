package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.IntConsumer;

public class Timer {
    int timeLeftSecond, timeTotalSecond;
    Set<IntConsumer> tickObservers;
    Set<Runnable> finishObservers;

    private Timer() {}
    public void setTimeInSecond(int timeInSecond) {}
    public void start() {}
    public int getTimeLeftSeconds() {return 0;}
    public void addTickObserver(String observerName, IntConsumer observer) {}
    public void addFinishObserver(String observerName, Runnable observer) {}

    class CountDownService extends Service {

        // TODO: recreate task and utilize START_STICKY
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            new CountDownTimer(timeTotalSecond * 1000L, 500) {

                @Override
                public void onTick(long l) {
                    if (l / 1000 != timeLeftSecond) {
                        timeLeftSecond = (int)l / 1000;
                        for(IntConsumer observer: tickObservers) {
                            observer.accept(timeLeftSecond);
                        }
                    }
                }

                @Override
                public void onFinish() {
                    for(Runnable observer: finishObservers) {
                        observer.run();
                    }
                }
            }.start();
            return START_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}

// timer:
// 1. update UI (MM:SS) and progress bar
// 2. send notification