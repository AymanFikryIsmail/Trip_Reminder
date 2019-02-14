package com.iti.android.tripapp.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.iti.android.tripapp.R;

import java.util.Arrays;
import java.util.List;

public class AddTripActivity extends AppCompatActivity {

    private final static String API_KEY = "AIzaSyDmsxdbTxg9asm9axY_HbgHw3UtSsSYcpk";
    private final static String PLACE_NO = "PlaceNo";
    private final static int REQUEST_CODE = 1;
    private final static int SOURCE = 1;
    private final static int DESTINATION = 2;

    TextView tvSource;
    TextView tvDest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        Button btnSource = findViewById(R.id.btn_source);
        Button btnDest = findViewById(R.id.btn_dest);
        Places.initialize(getApplicationContext(), API_KEY);
        btnSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(SOURCE);
            }
        });

        btnDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent(DESTINATION);
            }
        });
    }

    public void sendIntent(int placeNo) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(AddTripActivity.this);
        startActivityForResult(intent.putExtra(PLACE_NO, placeNo), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("MYTAG", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                if (data.getIntExtra(PLACE_NO, 0) == SOURCE)
                    tvSource.setText(place.getAddress());
                else if (data.getIntExtra(PLACE_NO, 0) == DESTINATION)
                    tvDest.setText(place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("MYTAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
