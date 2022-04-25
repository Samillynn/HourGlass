package com.example.myapplication.focus_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.SharedData;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Set;

/**
 * WhitelistActivity displays all the whitelisted apps,
 * with each app represented by a clickable icon
 */
public class WhitelistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);

        // get all whitelisted apps
        Set<String> whitelistPackageNames = SharedData.getInstance().getWhitelistPackages();

        // create clickable icons dynamically
        int imageButtonId = 0;
        for(String packageName: whitelistPackageNames) {
            addIconLauncher(packageName, imageButtonId);
            imageButtonId++;
        }
    }

    /**
     *  add and display a clickable icon on the activity
     */
    private void addIconLauncher(String packageName, int imageButtonId) {
        addEmptyImageButton(imageButtonId);
        fillImageButton(packageName, imageButtonId);
    }


    /**
     * create a empty image button, and add it in the activity
     */
    private void addEmptyImageButton(int imageButtonId) {
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FlexboxLayout linearLayout = findViewById(R.id.whitelist);
        ImageButton imageButton = new ImageButton(this);
        imageButton.setId(imageButtonId);
        linearLayout.addView(imageButton, params);
    }

    /**
     * make a specified image button clickable
     */
    private void fillImageButton(String packageName, int imageButtonId) {
        Intent app = getPackageManager().getLaunchIntentForPackage(packageName);
        ImageButton imageButton = findViewById(imageButtonId);
        try {
            imageButton.setBackground(getPackageManager().getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        imageButton.setOnClickListener(view -> {
            try {
                startActivity(app);
            } catch (Exception e) {
                Toast.makeText(this, "Can't Open the App", Toast.LENGTH_LONG).show();
            }
        });
    }

}