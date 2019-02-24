package com.iti.android.tripapp.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class NotesConverter {

    private Gson gson = new Gson();


    @TypeConverter
    public Notes storedStringToNotes(String value) {
        //ArrayList<String> notes = new ArrayList<>(Arrays.asList(value.split("\\s*,\\s*")));

        return gson.fromJson(value, Notes.class);
    }

    @TypeConverter
    public String languagesToStoredString(Notes notes) {
        String value = "";
        if (notes != null) {
            value = gson.toJson(notes);
//            for (String note : notes.getNotes())
//                value = value.concat(note + ",");
        }
        return value;
    }
}