package com.iti.android.tripapp.helpers.local;

import com.iti.android.tripapp.model.TripDTO;

import java.util.ArrayList;

/**
 * Created by ayman on 2019-02-20.
 */

public interface FireBaseCallBack {

    void getTrips( ArrayList<TripDTO> trips);
}
