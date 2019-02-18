package com.iti.android.tripapp.screens;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.iti.android.tripapp.R;
import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.services.alarm.AlarmHelper;
import com.iti.android.tripapp.utils.PrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity {

    private final static String API_KEY = "AIzaSyBLJv51oC0sfDw418dpUs2vu7xT4tMezyw";

    Double startLng = 0.0d;
    Double startLat;
    Double endLng;
    Double endLat;
    String TAG = " asdasdasdadsasdasd";
    LatLng StartLatLang;
    LatLng endLatLang;
    EditText name, note;
    Button addTrip;
    ImageView start_date ,start_time ,return_date ,return_time, addNote ;
    TextView start_date_text ,start_time_text , return_date_text ,return_time_text ;
    String placeName;
    String placeDestination;
    Calendar myCalendar = Calendar.getInstance();
    Calendar currentCalendar = Calendar.getInstance();
    Calendar myCalendarRound = Calendar.getInstance();
    SwitchCompat roundSwitch;
    Spinner repeat_spinner;
    LinearLayout roundedLayout;
    List<String> notes = new ArrayList<>();
    ListView lvNotes;
    boolean isRoundedTripChecked;
    int repeatPosition;
    String repeated="";
    int years;
    int months;
    int days;
    public int hours = 2;
    public int minutes;
    int years2;
    int months2;
    int days2;
    public int hours2 = 2;
    public int minutes2;

    private FireBaseHelper fireBaseHelper;

    private transient ProgressDialog progressDialog = null;

    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        fireBaseHelper=new FireBaseHelper();
        prefManager=new PrefManager(this);
        initView();
        initAutoComplete();

        initDate_Time();
        initBackDateTime();
        roundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    roundedLayout.setVisibility(View.VISIBLE);
                    isRoundedTripChecked = true;
                } else {
                    roundedLayout.setVisibility(View.GONE);
                    isRoundedTripChecked = false;
                }
            }
        });

        repeat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                repeatPosition = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!note.getText().toString().equals(""))
                    addNote(note.getText().toString());
            }
        });

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTrip();
            }
        });
    }
    void initView(){
        name =  findViewById(R.id.tripName_input);
        start_date =  findViewById(R.id.start_date);
        start_time =  findViewById(R.id.start_time);
        return_date =  findViewById(R.id.return_date);
        return_time =  findViewById(R.id.return_time);
        start_date_text =  findViewById(R.id.start_date_text);
        start_time_text =  findViewById(R.id.start_time_text);
        return_date_text =  findViewById(R.id.return_date_text);
        return_time_text =  findViewById(R.id.return_time_text);
        repeat_spinner = findViewById(R.id.repeat_spinner);
        roundSwitch = findViewById(R.id.roundedTrip);
        roundedLayout=findViewById(R.id.roundedLayout);
        lvNotes = findViewById(R.id.notes);
        note = findViewById(R.id.et_note);
        addNote = findViewById(R.id.add_notes);
        addTrip = findViewById(R.id.addTripId);
        myCalendar.setTimeInMillis(System.currentTimeMillis());
        currentCalendar.setTimeInMillis(System.currentTimeMillis());
    }

    void initDate_Time(){
        // set my Calendar date
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                years = year;
                months = monthOfYear;
                days = dayOfMonth;
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        // when alarmDate editText is clicked
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTripActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // click listener on alarmClock EditText
        start_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        start_time_text.setText(selectedHour + ":" + selectedMinute);
                        minutes = selectedMinute;
                        hours = selectedHour;
                        myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute - 1);
                        myCalendar.set(Calendar.SECOND, 59);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

    void initBackDateTime(){
        // click listener on alarmClock EditText
        return_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mCurrentTime2 = Calendar.getInstance();
                int hour = mCurrentTime2.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime2.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker2;
                mTimePicker2 = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        return_time_text.setText(selectedHour + ":" + selectedMinute);
                        minutes2 = selectedMinute;
                        hours2 = selectedHour;

                        myCalendarRound.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendarRound.set(Calendar.MINUTE, selectedMinute - 1);
                        myCalendarRound.set(Calendar.SECOND, 59);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker2.setTitle("Select Time");
                mTimePicker2.show();
            }
        });
        ////////////////////////////////// round picker ///////////////////////////////////////
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                years2 = year;
                months2 = monthOfYear;
                days2 = dayOfMonth;
                myCalendarRound.set(Calendar.YEAR, year);
                myCalendarRound.set(Calendar.MONTH, monthOfYear);
                myCalendarRound.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                   updateLabelRound();
            }
        };

        // when alarmDate editText is clicked
        return_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTripActivity.this, date1, myCalendarRound
                        .get(Calendar.YEAR), myCalendarRound.get(Calendar.MONTH),
                        myCalendarRound.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    // update alarmDate Text
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        start_date_text.setText(sdf.format(myCalendar.getTime()));
    }

    // update alarmDate Text
    private void updateLabelRound() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        return_date_text.setText(sdf.format(myCalendarRound.getTime()));
    }

    void initAutoComplete(){

        Places.initialize(getApplicationContext(), API_KEY);

        PlaceAutocompleteFragment startPlaceAutocompleteFragment, endPlaceAutocompleteFragment;
        startPlaceAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment_from);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        startPlaceAutocompleteFragment.setFilter(autocompleteFilter);

        startPlaceAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Toast.makeText(getApplicationContext(),place.getName().toString(),Toast.LENGTH_SHORT).show();
                placeName = (String) place.getName();
                startLng = place.getLatLng().longitude;
                startLat = place.getLatLng().latitude;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        endPlaceAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment_to);
        endPlaceAutocompleteFragment.setFilter(autocompleteFilter);

        endPlaceAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(getApplicationContext(),place.getName().toString(),Toast.LENGTH_SHORT).show();
                placeDestination = (String) place.getName();
                endLng = place.getLatLng().longitude;
                endLat = place.getLatLng().latitude;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        AutocompleteFilter typeFilter1 = new AutocompleteFilter.Builder()
                .setCountry("EG")
                .build();
        startPlaceAutocompleteFragment.setFilter(typeFilter1);
        endPlaceAutocompleteFragment.setFilter(typeFilter1);
    }

    public void addNote(String note) {
        notes.add(note);
        String[] strings = new String[notes.size()];
        notes.toArray(strings);
        this.note.setText("");
        lvNotes.setVisibility(View.VISIBLE);
        lvNotes.setAdapter(new ArrayAdapter<>(this, R.layout.raw_note, strings));
        lvNotes.setSelection(notes.size() - 1);
    }

    public void addTrip(){
        String trip_name = name.getText().toString();

        String trip_start_point = placeName;
        String trip_end_point = placeDestination;
        if (trip_name.matches("") || start_time_text.getText().toString().matches("") ||
                start_date_text.getText().toString().matches("")   ) {
            Toast.makeText(this, "missing fields !", Toast.LENGTH_LONG).show();

        } else if (myCalendar.compareTo(currentCalendar) <= 0) {

            Toast.makeText(this, "cannot insert passed time", Toast.LENGTH_SHORT).show();
        }  else {

            if (repeatPosition == 0) {
                repeated="";
            }else if (repeatPosition==1){
                repeated="Daily";
            }else if (repeatPosition==2) {
                repeated="Weekly";
            }else if (repeatPosition==3) {
                repeated="Monthly";
            }
            TripDTO tripDTO=new TripDTO(prefManager.getUserId() ,trip_name, trip_start_point , trip_end_point,
                    startLat,startLng ,endLng,endLat ,start_date_text.getText().toString() ,start_time_text.getText().toString()
                     ,repeated,"waited");
            int tripId= (int) MyAppDB.getAppDatabase(this).tripDao().addTrip(tripDTO);
            tripDTO.setId(tripId);
            fireBaseHelper.createTripOnFirebase(tripDTO);

            //alarm logic
            AlarmHelper.setAlarm(this,tripDTO,myCalendar);
//            m.trips.add(tripDTO);
//            m.adapter.notifyDataSetChanged();
            finish();

        }

    }

    public void gotoHomeActivity() {
//        firebaseDatabaseDAO.createAndUpdateTripOnFirebase(trip);
//        AlarmHelper.setAlarm(this, trip);
        Toast.makeText(getApplicationContext(), "Trip added.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
       // progressDialog.dismiss();
        finish();

    }
}