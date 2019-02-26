package com.iti.android.tripapp.model.map_model;

import java.io.Serializable;

/**
 * Created by ayman on 2019-02-25.
 */

public class MapPoint implements Serializable {

    private Polyline polyline;

    public MapPoint(Polyline polyline) {
        this.polyline = polyline;
    }

    public class Polyline{
        String points;
    }

    public Polyline getPolyline() {
        return polyline;
    }
}
