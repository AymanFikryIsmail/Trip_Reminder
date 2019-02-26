package com.iti.android.tripapp.model.map_model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ayman on 2019-02-25.
 */

public class MapStep implements Serializable {

    private distance distance;
    private duration duration;
    private List<MapStep> steps;

    public MapStep(distance distance, duration duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public distance getDistance() {
        return distance;
    }

    public MapStep.duration getDuration() {
        return duration;
    }

    public class distance{
        String text;
        public String getText() {
            return text;
        }
    }
    public class duration{
        String text;
        public String getText() {
            return text;
        }
    }
//
//    private List<MapPoint> steps;
//
//    public MapStep(List<MapPoint> steps) {
//        this.steps = steps;
//    }
//
//    public MapStep(List<MapPoint> steps, String copyrights) {
//        this.steps = steps;
//    }
//
//    public List<MapPoint> getSteps() {
//        return steps;
//    }
//
//    public void setSteps(List<MapPoint> steps) {
//        this.steps = steps;
//    }
}
