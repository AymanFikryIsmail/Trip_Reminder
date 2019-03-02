package com.iti.android.tripapp.ui.alarm_mvp;

import com.iti.android.tripapp.model.TripDTO;

/**
 * Created by ayman on 2019-02-24.
 */

public interface AlarmPresenter {

    void startTrip(TripDTO tripDTO, int tripId);
     void snoozeTrip( TripDTO tripDTO, int tripId);
     void cancelTrip( TripDTO tripDTO, int tripId);

}
