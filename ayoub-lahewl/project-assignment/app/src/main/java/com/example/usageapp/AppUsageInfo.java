package com.example.usageapp;

import android.graphics.drawable.Drawable;

public class AppUsageInfo {
    public String appName;
    public String packageName;
    public long usageTime;
    public long launchCount;
    public Drawable appIcon;
    public long lastTimeUsed;

    public AppUsageInfo(String appName, String packageName, long usageTime, long launchCount, Drawable appIcon, long lastTimeUsed) {
        this.appName = appName;
        this.packageName = packageName;
        this.usageTime = usageTime;
        this.launchCount = launchCount;
        this.appIcon = appIcon;
        this.lastTimeUsed = lastTimeUsed;
    }
}

