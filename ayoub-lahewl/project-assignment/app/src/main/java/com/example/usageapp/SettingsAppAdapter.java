package com.example.usageapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingsAppAdapter extends RecyclerView.Adapter<SettingsAppAdapter.SettingsAppViewHolder> {

    private List<SettingsAppInfo> settingsAppList;

    public SettingsAppAdapter() {
        this.settingsAppList = new ArrayList<>();
    }

    public void setSettingsAppList(List<SettingsAppInfo> settingsAppList) {
        this.settingsAppList = settingsAppList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SettingsAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_app, parent, false);
        return new SettingsAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsAppViewHolder holder, int position) {
        SettingsAppInfo appInfo = settingsAppList.get(position);
        holder.appNameTextView.setText(appInfo.appName);
        holder.appIconImageView.setImageDrawable(appInfo.appIcon);
        holder.appTrackingSwitch.setChecked(appInfo.isTracked);

        holder.appTrackingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            appInfo.isTracked = isChecked;
            SharedPreferences prefs = buttonView.getContext().getSharedPreferences("tracking_prefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean(appInfo.packageName, isChecked).apply();
        });
    }

    @Override
    public int getItemCount() {
        return settingsAppList.size();
    }

    static class SettingsAppViewHolder extends RecyclerView.ViewHolder {
        ImageView appIconImageView;
        TextView appNameTextView;
        Switch appTrackingSwitch;

        public SettingsAppViewHolder(@NonNull View itemView) {
            super(itemView);
            appIconImageView = itemView.findViewById(R.id.appIconImageView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            appTrackingSwitch = itemView.findViewById(R.id.appTrackingSwitch);
        }
    }
}
