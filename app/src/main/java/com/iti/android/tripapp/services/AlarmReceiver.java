package com.iti.android.tripapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iti.android.tripapp.model.TripDTO;

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
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
     //   throw new UnsupportedOperationException("Not yet implemented");
    }
}
