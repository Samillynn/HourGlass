package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        addPreferencesFromResource(R.xml.settings);

        NumberPickerPreference relaxTimePref = (NumberPickerPreference)findPreference("relax_time");
        relaxTimePref.setGetter(SharedData.getInstance()::getDefaultRestTimeSecs);
        relaxTimePref.setSetter(SharedData.getInstance()::setDefaultRestTimeSecs);

        NumberPickerPreference focusTimePref = (NumberPickerPreference) findPreference("focus_time");
        focusTimePref.setGetter(SharedData.getInstance()::getDefaultFocusTimeSecs);
        focusTimePref.setSetter(SharedData.getInstance()::setDefaultFocusTimeSecs);

        NumberPickerPreference snoozeTimePref = (NumberPickerPreference) findPreference("snooze_time");
        snoozeTimePref.setGetter(SharedData.getInstance()::getDefaultSnoozeTimeSecs);
        snoozeTimePref.setSetter(SharedData.getInstance()::setDefaultSnoozeTimeSecs);

        Preference whitelistPref = findPreference("access_management");
        whitelistPref.setOnPreferenceClickListener(preference -> {
            Log.i("Preference", "whitelist clicked");
            startActivity(new Intent(getContext(), WhiteListChooserActivity.class));
            return true;
        });

        Preference motivationalMessagePref = findPreference("motivational_msg");
        motivationalMessagePref.setOnPreferenceChangeListener((p, v) -> {
            SharedData.getInstance().setMotivationMessage((String) v);
            return true;
        });
    }


}
