package com.example.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    final static String sharedPreferencesFile = "com.example.myapplication";
    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(sharedPreferencesFile, MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        getLifecycle().addObserver(new LifecycleLogger("Lifecycle", getClass().getName()));

        // Focus Mode
        Intent focusMode = new Intent(this, FocusActivity.class);
        findViewById(R.id.enter_focus).setOnClickListener(view -> startActivity(focusMode));

        findViewById(R.id.whitelist_chooser_button).setOnClickListener(
                view -> startActivity(new Intent(this, WhiteListChooserActivity.class)));


    }
}

class LifecycleLogger implements DefaultLifecycleObserver {
    private final String className;
    private final String tag;

    public LifecycleLogger(String _tag, String _className) {
        tag = _tag;
        className = _className;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.i(tag, className + " onCreate");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.i(tag, className + " onStart");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.i(tag, className + " onResume");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.i(tag, className + " onPause");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.i(tag, className + " onStop");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.i(tag, className + " onDestroy");
    }
}
