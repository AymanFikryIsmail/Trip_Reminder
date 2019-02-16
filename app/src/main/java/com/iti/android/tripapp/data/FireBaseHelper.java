package com.iti.android.tripapp.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.model.UserDTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ayman on 2019-02-15.
 */

public class FireBaseHelper implements Serializable {


    public void addUserToFirebase(UserDTO user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(user.getId()).setValue(user);
    }

    public void createTripOnFirebase(TripDTO trip) {
    }

    public void removeTripFromFirebase(TripDTO trip) {

    }


    public void retrieveUserTripsFromFirebase(UserDTO user) {

    }





}
