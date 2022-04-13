package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import java.util.HashSet;
import java.util.Set;

public class WhitelistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);
        Set<String> whitelistPackageNames = SettingData.getInstance().getWhitelistPackages();

        int imageButtonId = 0;
        for(String packageName: whitelistPackageNames) {
            addIconLauncher(packageName, imageButtonId);
            imageButtonId++;
        }
    }

    private void addIconLauncher(String packageName, int imageButtonId) {
        addEmptyImageButton(imageButtonId);
        fillImageButton(packageName, imageButtonId);
    }


    private void addEmptyImageButton(int imageButtonId) {
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FlexboxLayout linearLayout = (FlexboxLayout) findViewById(R.id.whitelist);
        ImageButton imageButton = new ImageButton(this);
        imageButton.setId(imageButtonId);
        linearLayout.addView(imageButton, params);
    }

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
        try {
            imageButton.setBackground(getPackageManager().getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }




}