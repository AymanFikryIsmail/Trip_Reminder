package com.iti.android.tripapp.model.map_model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ayman on 2019-02-25.
 */

public class MapStep implements Serializable {
    private List<MapPoint> steps;

    String copyrights;

    public String getCopyrights() {
        return copyrights;
    }

    public MapStep(List<MapPoint> steps) {
        this.steps = steps;
    }

    public MapStep(List<MapPoint> steps, String copyrights) {
        this.steps = steps;
        this.copyrights = copyrights;
    }

    public List<MapPoint> getSteps() {
        return steps;
    }

    public void setSteps(List<MapPoint> steps) {
        this.steps = steps;
    }
}
