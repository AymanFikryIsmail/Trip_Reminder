package com.iti.android.tripapp.services.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.screens.AlarmActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ayman on 2019-02-15.
 */

public class AlarmHelper {
    public static void setAlarm(Context context, TripDTO trip, Calendar myCalendar) {
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.putExtra("notification", trip.getName());
        alarmIntent.putExtra("tripId", trip.getId());
        alarmIntent.putExtra("startLng", trip.getTrip_start_point_longitude());
        alarmIntent.putExtra("endLng", trip.getTrip_end_point_longitude());
        alarmIntent.putExtra("startLat", trip.getTrip_start_point_longitude());
        alarmIntent.putExtra("endLat", trip.getTrip_start_point_longitude());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.getId(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), pendingIntent);
    }

    public static void cancelAlarm(Context context, Integer userId, Integer tripId) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("trip_id", tripId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(userId.toString() + tripId.toString()),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

}