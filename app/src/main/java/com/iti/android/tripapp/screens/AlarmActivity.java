package com.iti.android.tripapp.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.iti.android.tripapp.R;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toast.makeText(this,"hi ",Toast.LENGTH_LONG).show();
    }
}
