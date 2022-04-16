package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedData {
    final static String SHARED_PREFERENCE = "com.example.myapplication";
    final static String MOTIVATION_MESSAGE = "motivation_message";
    final static String FOCUS_TIME_DEFAULT = "focus_time_default";
    final static String REST_TIME_DEFAULT = "rest_time_default";
    final static String SNOOZE_TIME_DEFAULT= "snooze_time_default";
    final static String WHITELIST_APPS = "whitelist_apps";
    final static String REAL_FOCUS_TIME = "real_focus_time";

    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static SharedData singletonInstance;

    private SharedData() {}

    public void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    static public SharedData getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new SharedData();
        }
        return singletonInstance;
    }

    public String getMotivationMessage() {
        return sharedPreferences.getString(MOTIVATION_MESSAGE, "Self-discipline is the way to happiness");
    }

    public void setMotivationMessage(String motivationMessage) {
        editor.putString(MOTIVATION_MESSAGE, motivationMessage).apply();
    }

    public int getDefaultFocusTimeSecs() {
        return sharedPreferences.getInt(FOCUS_TIME_DEFAULT, 25 * 60);
    }

    public void setDefaultFocusTimeSecs(int defaultFocusTimeSecs) {
        editor.putInt(FOCUS_TIME_DEFAULT, defaultFocusTimeSecs).apply();
    }

    public int getDefaultRestTimeSecs() {
        return sharedPreferences.getInt(REST_TIME_DEFAULT, 25 * 60);
    }

    public void setDefaultRestTimeSecs(int defaultRestTimeSecs) {
        editor.putInt(REST_TIME_DEFAULT, defaultRestTimeSecs).apply();
    }

    public int getDefaultSnoozeTimeSecs() {
        return sharedPreferences.getInt(SNOOZE_TIME_DEFAULT, 2 * 60);
    }

    public void setDefaultSnoozeTimeSecs(int defaultSnoozeTimeSecs) {
        editor.putInt(SNOOZE_TIME_DEFAULT, defaultSnoozeTimeSecs).apply();
    }


    public Set<String> getWhitelistPackages() {
        return sharedPreferences.getStringSet(WHITELIST_APPS, new HashSet<>());
    }

    public void setWhitelistPackages(Set<String> whitelistPackages) {
        editor.putStringSet(WHITELIST_APPS, new HashSet<>(whitelistPackages)).apply();
    }

    public int getFocusTimeSecs() {
        return sharedPreferences.getInt(REAL_FOCUS_TIME, getDefaultFocusTimeSecs());
    }

    public void setFocusTimeSecs(int focusTimeSecs) {
        editor.putInt(REAL_FOCUS_TIME, focusTimeSecs).apply();
    }
}
