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

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private ArrayList<NoteDTO> mNotes;

    public ArrayList<NoteDTO> getNotes() {
        return mNotes;
    }

    public NotesAdapter(ArrayList<NoteDTO> tripNotes) {
        mNotes = tripNotes;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_note, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    class NoteHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox checkBox;
        NoteHolder(View v) {
            super(v);
            checkBox = v.findViewById(R.id.cb_note);
            textView = v.findViewById(R.id.tv_note);
        }
        void bind(final int index){
            textView.setText(mNotes.get(index).getContent());
            checkBox.setChecked(mNotes.get(index).isChecked());
            if (mNotes.get(index).isChecked()){
                checkBox.setChecked(true);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mNotes.get(index).setChecked(isChecked);
                }
            });
        }
    }

}