package com.iti.android.tripapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.ui.alarm_mvp.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

    double startLat;
    double startLng;
    double endLat;
    double endLng;
    String notification;
    int notificationID;
    TripDTO tripDTO;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.putExtra("tripId", intent.getIntExtra("tripId", 0));
        i.putExtra("tripid", intent.getIntExtra("tripid",0));
        context.startActivity(i);

    }
}
