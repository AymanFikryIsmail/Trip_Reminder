package com.iti.android.tripapp.model;

public class NoteDTO {

    private boolean isChecked;
    private String content;

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
