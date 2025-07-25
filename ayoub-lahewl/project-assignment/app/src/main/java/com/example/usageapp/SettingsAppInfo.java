
package com.example.usageapp;

import android.graphics.drawable.Drawable;

public class SettingsAppInfo {
    public String appName;
    public String packageName;
    public Drawable appIcon;
    public boolean isTracked;

    public SettingsAppInfo(String appName, String packageName, Drawable appIcon, boolean isTracked) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.isTracked = isTracked;
    }
}

