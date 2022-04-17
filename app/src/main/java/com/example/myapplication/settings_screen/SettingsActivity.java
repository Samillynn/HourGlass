package com.example.myapplication.settings_screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.main_screen.MainActivity;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // Constructor
        super.onCreate(savedInstanceState);
        // Inflater
        setContentView(R.layout.activity_settings);

        // Initialize the custom action bar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.settings_action_bar);

        // Set the behaviors of the "return last page" icon in the action bar
        findViewById(R.id.return_last_page).setOnClickListener(view -> {
            Intent lastPageIntent = new Intent(this, MainActivity.class);
            lastPageIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(lastPageIntent);
        });

        // Add the settings fragment to the settings page
        if (findViewById(R.id.frame_layout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.frame_layout, new SettingsFragment()).commit();
        }
    }
}
