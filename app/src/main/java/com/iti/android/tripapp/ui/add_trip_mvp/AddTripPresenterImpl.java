package com.iti.android.tripapp.ui.add_trip_mvp;

import android.app.NotificationManager;
import android.content.Context;

import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.helpers.NotificationHelper;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.services.alarm.AlarmHelper;

import java.util.Calendar;

/**
 * Created by ayman on 2019-02-24.
 */

public class AddTripPresenterImpl implements AddTripPresenter
{
    private AddTripView view;
    private Context context;
    NotificationHelper notificationHelper;
    private NotificationManager notificationManager;
    private FireBaseHelper fireBaseHelper;
    public AddTripPresenterImpl(AddTripView view)
    {
        this.view = view;
        this.context= (Context) view;
        fireBaseHelper=new FireBaseHelper();
        notificationHelper=new NotificationHelper(context);
        notificationManager=notificationHelper.getManager();
    }

    @Override
    public void updateFirebaseAndCreateAlarms(TripDTO tripDTO,Calendar myCalendar)  {
        int tripId= (int) MyAppDB.getAppDatabase(context).tripDao().addTrip(tripDTO);
        tripDTO.setId(tripId);
        fireBaseHelper.createTripOnFirebase(tripDTO);
        AlarmHelper.setAlarm(context,tripDTO,myCalendar);

    }

    @Override
    public void addTrip() {

    }

    @Override
    public void updateTrip(TripDTO tripDTO,Calendar myCalendar) {
        MyAppDB.getAppDatabase(context).tripDao().updateTrip(tripDTO);
        fireBaseHelper.updateTripOnFirebase(tripDTO);
        AlarmHelper.setAlarm(context,tripDTO,myCalendar);
    }
}