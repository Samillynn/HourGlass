package com.example.myapplication.utils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class LifecycleLogger implements DefaultLifecycleObserver {
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
