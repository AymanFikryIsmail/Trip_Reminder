package com.iti.android.tripapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.model.NoteDTO;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private static List<NoteDTO> mNotes;
    private int i = 0;

    public List<NoteDTO> getNotes() {
        return mNotes;
    }

    static class NoteHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox checkBox;
        final int index;

        NoteHolder(View v, int i) {
            super(v);
            index = i;
            checkBox = v.findViewById(R.id.cb_note);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mNotes.get(index).setChecked(isChecked);
                }
            });
            textView = v.findViewById(R.id.tv_note);
        }
    }

    public NotesAdapter(List<NoteDTO> tripNotes) {
        mNotes = tripNotes;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_note, parent, false);
        return new NoteHolder(v, i++);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        holder.textView.setText(mNotes.get(position).getContent());
        holder.checkBox.setChecked(mNotes.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }
}