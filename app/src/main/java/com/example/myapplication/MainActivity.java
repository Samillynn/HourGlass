package com.example.myapplication;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLifecycle().addObserver(new LifecycleLogger("Lifecycle", getClass().getName()));

        // print all packages
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if(am != null) {
            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            if (tasks.size() > 0) {
                tasks.get(0).setExcludeFromRecents(true);
            }
        }
        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.i("CountDown", "Installed package :" + packageInfo.packageName);
        }

//        startActivity(getPackageManager().getLaunchIntentForPackage("com.google.android.videos"));

        // Focus Mode
        Intent focusMode = new Intent(this, FocusActivity.class);
        findViewById(R.id.enter_focus).setOnClickListener(view -> {
            startActivity(focusMode);
        });

        // Activity Result Exp
        findViewById(R.id.activity_result).setOnClickListener(
                view -> startActivity(new Intent(this, ActivityResult.class)));
    }

}