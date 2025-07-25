
package com.example.usageapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UsageStatsAdapter extends RecyclerView.Adapter<UsageStatsAdapter.UsageStatsViewHolder> {

    private List<AppUsageInfo> usageStatsList;

    public UsageStatsAdapter() {
        this.usageStatsList = new ArrayList<>();
    }

    public void setUsageStatsList(List<AppUsageInfo> usageStatsList) {
        this.usageStatsList = usageStatsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsageStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_usage, parent, false);
        return new UsageStatsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UsageStatsViewHolder holder, int position) {
        AppUsageInfo appUsageInfo = usageStatsList.get(position);
        holder.appNameTextView.setText(appUsageInfo.appName);
        holder.appIconImageView.setImageDrawable(appUsageInfo.appIcon);

        // وقت الاستخدام
        long totalTime = appUsageInfo.usageTime;
        long hours = TimeUnit.MILLISECONDS.toHours(totalTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime) % 60;
        String usageTimeString = String.format(Locale.getDefault(), "%dh %02dm", hours, minutes);
        holder.appUsageTimeTextView.setText("وقت الاستخدام: " + usageTimeString);

        // عدد مرات التشغيل
        int launchCount = (int) appUsageInfo.launchCount;
        holder.appLaunchCountTextView.setText("عدد مرات التشغيل: " + launchCount);

        // آخر استخدام
        String lastUsedString = "غير متوفر";
        if (appUsageInfo.lastTimeUsed > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault());
            lastUsedString = sdf.format(new Date(appUsageInfo.lastTimeUsed));
        }
        holder.appLastUsedTextView.setText("آخر استخدام: " + lastUsedString);
    }



    @Override
    public int getItemCount() {
        return usageStatsList.size();
    }

    static class UsageStatsViewHolder extends RecyclerView.ViewHolder {
        ImageView appIconImageView;
        TextView appNameTextView;
        TextView appUsageTimeTextView;
        TextView appLaunchCountTextView;
        TextView appLastUsedTextView;

        public UsageStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            appIconImageView = itemView.findViewById(R.id.appIconImageView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            appUsageTimeTextView = itemView.findViewById(R.id.appUsageTimeTextView);
            appLaunchCountTextView = itemView.findViewById(R.id.appLaunchCountTextView);
            appLastUsedTextView = itemView.findViewById(R.id.appLastUsedTextView);
        }
    }
}

