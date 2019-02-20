package com.iti.android.tripapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ayman on 2019-02-08.
 */

@Entity(tableName = "trip")
public class TripDTO implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "trip_start_point")
    private String trip_start_point;
    @ColumnInfo(name = "trip_end_point")
    private String trip_end_point;
    @ColumnInfo(name = "trip_start_point_longitude")
    private Double trip_start_point_longitude;
    @ColumnInfo(name = "trip_start_point_latitude")
    private Double trip_start_point_latitude;
    @ColumnInfo(name = "trip_end_point_longitude")
    private Double trip_end_point_longitude;
    @ColumnInfo(name = "trip_end_point_latitude")
    private Double trip_end_point_latitude;
    @ColumnInfo(name = "trip_date")
    private String trip_date;
    @ColumnInfo(name = "trip_time")
    private String trip_time;
    @ColumnInfo(name = "roundStatus")
    private String roundStatus;
    @ColumnInfo(name = "trip_rounded")
    private int trip_rounded;
    @ColumnInfo(name = "repeated")
    private String repeated;
    @ColumnInfo(name = "tripStatus")
    private String tripStatus;

    @ColumnInfo(name = "userId")
    private String userId;
    @ColumnInfo(name = "duration")
    private String duration;
    @ColumnInfo(name = "averageSpeed")
    private String averageSpeed;

    public String getTripStatus() {
        return tripStatus;
    }

    public TripDTO() {
    }

    public TripDTO(String userId, String name, String trip_start_point, String trip_end_point,
                   Double trip_start_point_longitude, Double trip_start_point_latitude, Double trip_end_point_longitude, Double trip_end_point_latitude,
                   String trip_date, String trip_time , String repeated , String tripStatus ) {
        this.userId = userId;
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
        this.tripStatus=tripStatus;

    }

    public void setId(int id) {
        this.id = id;
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

    public String getUserId() {
        return userId;
    }

    public String getDuration() {
        return duration;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setTrip_rounded(int trip_rounded) {
        this.trip_rounded = trip_rounded;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setRoundStatus(String roundStatus) {
        this.roundStatus = roundStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrip_start_point(String trip_start_point) {
        this.trip_start_point = trip_start_point;
    }

    public void setTrip_end_point(String trip_end_point) {
        this.trip_end_point = trip_end_point;
    }

    public void setTrip_start_point_longitude(Double trip_start_point_longitude) {
        this.trip_start_point_longitude = trip_start_point_longitude;
    }

    public void setTrip_start_point_latitude(Double trip_start_point_latitude) {
        this.trip_start_point_latitude = trip_start_point_latitude;
    }

    public void setTrip_end_point_longitude(Double trip_end_point_longitude) {
        this.trip_end_point_longitude = trip_end_point_longitude;
    }

    public void setTrip_end_point_latitude(Double trip_end_point_latitude) {
        this.trip_end_point_latitude = trip_end_point_latitude;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public void setTrip_time(String trip_time) {
        this.trip_time = trip_time;
    }

    public void setRepeated(String repeated) {
        this.repeated = repeated;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
