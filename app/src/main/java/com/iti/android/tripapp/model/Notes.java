package com.iti.android.tripapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Notes implements Serializable {

    private ArrayList<NoteDTO> notes;

    public Notes() {}

    public Notes(ArrayList<NoteDTO> notes) {
        this.notes = notes;
    }

    public ArrayList<NoteDTO> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<NoteDTO> notes) {
        this.notes = notes;
    }
}