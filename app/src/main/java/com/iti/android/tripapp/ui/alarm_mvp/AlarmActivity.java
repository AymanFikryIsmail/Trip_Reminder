package com.iti.android.tripapp.ui.alarm_mvp;

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
import com.iti.android.tripapp.model.Notes;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.services.FloatingIconService;
import com.iti.android.tripapp.services.alarm.AlarmHelper;
import com.iti.android.tripapp.ui.register_mvp.RegisterPresenterImpl;


public class AlarmActivity extends AppCompatActivity implements AlarmView{

    AlertDialog.Builder alertBuilder;
    NotificationHelper notificationHelper;
    private NotificationManager notificationManager;
    TripDTO tripDTO;
    MediaPlayer player;
    private FireBaseHelper fireBaseHelper;

    private AlarmPresenter alarmPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setFinishOnTouchOutside(false);
        alarmPresenter = new AlarmPresenterImpl(this);

        player = MediaPlayer.create(this, R.raw.notification);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        player.start();

        final int tripId= getIntent().getIntExtra("tripid",0);
        tripDTO= MyAppDB.getAppDatabase(this).tripDao().getTrip(tripId);
        alertBuilder =new AlertDialog.Builder(this);
        alertBuilder.setTitle("Tripado")
                .setMessage("Do you want to start " + tripDTO.getName() + " trip?")
                .setPositiveButton("start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        player.stop();
                        player.release();
                        alarmPresenter.startTrip(tripDTO,tripId);
                        finish();
                    }
                })
                .setNeutralButton("snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        player.stop();
                        player.release();
                        alarmPresenter.snoozeTrip(tripDTO,tripId);

                        finish();
                    }
                })
                .setNegativeButton("Cancel Trip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        player.stop();
                        player.release();
                        alarmPresenter.cancelTrip(tripDTO,tripId);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();

    }

    //open google maps and finish activity
    public void showDirection (){
        Uri gmmIntentUri1 = Uri.parse("google.navigation:q=" + tripDTO.getTrip_end_point_latitude()
                + "," + tripDTO.getTrip_end_point_longitude()+ "&travelmode=driving");
        Uri gmmIntentUri =Uri.parse("http://maps.google.com/maps?saddr=" +tripDTO.getTrip_start_point_latitude() +
                "," + tripDTO.getTrip_start_point_longitude()
                + "&daddr=" +tripDTO.getTrip_end_point_latitude() + "," + tripDTO.getTrip_end_point_longitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(mapIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
        }
        finish();
    }
    /*  Start Floating widget service and finish current activity */
    public void startFloatingWidgetService() {
        Intent intent = new Intent(this, FloatingIconService.class);
        Notes notes = tripDTO.getNotes();
        if (notes.getNotes().size() != 0) {
            Log.i("TAG", "calling service");
            intent.putExtra("noteList", tripDTO);
            startService(intent);
        }
    }
}
