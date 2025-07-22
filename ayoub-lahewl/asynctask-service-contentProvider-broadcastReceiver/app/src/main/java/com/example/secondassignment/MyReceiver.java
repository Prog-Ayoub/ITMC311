package com.example.secondassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {
        int level = intent.getIntExtra("Level ",0);
        Toast.makeText(context, "Battery Level: " + level + "%", Toast.LENGTH_LONG).show();
    }
}
