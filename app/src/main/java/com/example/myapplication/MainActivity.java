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

import androidx.appcompat.app.AppCompatActivity;

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