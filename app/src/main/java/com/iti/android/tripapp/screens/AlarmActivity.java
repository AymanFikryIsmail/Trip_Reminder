package com.iti.android.tripapp.screens;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.components.MovableIcon;
import com.iti.android.tripapp.helpers.NotificationHepler;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.services.BackgroundSoundService;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    AlertDialog.Builder alaertBuilder;
    NotificationHepler notificationHepler;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        AlarmActivity.this.setFinishOnTouchOutside(false);

//        getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        super.onCreate(savedInstanceState);
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
        Intent intentSound = new Intent(this, BackgroundSoundService.class);
        startService(intentSound);


                 getIntent().getSerializableExtra("trip");

                 //String notifState= getIntent().getStringExtra("");


        alaertBuilder =new AlertDialog.Builder(this);
        alaertBuilder.setTitle("Tripaddo")
                .setMessage("Do you want to start " + "tripname" + " trip ?")
                .setPositiveButton("start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showDirection();
                        int tripId = getIntent().getIntExtra("tripId",0);
                        // started
                        Intent intent = new Intent(AlarmActivity.this, BackgroundSoundService.class);
                        intent.setAction("cancel");
                        startService(intent);
                        startFloatingWidgetService();
                        finish();

                    }
                }).setNeutralButton("snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                 notificationHepler=new NotificationHepler(AlarmActivity.this);
                notificationManager=notificationHepler.getManager();
                notificationHepler.createNotification();

                Intent intent = new Intent(AlarmActivity.this, BackgroundSoundService.class);
                intent.setAction("cancel");
                stopService(intent);
                   }
                   }).setNegativeButton("Cancel Trip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        notificationManager.cancel(0);// trip id

//                        tripDTO.setTrip_status(com.example.omnia.easytripplanner.database.dto.Status.CANCELLED);
//                        tripDAO.updateTrip(tripDTO);
//                        Intent dismissIntent = new Intent(AlertActivity.this, RingtonePlayingService.class);
//                        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
//                        PendingIntent pendingIntent = PendingIntent.getService(AlertActivity.this, notificationID, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        startService(dismissIntent);
                        Intent intent = new Intent(AlarmActivity.this, BackgroundSoundService.class);
                        intent.setAction("cancel");
                        stopService(intent);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();

    }

    //open google maps and finish activity
    public void showDirection (){
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + 31.207751 + "," + 29.911807
                + "&travelmode=driving");
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
        Intent intent = new Intent(this, MovableIcon.class);
        ArrayList<String> noteList = new ArrayList<>();
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
}
