package com.iti.android.tripapp.screens;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
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
import com.iti.android.tripapp.model.Notes;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.services.alarm.AlarmHelper;
import com.iti.android.tripapp.utils.PrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    ImageView startDate, startTime, returnDate, returnTime, addNote ;
    TextView startDateText, startTimeText, returnDateText, returnTimeText;
    PlaceAutocompleteFragment startPlaceAutocompleteFragment, endPlaceAutocompleteFragment;
    String placeName;
    String placeDestination;
    Calendar myCalendar = Calendar.getInstance();
    Calendar currentCalendar = Calendar.getInstance();
    Calendar myCalendarRound = Calendar.getInstance();
    SwitchCompat roundSwitch;
    Spinner repeat_spinner;
    LinearLayout roundedLayout;
    ArrayList<String> notes = new ArrayList<>();
    ListView lvNotes;
    boolean isRoundedTripChecked, isEditing;
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

        final TripDTO editableTrip = (TripDTO) getIntent().getSerializableExtra("tripDTO");
        if (editableTrip != null)
            reinitializeData(editableTrip);

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
                if (!isEditing)
                    addTrip();
                else
                    updateTrip(editableTrip);
            }
        });
    }

    private void reinitializeData(TripDTO trip) {
        isEditing = true;
        roundSwitch.setVisibility(View.GONE);
        name.setText(trip.getName());

        startPlaceAutocompleteFragment.setText(placeName = trip.getTrip_start_point());
        endPlaceAutocompleteFragment.setText(placeDestination = trip.getTrip_end_point());

        startDateText.setText(trip.getTrip_date());
        startTimeText.setText(trip.getTrip_time());
        hours = Integer.parseInt(trip.getTrip_time().split(":")[0]);
        minutes = Integer.parseInt(trip.getTrip_time().split(":")[1]);

        try {
            myCalendar.setTime(new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(trip.getTrip_date()));
            years = myCalendar.get(Calendar.YEAR);
            months = myCalendar.get(Calendar.MONTH);
            days = myCalendar.get(Calendar.DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        repeated = trip.getRepeated();
        switch (repeated) {
            case "":
                repeat_spinner.setSelection(0);
                break;
            case "Daily":
                repeat_spinner.setSelection(1);
                break;
            case "Weekly":
                repeat_spinner.setSelection(2);
                break;
            case "Monthly":
                repeat_spinner.setSelection(3);
                break;
        }
        startLat = trip.getTrip_start_point_latitude();
        startLng = trip.getTrip_start_point_longitude();
        endLat = trip.getTrip_end_point_latitude();
        endLng = trip.getTrip_end_point_longitude();
        notes = trip.getNotes().getNotes();
        setAdapterAfterUpdate();
    }

    private void updateTrip(TripDTO trip) {
        TripDTO tripDTO = new TripDTO(prefManager.getUserId(), name.getText().toString(), placeName, placeDestination,
                startLat,startLng ,endLng,endLat , startDateText.getText().toString() , startTimeText.getText().toString()
                ,repeated,"waited", new Notes(notes));

        tripDTO.setId(trip.getId());
        MyAppDB.getAppDatabase(this).tripDao().updateTrip(tripDTO);
        fireBaseHelper.updateTripOnFirebase(tripDTO);
        AlarmHelper.setAlarm(this,tripDTO,myCalendar);

        finish();
    }

    void initView(){
        name =  findViewById(R.id.tripName_input);
        startDate =  findViewById(R.id.start_date);
        startTime =  findViewById(R.id.start_time);
        returnDate =  findViewById(R.id.return_date);
        returnTime =  findViewById(R.id.return_time);
        startDateText =  findViewById(R.id.start_date_text);
        startTimeText =  findViewById(R.id.start_time_text);
        returnDateText =  findViewById(R.id.return_date_text);
        returnTimeText =  findViewById(R.id.return_time_text);
        repeat_spinner = findViewById(R.id.repeat_spinner);
        roundSwitch = findViewById(R.id.rounded_trip);
        roundedLayout=findViewById(R.id.rounded_layout);
        lvNotes = findViewById(R.id.notes);
        note = findViewById(R.id.et_note);
        addNote = findViewById(R.id.add_notes);
        addTrip = findViewById(R.id.add_trip_id);
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
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddTripActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // click listener on alarmClock EditText
        startTime.setOnClickListener(new View.OnClickListener() {

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
                        String delimiter = ":";
                        if (selectedMinute < 10)
                            delimiter = delimiter.concat("0");
                        startTimeText.setText(selectedHour + delimiter + selectedMinute);
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
        returnTime.setOnClickListener(new View.OnClickListener() {

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
                        String delimiter = ":";
                        if (selectedMinute < 10)
                            delimiter = delimiter.concat("0");
                        returnTimeText.setText(selectedHour + delimiter + selectedMinute);
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
        returnDate.setOnClickListener(new View.OnClickListener() {
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
        startDateText.setText(sdf.format(myCalendar.getTime()));
    }

    // update alarmDate Text
    private void updateLabelRound() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        returnDateText.setText(sdf.format(myCalendarRound.getTime()));
    }

    void initAutoComplete(){

        Places.initialize(getApplicationContext(), API_KEY);

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
        setAdapterAfterUpdate();
    }

    private void setAdapterAfterUpdate() {
        String[] strings = new String[notes.size()];
        notes.toArray(strings);
        this.note.setText("");
        lvNotes.setVisibility(View.VISIBLE);
        lvNotes.setAdapter(new ArrayAdapter<>(this, R.layout.raw_note, strings));
        lvNotes.setSelection(notes.size() - 1);
    }

    public void addTrip(){
        String trip_name = name.getText().toString();

        if (trip_name.matches("") || startTimeText.getText().toString().matches("") ||
                startDateText.getText().toString().matches("") || placeName == null ||
                placeDestination == null) {
            Toast.makeText(this, "missing fields !", Toast.LENGTH_LONG).show();

        } else if (myCalendar.compareTo(currentCalendar) <= 0) {

            Toast.makeText(this, "cannot insert passed time", Toast.LENGTH_SHORT).show();
        }  else {

            if (repeatPosition == 0) {
                repeated="";
            }else if (repeatPosition==1) {
                repeated="Daily";
            }else if (repeatPosition==2) {
                repeated="Weekly";
            }else if (repeatPosition==3) {
                repeated="Monthly";
            }

            updateFirebaseAndCreateAlarms();

            finish();

        }

    }

    private void updateFirebaseAndCreateAlarms() {
        TripDTO tripDTO = new TripDTO(prefManager.getUserId(), name.getText().toString(), placeName, placeDestination,
                startLat,startLng ,endLat ,endLng, startDateText.getText().toString() , startTimeText.getText().toString()
                ,repeated,"waited", new Notes(notes));
        int tripId= (int) MyAppDB.getAppDatabase(this).tripDao().addTrip(tripDTO);
        tripDTO.setId(tripId);
        fireBaseHelper.createTripOnFirebase(tripDTO);
        AlarmHelper.setAlarm(this,tripDTO,myCalendar);
        if (isRoundedTripChecked){
            TripDTO tripRoundDTO=new TripDTO(prefManager.getUserId(), name.getText().toString(), placeDestination, placeName,
                    endLng,endLat ,startLng,startLat , returnDateText.getText().toString() , returnTimeText.getText().toString()
                    ,repeated,"waited", new Notes(notes));
            int tripRoundId= (int) MyAppDB.getAppDatabase(this).tripDao().addTrip(tripRoundDTO);
            tripRoundDTO.setId(tripRoundId);
            fireBaseHelper.createTripOnFirebase(tripRoundDTO);
            AlarmHelper.setAlarm(this,tripRoundDTO,myCalendarRound);
        }
    }
}