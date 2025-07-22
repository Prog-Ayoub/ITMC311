package com.example.secondassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProdcastReceiverActivity extends AppCompatActivity {

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context c, Intent i) {
            int level = i.getIntExtra("level", 0);

            ProgressBar pb = (ProgressBar) findViewById(R.id.batteryBar);
            TextView tv = (TextView) findViewById(R.id.batteryLevel);

            pb.setProgress(level);
            tv.setText("Battery Level: " + Integer.toString(level) + "%");
            if (level < 20) {
                tv.setTextColor(Color.RED);
            } else if (level < 50) {
                tv.setTextColor(Color.parseColor("#FFA500")); // برتقالي
            } else {
                tv.setTextColor(Color.GREEN);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prodcast_receiver);

        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}