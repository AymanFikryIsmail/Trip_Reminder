package com.iti.android.tripapp.model;


import java.io.Serializable;

/**
 * Created by ayman on 2019-02-14.
 */


public class NoteDTO implements Serializable {

    private boolean isChecked;
    private String content;

    public NoteDTO() {}

    public NoteDTO(boolean isChecked, String content) {
        this.isChecked = isChecked;
        this.content = content;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getContent() {
        return content;
    }
}
