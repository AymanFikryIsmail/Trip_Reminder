package com.iti.android.tripapp.model.map_model;

import java.io.Serializable;

/**
 * Created by ayman on 2019-02-25.
 */

public class MapPoint implements Serializable {


    public MapPoint(MapPoint.distance distance, MapPoint.duration duration, String travel_mode) {
        this.distance = distance;
        this.duration = duration;
        this.travel_mode = travel_mode;
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
    private distance distance;
    private duration duration;

    private String travel_mode;
    public MapPoint.distance getDistance() {
        return distance;
    }

    public void setDistance(MapPoint.distance distance) {
        this.distance = distance;
    }

    public MapPoint.duration getDuration() {
        return duration;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setDuration(MapPoint.duration duration) {
        this.duration = duration;
    }
}
