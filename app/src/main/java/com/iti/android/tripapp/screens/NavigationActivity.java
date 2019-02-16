package com.iti.android.tripapp.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.components.MovableIcon;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        MovableIcon notesInvoker = new MovableIcon(this);
    }
}
