package com.iti.android.tripapp.components;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.adapter.NotesAdapter;
import com.iti.android.tripapp.model.NoteDTO;
import com.iti.android.tripapp.screens.NavigationActivity;

import java.util.ArrayList;
import java.util.List;

public class MovableIcon extends FloatingActionButton implements View.OnTouchListener {

    private final static float CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.

    private float downRawX, downRawY;
    private float dX, dY;
    private List<NoteDTO> notes = new ArrayList<>();

    public MovableIcon(Context context) {
        super(context);
        init();
    }

    public MovableIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovableIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){

        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Log.i("DOWN", "down");

            downRawX = motionEvent.getRawX();
            downRawY = motionEvent.getRawY();
            dX = view.getX() - downRawX;
            dY = view.getY() - downRawY;

            return true; // Consumed

        }
        else if (action == MotionEvent.ACTION_MOVE) {
            Log.i("MOVE", "moving");

            int viewWidth = view.getWidth();
            int viewHeight = view.getHeight();

            View viewParent = (View)view.getParent();
            int parentWidth = viewParent.getWidth();
            int parentHeight = viewParent.getHeight();

            float newX = motionEvent.getRawX() + dX;
            newX = Math.max(0, newX); // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(parentWidth - viewWidth, newX); // Don't allow the FAB past the right hand side of the parent

            float newY = motionEvent.getRawY() + dY;
            newY = Math.max(0, newY); // Don't allow the FAB past the top of the parent
            newY = Math.min(parentHeight - viewHeight, newY); // Don't allow the FAB past the bottom of the parent

            view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start();

            return true; // Consumed

        }
        else if (action == MotionEvent.ACTION_UP) {

            float upRawX = motionEvent.getRawX();
            float upRawY = motionEvent.getRawY();

            float upDX = upRawX - downRawX;
            float upDY = upRawY - downRawY;

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click

                // custom dialog
                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_notes, viewGroup, false);

                final RecyclerView rv = dialogView.findViewById(R.id.rv_notes);
                //rv.setHasFixedSize(true);
                /*TODO Receive actual data from file or db*/
                if (notes.isEmpty())
                    for (int i = 0; i < 10; i++) {
                        Log.i("MYTEST", "loop" + i);
                        notes.add(new NoteDTO(false, "Note" + (i + 1)));
                    }
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(new NotesAdapter(notes));

                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button dialogButton = (Button) alertDialog.findViewById(R.id.btn_apply);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notes = ((NotesAdapter)rv.getAdapter()).getNotes();
                        alertDialog.dismiss();
                    }
                });

                return performClick();
            }
            else { // A drag
                int parentWidth = ((View)view.getParent()).getWidth();
                if (upRawX <= parentWidth / 2) {
                    view.animate()
                            .x(0)
                            .setDuration(250)
                            .start();
                } else {
                    view.animate()
                            .x(parentWidth - view.getWidth())
                            .setDuration(250)
                            .start();
                }
                return true; // Consumed
            }

        }
        else {
            return super.onTouchEvent(motionEvent);
        }

    }

}