package com.iti.android.tripapp.model.map_model;

import java.util.List;

/**
 * Created by ayman on 2019-02-25.
 */

public class MapLeg {

    List<MapStep> legs;

    Polyline overview_polyline;

    public MapLeg(List<MapStep> legs, Polyline overview_polyline) {
        this.legs = legs;
        this.overview_polyline = overview_polyline;
    }

    public class Polyline {
        String points;

        public Polyline(String points) {
            this.points = points;
        }

        public String getPoints() {
            return points;
        }
    }
        public List<MapStep> getLegs() {
        return legs;
    }

    public Polyline getOverview_polyline() {
        return overview_polyline;
    }
}
