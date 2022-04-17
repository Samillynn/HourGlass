package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        // Constructor
        super.onCreate(saveInstanceState);
        // Inflater
        addPreferencesFromResource(R.xml.settings);

        /** After users click the default time preference */
        /** Bind the number picker with the corresponding data entry
        And update the latest value to the SharedData pool */
        NumberPickerPreference relaxTimePref = (NumberPickerPreference) findPreference("relax_time");
        relaxTimePref.setGetter(SharedData.getInstance()::getDefaultRestTimeSecs);
        relaxTimePref.setSetter(SharedData.getInstance()::setDefaultRestTimeSecs);

        NumberPickerPreference focusTimePref = (NumberPickerPreference) findPreference("focus_time");
        focusTimePref.setGetter(SharedData.getInstance()::getDefaultFocusTimeSecs);
        focusTimePref.setSetter(SharedData.getInstance()::setDefaultFocusTimeSecs);

        NumberPickerPreference snoozeTimePref = (NumberPickerPreference) findPreference("snooze_time");
        snoozeTimePref.setGetter(SharedData.getInstance()::getDefaultSnoozeTimeSecs);
        snoozeTimePref.setSetter(SharedData.getInstance()::setDefaultSnoozeTimeSecs);
        snoozeTimePref.setMaxValue(15);

        /** After users click the whitelist preference */
        Preference whitelistPref = findPreference("access_management");
        // Listen to the whitelist preference. If it is clicked, the page jumps to the WhiteListChooserActivity.
        whitelistPref.setOnPreferenceClickListener(preference -> {
            Log.i("Preference", "whitelist clicked");
            startActivity(new Intent(getContext(), WhiteListChooserActivity.class));
            return true;
        });

        /** After users click and confirm to modify the motivational message preference */
        Preference motivationalMessagePref = findPreference("motivational_msg");
        motivationalMessagePref.setOnPreferenceChangeListener((p, v) -> {
            // Update the latest value to the SharedData pool
            SharedData.getInstance().setMotivationMessage((String) v);
            return true;
        });
    }


}
