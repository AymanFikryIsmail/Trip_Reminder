package com.iti.android.tripapp.helpers.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.iti.android.tripapp.model.Notes;
import com.iti.android.tripapp.model.TripDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayman on 2019-02-18.
 */

@Dao
public interface TripDao {
    @Insert
    long addTrip(TripDTO tripDTO);
    @Query("SELECT * FROM trip where tripStatus LIKE :waiting AND userId LIKE :userId ")//
    List<TripDTO> getAllTrips(String waiting, String userId);

    @Query("SELECT notes FROM trip where id LIKE :id")//
    Notes getNotes(int id);

    @Query("SELECT * FROM trip where id LIKE :tripid ")//
    TripDTO getTrip(int tripid);

    @Update
    int updateTrip(TripDTO trip);
    @Query("SELECT COUNT(*) from trip")
    int countUsers();

    @Insert
    void insertAll(ArrayList<TripDTO> tripDTOArrayList);

    @Delete
    void delete(TripDTO trip);

    @Query("DELETE FROM trip WHERE userId LIKE :userId")
    void deleteByUserId(String userId);
}
