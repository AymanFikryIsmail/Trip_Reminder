package com.iti.android.tripapp.helpers.local.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.iti.android.tripapp.helpers.local.dao.TripDao;
import com.iti.android.tripapp.model.NoteDTO;
import com.iti.android.tripapp.model.TripDTO;

/**
 * Created by ayman on 2019-02-18.
 */

@Database(entities = {TripDTO.class}, version = 1, exportSchema = false)
public abstract class MyAppDB extends RoomDatabase {

    private static MyAppDB INSTANCE;

    public abstract TripDao tripDao();

    public static MyAppDB getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MyAppDB.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
