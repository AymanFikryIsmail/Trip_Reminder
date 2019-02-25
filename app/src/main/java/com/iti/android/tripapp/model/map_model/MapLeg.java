package com.iti.android.tripapp.model.map_model;

import java.util.List;

/**
 * Created by ayman on 2019-02-25.
 */

public class MapLeg {

    List<MapStep> legs;

    public MapLeg(List<MapStep> legs) {
        this.legs = legs;
    }

    public List<MapStep> getLegs() {
        return legs;
    }
}
