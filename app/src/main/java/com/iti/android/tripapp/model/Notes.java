package com.iti.android.tripapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Notes implements Serializable {

    public Notes() {
    }


    private ArrayList<String> notes;

    public Notes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }
}