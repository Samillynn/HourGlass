package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ActivityResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ActivityResultLauncher<Intent> openActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> Log.i("CountDown", "activity returned"));

        findViewById(R.id.open_app).setOnClickListener(view -> openActivity.launch(getPackageManager().getLaunchIntentForPackage("cn.wps.moffice_eng")));
    }
}