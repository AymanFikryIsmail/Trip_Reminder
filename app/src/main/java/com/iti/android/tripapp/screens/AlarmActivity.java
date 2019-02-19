package com.iti.android.tripapp.screens;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.helpers.NotificationHelper;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.services.BackgroundSoundService;
import com.iti.android.tripapp.services.FloatingIconService;
import com.iti.android.tripapp.services.alarm.AlarmHelper;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    AlertDialog.Builder alertBuilder;
    NotificationHelper notificationHelper;
    private NotificationManager notificationManager;
    TripDTO tripDTO;
    MediaPlayer player;
    private FireBaseHelper fireBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setFinishOnTouchOutside(false);
        fireBaseHelper=new FireBaseHelper();

//        getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        super.onCreate(savedInstanceState);
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
//        Intent intentSound = new Intent(this, BackgroundSoundService.class);
//        startService(intentSound);

        player = MediaPlayer.create(this, R.raw.notification);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        player.start();
        notificationHelper=new NotificationHelper(this);
        notificationManager=notificationHelper.getManager();

        final int tripid= getIntent().getIntExtra("tripid",0);
         tripDTO= MyAppDB.getAppDatabase(this).tripDao().getTrip(tripid);

        alertBuilder =new AlertDialog.Builder(this);
        alertBuilder.setTitle("Tripaddo")
                .setMessage("Do you want to start " + "" + " trip ?")
                .setPositiveButton("start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int tripId = getIntent().getIntExtra("tripId",0);
                        player.stop();
                        player.release();
                        tripDTO.setTripStatus("started");
                        tripDTO.setId(tripId);
                        // update in fire base
                        MyAppDB.getAppDatabase(AlarmActivity.this).tripDao().updateTour(tripDTO);
                        AlarmHelper.cancelAlarm(getApplicationContext(),tripId);
                        notificationManager.cancel(tripid);

                        showDirection();
                        startFloatingWidgetService();
                        finish();

                    }
                })
                .setNeutralButton("snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        player.stop();
                        player.release();
                        AlarmHelper.cancelAlarm(getApplicationContext(),tripid);
                        notificationHelper.createNotification(tripDTO);
                        finish();

                    }
                })
                .setNegativeButton("Cancel Trip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyAppDB.getAppDatabase(AlarmActivity.this).tripDao().delete(tripDTO);
                        fireBaseHelper.removeTripFromFirebase(tripDTO);
                        player.stop();
                        player.release();
                        notificationManager.cancel(tripid);
                        AlarmHelper.cancelAlarm(getApplicationContext(),tripid);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();

    }

    //open google maps and finish activity
    public void showDirection (){
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tripDTO.getTrip_end_point_latitude()
                + "," + tripDTO.getTrip_end_point_longitude()+ "&travelmode=driving");
        //Uri.parse("http://maps.google.com/maps?saddr=" + 31.267048 + "," + 29.994168 + "&daddr=" +31.207751 + "," + 29.911807));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(mapIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
        }

      //  AlarmHelper.cancelAlarm(getApplicationContext(), trip.getUserId(), trip.getTripId());
        finish();
    }
    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        Intent intent = new Intent(this, FloatingIconService.class);
        ArrayList<String> noteList = new ArrayList<>();
        //TODO Receive actual data from db
        noteList.add("Note1");
        noteList.add("Note2");
        noteList.add("Note3");
        noteList.add("Note4");
        noteList.add("Note5");
        noteList.add("Note6");
        intent.putExtra("noteList", noteList);
        startService(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (player!=null) {
//            player.stop();
//            player.release();
//        }
    }
}
