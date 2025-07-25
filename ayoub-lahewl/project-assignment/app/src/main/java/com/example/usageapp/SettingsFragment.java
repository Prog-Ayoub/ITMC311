package com.example.usageapp;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends Fragment {
    private RecyclerView settingsRecyclerView;
    private SettingsAppAdapter settingsAppAdapter;

    // قائمة حزم نظام يجب استثناؤها
    private final List<String> SYSTEM_PACKAGES = Arrays.asList(
            "com.sec.android.app.launcher",
            "com.android.systemui",
            "android",
            "com.google.android.googlequicksearchbox",
            "com.sec.android.app.sbrowser",
            "com.samsung.android.app.contacts",
            "com.android.phone"
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsRecyclerView = view.findViewById(R.id.settingsRecyclerView);

        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        settingsRecyclerView.setAdapter(settingsAppAdapter);

        loadInstalledApps();
        return view;
    }

    private void loadInstalledApps() {
        PackageManager packageManager = getActivity().getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<SettingsAppInfo> settingsAppInfoList = new ArrayList<>();

        for (ApplicationInfo appInfo : installedApplications) {
            String packageName = appInfo.packageName;

            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 &&
                    !SYSTEM_PACKAGES.contains(packageName)) {

                String appName = appInfo.loadLabel(packageManager).toString();
                settingsAppInfoList.add(new SettingsAppInfo(
                        appName,
                        packageName,
                        appInfo.loadIcon(packageManager),
                        true
                ));
            }
        }

        settingsAppAdapter.setSettingsAppList(settingsAppInfoList);
    }
}