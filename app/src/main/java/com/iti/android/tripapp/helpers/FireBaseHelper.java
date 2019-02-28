package com.iti.android.tripapp.helpers;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.android.tripapp.helpers.local.FireBaseCallBack;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.model.UserDTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ayman on 2019-02-15.
 */

public class FireBaseHelper implements Serializable {

    FirebaseDatabase database;
    public FireBaseHelper() {
         database = FirebaseDatabase.getInstance();
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    public void addUserToFirebase(UserDTO user) {
        DatabaseReference myRef = database.getReference("users");
        myRef.child(user.getId()).setValue(user);
    }

    public void createTripOnFirebase(TripDTO trip) {
        DatabaseReference myRef = database.getReference(trip.getUserId());
        myRef.child(Integer.toString(trip.getId())).setValue(trip);
    }
    public void updateTripOnFirebase(TripDTO trip) {
        DatabaseReference myRef = database.getReference(trip.getUserId() );
        myRef.child(Integer.toString(trip.getId())).setValue(trip);
    }
    public void removeTripFromFirebase(TripDTO trip) {
        DatabaseReference myRef = database.getReference(trip.getUserId());
        myRef.child(Integer.toString(trip.getId())).setValue(null);
    }


    public void retrieveUserTripsFromFirebase(String userId , final FireBaseCallBack fireBaseCallBack) {
         final ArrayList<TripDTO> trips;
        trips = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(userId );
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            //userSnapshot.child()
                            TripDTO trip = userSnapshot.getValue(TripDTO.class);
                            trips.add(trip);
                        }
                        fireBaseCallBack.getTrips(trips);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }
}
