package com.iti.android.tripapp.helpers;

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
        DatabaseReference myRef = database.getReference(trip.getUserId() + "trips");
        myRef.child(Integer.toString(trip.getId())).setValue(trip);
    }
    public void updateTripOnFirebase(TripDTO trip) {
        DatabaseReference myRef = database.getReference(trip.getUserId() + "trips");
        myRef.child(Integer.toString(trip.getId())).setValue(trip);
    }
    public void removeTripFromFirebase(TripDTO trip) {
        DatabaseReference myRef = database.getReference(trip.getUserId() + "trips");
        myRef.child(Integer.toString(trip.getId())).setValue(null);
    }


    public ArrayList<TripDTO> retrieveUserTripsFromFirebase(UserDTO user) {
         final ArrayList<TripDTO> trips;
        trips = new ArrayList<TripDTO>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(user.getId() + "trips");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            TripDTO trip = userSnapshot.getValue(TripDTO.class);
                            trips.add(trip);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        return trips;
    }

//
//    public void createAndUpdateNotesOnFirebase(Note note) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference(note.get() + "trip" + note.getTripId() + "notes");
//        myRef.child(Integer.toString(note.getNoteId())).setValue(note);
//    }
//
//    public void removeNotesFromFirebase(Note note) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference(note.getUserId() + "trip" + note.getTripId() + "notes");
//        myRef.child(Integer.toString(note.getNoteId())).setValue(null);
//    }
//



}
