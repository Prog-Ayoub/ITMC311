package com.example.usageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private RecyclerView settingsRecyclerView;
    private SettingsAppAdapter settingsAppAdapter;

    private final List<String> SYSTEM_PACKAGES = Arrays.asList(
            "com.sec.android.app.launcher",
            "com.android.systemui",
            "android",
            "com.google.android.googlequicksearchbox",
            "com.sec.android.app.sbrowser",
            "com.samsung.android.app.contacts",
            "com.android.phone"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        settingsRecyclerView = findViewById(R.id.settingsRecyclerView);
        settingsAppAdapter = new SettingsAppAdapter();
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        settingsRecyclerView.setAdapter(settingsAppAdapter);

        loadInstalledApps();
    }

    private void loadInstalledApps() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<SettingsAppInfo> settingsAppInfoList = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("tracking_prefs", MODE_PRIVATE);

        for (ApplicationInfo appInfo : installedApplications) {
            String packageName = appInfo.packageName;

            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    !SYSTEM_PACKAGES.contains(packageName)) {

                String appName = appInfo.loadLabel(packageManager).toString();
                boolean isTracked = prefs.getBoolean(packageName, true);

                settingsAppInfoList.add(new SettingsAppInfo(
                        appName,
                        packageName,
                        appInfo.loadIcon(packageManager),
                        isTracked
                ));
            }
        }

        settingsAppAdapter.setSettingsAppList(settingsAppInfoList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
