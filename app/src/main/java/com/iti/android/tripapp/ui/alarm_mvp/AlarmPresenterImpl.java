package com.iti.android.tripapp.ui.alarm_mvp;

import android.app.NotificationManager;
import android.content.Context;

import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.helpers.NotificationHelper;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.services.alarm.AlarmHelper;

/**
 * Created by ayman on 2019-02-24.
 */

public class AlarmPresenterImpl implements AlarmPresenter
{
    private AlarmView view;
    private Context context;
    NotificationHelper notificationHelper;
    private NotificationManager notificationManager;
    private FireBaseHelper fireBaseHelper;
    public AlarmPresenterImpl(AlarmView view)
    {
        this.view = view;
        this.context= (Context) view;
        fireBaseHelper=new FireBaseHelper();
        notificationHelper=new NotificationHelper(context);
        notificationManager=notificationHelper.getManager();
    }
    @Override
    public void startTrip(final TripDTO tripDTO, int tripId)
    {
        tripDTO.setTripStatus("started");
        tripDTO.setId(tripId);
        fireBaseHelper.updateTripOnFirebase(tripDTO);
        // update in fire base
        MyAppDB.getAppDatabase(context).tripDao().updateTrip(tripDTO);
        AlarmHelper.cancelAlarm(context.getApplicationContext(),tripId);
        notificationManager.cancel(tripId);
        view.showDirection();
        view.startFloatingWidgetService();
    }

    @Override
    public void snoozeTrip(TripDTO tripDTO, int tripId) {
        AlarmHelper.cancelAlarm(context.getApplicationContext(),tripId);
        notificationHelper.createNotification(tripDTO);
    }

    @Override
    public void cancelTrip(TripDTO tripDTO, int tripId) {
        MyAppDB.getAppDatabase(context).tripDao().delete(tripDTO);
        fireBaseHelper.removeTripFromFirebase(tripDTO);
        notificationManager.cancel(tripId);
        AlarmHelper.cancelAlarm(context.getApplicationContext(),tripId);
    }
}