package com.iti.android.tripapp.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.adapter.NotesAdapter;
import com.iti.android.tripapp.model.NoteDTO;

import java.util.ArrayList;
import java.util.List;

public class FloatingIconService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingIcon;
    private List<NoteDTO> notes = new ArrayList<>();

    public FloatingIconService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<String> notesContent = intent.getStringArrayListExtra("noteList");
        for (String note : notesContent) {
            notes.add(new NoteDTO(false, note));
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingIcon = LayoutInflater.from(this).inflate(R.layout.floating_icon, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        //Specify the view position
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //Add the view to the window
        mWindowManager.addView(mFloatingIcon, params);

        final View collapsedView = mFloatingIcon.findViewById(R.id.collapsed);
        final View expandedView = mFloatingIcon.findViewById(R.id.expanded);

        ImageView closeButtonCollapsed = mFloatingIcon.findViewById(R.id.btn_close);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        mFloatingIcon.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {

            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private final static float CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int xDiff = (int) (event.getRawX() - initialTouchX);
                        int yDiff = (int) (event.getRawY() - initialTouchY);
                        if (xDiff < CLICK_DRAG_TOLERANCE && yDiff < CLICK_DRAG_TOLERANCE) {
                            collapsedView.setVisibility(View.GONE);
                            expandedView.setVisibility(View.VISIBLE);

                            final RecyclerView rv = mFloatingIcon.findViewById(R.id.rv_notes);

                            rv.setLayoutManager(new LinearLayoutManager(v.getContext()));
                            rv.setAdapter(new NotesAdapter(notes));

                            Button dialogButton = mFloatingIcon.findViewById(R.id.btn_apply);
                            // if button is clicked, close the custom dialog
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    collapsedView.setVisibility(View.VISIBLE);
                                    expandedView.setVisibility(View.GONE);
                                    notes = ((NotesAdapter)rv.getAdapter()).getNotes();
                                }
                            });
                            v.performClick();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingIcon, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingIcon != null) mWindowManager.removeView(mFloatingIcon);
    }
}
