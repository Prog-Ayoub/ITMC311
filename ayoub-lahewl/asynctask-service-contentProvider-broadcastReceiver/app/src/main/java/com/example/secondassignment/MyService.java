package com.example.secondassignment;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

public class MyService extends Service {
    MediaPlayer mp;
    public MyService()
    {

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(mp == null)
        {
            mp = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            mp.setLooping(true);
            mp.start();
        }
        else if(!mp.isPlaying())
            mp.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mp.isPlaying())
            mp.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}