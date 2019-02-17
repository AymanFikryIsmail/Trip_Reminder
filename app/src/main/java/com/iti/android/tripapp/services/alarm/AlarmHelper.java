package com.iti.android.tripapp.services.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.screens.AlarmActivity;
import com.iti.android.tripapp.services.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ayman on 2019-02-15.
 */

public class AlarmHelper {
    public static void setAlarm(Context context, TripDTO trip, Calendar myCalendar) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        alarmIntent.putExtra("trip",trip);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.getId(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(trip.getRepeated().equals("Daily")){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }else if(trip.getRepeated().equals("Weekly")){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7, pendingIntent);
        }else if(trip.getRepeated().equals("Monthly")){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*30, pendingIntent);
        }
        else {
        alarmManager.set(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void cancelAlarm(Context context, int tripId) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("user_id", 0);
        intent.putExtra("trip_id", tripId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tripId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

}