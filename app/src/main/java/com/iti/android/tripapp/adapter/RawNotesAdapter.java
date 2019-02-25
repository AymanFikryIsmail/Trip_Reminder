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

public class RawNotesAdapter extends RecyclerView.Adapter<RawNotesAdapter.RawNoteHolder> {

    private List<NoteDTO> mNotes;

    public List<NoteDTO> getNotes() {
        return mNotes;
    }

    public RawNotesAdapter(List<NoteDTO> tripNotes) {
        mNotes = tripNotes;
    }

    @Override
    public RawNoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_note, parent, false);
        return new RawNoteHolder(v);
    }

    @Override
    public void onBindViewHolder(RawNoteHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    class RawNoteHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView button;

        RawNoteHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.note_content);
            button = v.findViewById(R.id.btn_delete);
        }

        void bind(final int index) {
            textView.setText(mNotes.get(index).getContent());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNotes.remove(index);
                    notifyDataSetChanged();
                }
            });
        }
    }

}