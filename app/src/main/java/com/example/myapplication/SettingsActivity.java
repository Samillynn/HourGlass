package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    Button whitelist_button;
    Button customiseMes_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        whitelist_button = (Button) findViewById(R.id.button1);
        whitelist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhitelist();
            }
        });

        customiseMes_button = (Button) findViewById(R.id.button2);
        customiseMes_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMessageAct();
                }
        });
    }
    public void openWhitelist(){
        Intent intent_whitelist = new Intent(this, WhiteListChooserActivity.class);
        startActivity(intent_whitelist);
    }
    public void openMessageAct(){
        Intent intent_mes = new Intent(this, CustomiseMes.class);
        startActivity(intent_mes);
    }
}