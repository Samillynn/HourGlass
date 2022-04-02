package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WhiteListChooserActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_list_chooser);
        sharedPreferences = getSharedPreferences(MainActivity.sharedPreferencesFile, MODE_PRIVATE);
        preferenceEditor = sharedPreferences.edit();

        Set<String> whitelistPackageNames = sharedPreferences.getStringSet("whitelist", new HashSet<>());
        AppInfoAdapter appInfoAdapter = new AppInfoAdapter(getInstalledPackageNames(), whitelistPackageNames);
        ListView listView = findViewById(R.id.whitelist_chooser);
        listView.setAdapter(appInfoAdapter);

        Button saveActionButton = findViewById(R.id.save_action);
        saveActionButton.setOnClickListener(view -> {
            preferenceEditor.putStringSet("whitelist", appInfoAdapter.whitelistedPackageNames);
            preferenceEditor.apply();
            finish();
        });

        ((Button)findViewById(R.id.cancel_action)).setOnClickListener(view -> finish());
    }

    private List<String> getInstalledPackageNames() {
        List<String> result = new ArrayList<>();
        PackageManager pm = getPackageManager();
        for(ApplicationInfo applicationInfo: pm.getInstalledApplications(PackageManager.GET_META_DATA)) {
            if (pm.getLaunchIntentForPackage(applicationInfo.packageName) != null) result.add(applicationInfo.packageName);
        }
        return result;
    }

    class AppInfoAdapter extends BaseAdapter {

        private final Set<String> whitelistedPackageNames;
        private final ArrayList<String> installedPackageNames;
        private final PackageManager pm;

        public AppInfoAdapter(List<String> installPackageNames, Set<String> whitelistedPackageNames) {
            this.installedPackageNames = new ArrayList<>(installPackageNames);
            this.whitelistedPackageNames = new HashSet<>(whitelistedPackageNames);
            pm = getPackageManager();
        }

        @Override
        public int getCount() {
            return installedPackageNames.size();
        }

        @Override
        public String getItem(int i) {
            return installedPackageNames.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) view = getLayoutInflater().inflate(R.layout.app_info_layout, viewGroup, false);

            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = pm.getApplicationInfo(getItem(i), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return view;
            }

            // Application Icon
            Drawable appIcon = pm.getApplicationIcon(applicationInfo);
            ImageView imageView = view.findViewById(R.id.app_icon);
            imageView.setBackground(appIcon);

            // Application Name
            TextView textView = view.findViewById(R.id.app_label);
            textView.setText(pm.getApplicationLabel(applicationInfo));

            // Checkbox
            CheckBox checkBox = view.findViewById(R.id.app_checkbox);
            checkBox.setChecked(whitelistedPackageNames.contains(getItem(i)));
            checkBox.setOnClickListener(view1 -> this.clickCheckBox(checkBox, i));

            Log.i("Whitelist", "get view called " + applicationInfo.packageName);
            return view;
        }


        private void clickCheckBox(CheckBox checkBox, int i) {
            Log.i("Whitelist", getItem(i) + " clicked");
            if(checkBox.isChecked()) {
                whitelistedPackageNames.add(getItem(i));
            } else {
                whitelistedPackageNames.remove(getItem(i));
            }
        }
    }
}
