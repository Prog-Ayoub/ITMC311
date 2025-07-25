package com.example.usageapp;

import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import android.content.SharedPreferences;

public class AppUsageFragment extends Fragment {

    private static final String TAG = "AppUsageFragment";
    private static final int REQUEST_USAGE_PERMISSION = 123;

    private Button requestPermissionButton;
    private RecyclerView usageStatsRecyclerView;
    private UsageStatsAdapter usageStatsAdapter;
    private BarChart usageBarChart;
    private TextView dateRangeTextView;
    private TextView totalUsageTextView; // Added TextView for total usage
    private int currentInterval = UsageStatsManager.INTERVAL_DAILY;
    private CardView permissionCard, intervalCard, chartCard, listCard, totalUsageCard; // Added totalUsageCard

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_usage, container, false);

        requestPermissionButton = view.findViewById(R.id.requestPermissionButton);
        usageStatsRecyclerView = view.findViewById(R.id.usageStatsRecyclerView);
        usageBarChart = view.findViewById(R.id.usageBarChart);
        dateRangeTextView = view.findViewById(R.id.dateRangeTextView);
        totalUsageTextView = view.findViewById(R.id.totalUsageTextView); // Initialized totalUsageTextView
        permissionCard = view.findViewById(R.id.permissionCard);
        intervalCard = view.findViewById(R.id.intervalCard);
        chartCard = view.findViewById(R.id.chartCard);
        listCard = view.findViewById(R.id.listCard);
        totalUsageCard = view.findViewById(R.id.totalUsageCard); // Initialized totalUsageCard

        usageStatsAdapter = new UsageStatsAdapter();
        usageStatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usageStatsRecyclerView.setAdapter(usageStatsAdapter);

        requestPermissionButton.setOnClickListener(v -> requestUsageStatsPermission());

        Spinner intervalSpinner = view.findViewById(R.id.intervalSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.interval_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(adapter);

        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        currentInterval = UsageStatsManager.INTERVAL_DAILY;
                        break;
                    case 1:
                        currentInterval = UsageStatsManager.INTERVAL_WEEKLY;
                        break;
                    case 2:
                        currentInterval = UsageStatsManager.INTERVAL_MONTHLY;
                        break;
                    case 3:
                        currentInterval = UsageStatsManager.INTERVAL_YEARLY;
                        break;
                }
                getUsageStats();
                updateDateRangeText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        setupBarChart();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUsageStatsPermission();
    }

    private void checkUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getContext().getPackageName());

        if (mode == AppOpsManager.MODE_ALLOWED) {
            permissionCard.setVisibility(View.GONE);
            intervalCard.setVisibility(View.VISIBLE);
            totalUsageCard.setVisibility(View.VISIBLE); // Show total usage card
            chartCard.setVisibility(View.VISIBLE);
            listCard.setVisibility(View.VISIBLE);
            getUsageStats();
        } else {
            permissionCard.setVisibility(View.VISIBLE);
            intervalCard.setVisibility(View.GONE);
            totalUsageCard.setVisibility(View.GONE); // Hide total usage card
            chartCard.setVisibility(View.GONE);
            listCard.setVisibility(View.GONE);
            Toast.makeText(getContext(), "يرجى منح إذن الوصول إلى بيانات الاستخدام", Toast.LENGTH_LONG).show();
        }
    }

    private void requestUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(intent, REQUEST_USAGE_PERMISSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_USAGE_PERMISSION) {
            checkUsageStatsPermission();
        }
    }

    public void getUsageStats() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager == null) {
            Log.e(TAG, "UsageStatsManager is null");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        if (currentInterval == UsageStatsManager.INTERVAL_WEEKLY) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
        } else if (currentInterval == UsageStatsManager.INTERVAL_MONTHLY) {
            calendar.add(Calendar.MONTH, -1);
        } else if (currentInterval == UsageStatsManager.INTERVAL_YEARLY) {
            calendar.add(Calendar.YEAR, -1);
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, -1); // default daily
        }
        long startTime = calendar.getTimeInMillis();

        UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, endTime);
        UsageEvents.Event event = new UsageEvents.Event();

        PackageManager packageManager = getContext().getPackageManager();
        SharedPreferences prefs = getContext().getSharedPreferences("tracking_prefs", Context.MODE_PRIVATE);

        Map<String, Integer> launchCounts = new java.util.HashMap<>();
        Map<String, Long> usageTimes = new java.util.HashMap<>();
        Map<String, Long> lastUsedMap = new java.util.HashMap<>();
        Map<String, Long> startTimes = new java.util.HashMap<>();

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);

            String packageName = event.getPackageName();
            if (packageName == null) continue;

            switch (event.getEventType()) {
                case UsageEvents.Event.MOVE_TO_FOREGROUND:
                    if (!startTimes.containsKey(packageName)) {
                        launchCounts.put(packageName, launchCounts.getOrDefault(packageName, 0) + 1);
                        startTimes.put(packageName, event.getTimeStamp());
                    }
                    lastUsedMap.put(packageName, event.getTimeStamp());
                    break;

                case UsageEvents.Event.MOVE_TO_BACKGROUND:
                    Long start = startTimes.get(packageName);
                    if (start != null) {
                        long duration = event.getTimeStamp() - start;
                        usageTimes.put(packageName, usageTimes.getOrDefault(packageName, 0L) + duration);
                        startTimes.remove(packageName);
                    }
                    break;
            }
        }


        for (Map.Entry<String, Long> entry : startTimes.entrySet()) {
            String packageName = entry.getKey();
            long duration = endTime - entry.getValue();
            usageTimes.put(packageName, usageTimes.getOrDefault(packageName, 0L) + duration);
        }

        List<AppUsageInfo> appUsageInfoList = new ArrayList<>();
        long totalUsageTime = 0;
        int totalLaunchCount = 0;

        for (String packageName : usageTimes.keySet()) {
            try {
                boolean isTracked = prefs.getBoolean(packageName, true);
                if (!isTracked) continue;

                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                String appName = (String) packageManager.getApplicationLabel(applicationInfo);
                long usageTime = usageTimes.get(packageName);
                int launchCount = launchCounts.getOrDefault(packageName, 0);
                long lastTimeUsed = lastUsedMap.getOrDefault(packageName, 0L);

                if (usageTime > 0) {
                    appUsageInfoList.add(new AppUsageInfo(
                            appName, packageName, usageTime, launchCount,
                            applicationInfo.loadIcon(packageManager), lastTimeUsed
                    ));
                    totalUsageTime += usageTime;
                    totalLaunchCount += launchCount;
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Package not found: " + packageName, e);
            }
        }

        Collections.sort(appUsageInfoList, new Comparator<AppUsageInfo>() {
            @Override
            public int compare(AppUsageInfo o1, AppUsageInfo o2) {
                return Long.compare(o2.usageTime, o1.usageTime);
            }
        });

        usageStatsAdapter.setUsageStatsList(appUsageInfoList);
        updateBarChart(appUsageInfoList);

        // Update total usage TextView
        String totalUsageText = TimeUnit.MILLISECONDS.toMinutes(totalUsageTime) + " دقيقة | "
                + totalLaunchCount + " مرة تشغيل";
        totalUsageTextView.setText(totalUsageText);

        // Update date range TextView (removed total usage from here)
        dateRangeTextView.setText("عرض الاستخدام لـ: " + getIntervalText());
    }

    private void setupBarChart() {
        usageBarChart.getDescription().setEnabled(false);
        usageBarChart.setDrawGridBackground(false);
        usageBarChart.setDrawBarShadow(false);
        usageBarChart.setDrawValueAboveBar(true);

        XAxis xAxis = usageBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);
        xAxis.setTextSize(10f);

        usageBarChart.getAxisLeft().setDrawGridLines(false);
        usageBarChart.getAxisRight().setEnabled(false);
        usageBarChart.getLegend().setEnabled(false);
        usageBarChart.animateY(1000);
    }

    private void updateDateRangeText() {
        dateRangeTextView.setText("عرض الاستخدام لـ: " + getIntervalText());
    }

    private String getIntervalText() {
        switch (currentInterval) {
            case UsageStatsManager.INTERVAL_DAILY: return "اليوم";
            case UsageStatsManager.INTERVAL_WEEKLY: return "الأسبوع";
            case UsageStatsManager.INTERVAL_MONTHLY: return "الشهر";
            case UsageStatsManager.INTERVAL_YEARLY: return "السنة";
            default: return "";
        }
    }

    private void updateBarChart(List<AppUsageInfo> appUsageInfoList) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < appUsageInfoList.size(); i++) {
            AppUsageInfo info = appUsageInfoList.get(i);
            entries.add(new BarEntry(i, TimeUnit.MILLISECONDS.toMinutes(info.usageTime)));
            labels.add(info.appName);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Usage Time in Minutes");
        dataSet.setColor(0xFF2196F3);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        usageBarChart.setData(barData);
        usageBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        usageBarChart.getXAxis().setAxisMinimum(-0.5f);
        usageBarChart.getXAxis().setAxisMaximum(appUsageInfoList.size() - 0.5f);
        usageBarChart.setVisibleXRangeMaximum(5f);
        usageBarChart.setDragEnabled(true);
        usageBarChart.setScaleEnabled(false);
        usageBarChart.invalidate();
    }
}


