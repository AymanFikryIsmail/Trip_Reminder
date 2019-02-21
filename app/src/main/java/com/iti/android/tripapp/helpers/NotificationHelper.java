package com.iti.android.tripapp.helpers;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.screens.AlarmActivity;
import com.iti.android.tripapp.screens.MainActivity;

/**
 * Created by ayman on 2019-02-17.
 */

public class NotificationHelper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;
Context context;
    public NotificationHelper(Context base) {
context=base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }else {
            if (mManager == null) {
                mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
//        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
//        getManager().createNotificationChannel(channel);
         mManager = context.getSystemService(NotificationManager.class);
        mManager.createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
//        if (mManager == null) {
//            mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        }
        return mManager;
    }


    public void  createNotification(TripDTO tripDTO){
        // content intent
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.putExtra("tripid",tripDTO.getId());
        PendingIntent alarmPendingIntent = PendingIntent.getActivity(context,  tripDTO.getId(), alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(context, AlarmActivity.class);
        cancelIntent.putExtra("tripid", tripDTO.getId());
        cancelIntent.putExtra("cancel", "cancel");
        PendingIntent cancelPendingIntent = PendingIntent.getActivity(context, tripDTO.getId(),
                cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // start intent
        Intent startIntent = new Intent(context, AlarmActivity.class);
        startIntent.putExtra("tripid",tripDTO.getId());
        startIntent.putExtra("start", "start");
        PendingIntent startPendingIntent = PendingIntent.getActivity(context,  tripDTO.getId() ,
                startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelID)
                .setOngoing(true)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.app_logo)
                        .setTicker("Trip Alarm")
                         .setAutoCancel(false)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setContentInfo("Info")
                        .setContentTitle("New Trip on hold")
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setLights(Color.BLUE, 500, 500)
                        .setContentText("trip name" + " waiting to start ")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(alarmPendingIntent)
//                        .addAction(R.drawable.remove_icon, "Cancel", cancelPendingIntent)
//                        .addAction(R.drawable.selected_icon, "Start", startPendingIntent)
                                     ;

        mManager.notify(tripDTO.getId(), mBuilder.build());
       // context.finish();


    }

}
