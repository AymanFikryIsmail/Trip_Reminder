package com.iti.android.tripapp.model;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class NotesConverter {
    @TypeConverter
    public Notes storedStringToNotes(String value) {
        ArrayList<String> notes = new ArrayList<>(Arrays.asList(value.split("\\s*,\\s*")));
        return new Notes(notes);
    }

    @TypeConverter
    public String languagesToStoredString(Notes notes) {
        String value = "";

        if (notes.getNotes()!=null) {
            for (String note : notes.getNotes())
                value = value.concat(note + ",");
        }
        return value;
    }
}