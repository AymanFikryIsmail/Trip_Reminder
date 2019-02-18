package com.iti.android.tripapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.services.alarm.AlarmHelper;

public class BackgroundSoundService extends Service {

    MediaPlayer player;

    public BackgroundSoundService() {
    }

    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.notification);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if ("cancel".equals(action)){
            stopSelf();
            player.stop();
            player.release();
            AlarmHelper.cancelAlarm(getApplicationContext(),0);
        }
        else {
            player.start();
        }
        return START_NOT_STICKY;
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    @Override
    public void onLowMemory() {

    }
}
