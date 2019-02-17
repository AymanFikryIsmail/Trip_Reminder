package com.iti.android.tripapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ayman on 2019-02-08.
 */

public class TripDTO implements Serializable {
    private int id;
    private String name;
    private String trip_start_point;
    private String trip_end_point;
    private Double trip_start_point_longitude;
    private Double trip_start_point_latitude;
    private Double trip_end_point_longitude;
    private Double trip_end_point_latitude;
    private String trip_date;
    private String trip_time;
    private String roundStatus;
    private int trip_rounded;
    private String repeated;
    private ArrayList<String> notes;
    private String userId;
    private String duration;
    private String averageSpeed;

    public TripDTO(String name, String trip_start_point, String trip_end_point, Double trip_start_point_longitude,
                   Double trip_start_point_latitude, Double trip_end_point_longitude, Double trip_end_point_latitude,
                   String trip_date, String trip_time , String repeated) {
        this.name = name;
        this.trip_start_point = trip_start_point;
        this.trip_end_point = trip_end_point;
        this.trip_start_point_longitude = trip_start_point_longitude;
        this.trip_start_point_latitude = trip_start_point_latitude;
        this.trip_end_point_longitude = trip_end_point_longitude;
        this.trip_end_point_latitude = trip_end_point_latitude;
        this.trip_date = trip_date;
        this.trip_time = trip_time;
        this.repeated=repeated;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTrip_start_point() {
        return trip_start_point;
    }

    public String getTrip_end_point() {
        return trip_end_point;
    }

    public Double getTrip_start_point_longitude() {
        return trip_start_point_longitude;
    }

    public Double getTrip_start_point_latitude() {
        return trip_start_point_latitude;
    }

    public Double getTrip_end_point_longitude() {
        return trip_end_point_longitude;
    }

    public Double getTrip_end_point_latitude() {
        return trip_end_point_latitude;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public String getTrip_time() {
        return trip_time;
    }

    public String getRoundStatus() {
        return roundStatus;
    }

    public int getTrip_rounded() {
        return trip_rounded;
    }

    public String getRepeated() {
        return repeated;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public String getUserId() {
        return userId;
    }

    public String getDuration() {
        return duration;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }
}
