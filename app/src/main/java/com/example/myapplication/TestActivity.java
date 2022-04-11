package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

public class TestActivity extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        progressBar = findViewById(R.id.progress_ring);
        progressBar.setMax(60);
        progressBar.setProgress(60);

        new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long l) {
                Log.i("ProgressBar", "" + (60 - (int)l/1000));
                progressBar.incrementProgressBy(-1);
            }

            @Override
            public void onFinish() {
                Log.i("ProgressBar", "finish");
                progressBar.setProgress(0);
            }
        }.start();
    }
}