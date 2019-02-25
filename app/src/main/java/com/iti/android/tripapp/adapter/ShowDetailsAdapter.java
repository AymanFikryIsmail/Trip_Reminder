package com.iti.android.tripapp.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.model.NoteDTO;

import java.util.List;

public class ShowDetailsAdapter extends RecyclerView.Adapter<ShowDetailsAdapter.ShowNoteHolder> {

    private List<NoteDTO> mNotes;

    public List<NoteDTO> getNotes() {
        return mNotes;
    }

    public ShowDetailsAdapter(List<NoteDTO> tripNotes) {
        mNotes = tripNotes;
    }

    @Override
    public ShowNoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_details, parent, false);
        return new ShowNoteHolder(v);
    }

    @Override
    public void onBindViewHolder(ShowDetailsAdapter.ShowNoteHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    class ShowNoteHolder extends RecyclerView.ViewHolder {

        TextView noteText ;
        ImageView checkedNote;

        ShowNoteHolder(View v) {
            super(v);

            noteText = v.findViewById(R.id.note_content);
            checkedNote = v.findViewById(R.id.checked_note);
        }
        void bind(final int index) {
            noteText.setText(mNotes.get(index).getContent());
            if (mNotes.get(index).isChecked()){
                checkedNote.setVisibility(View.VISIBLE);
            }else {
                checkedNote.setVisibility(View.GONE);
            }
        }
    }

    }
