package com.iti.android.tripapp.ui.add_trip_mvp;

import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.model.UserDTO;

import java.util.Calendar;

/**
 * Created by ayman on 2019-02-24.
 */

public interface AddTripPresenter {

    void updateFirebaseAndCreateAlarms(TripDTO tripDTO,Calendar myCalendar) ;
    void addTrip();
    void updateTrip(TripDTO trip,Calendar myCalendar) ;
}
