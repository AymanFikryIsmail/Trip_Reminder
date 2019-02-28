package com.iti.android.tripapp.ui.add_trip_mvp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.iti.android.tripapp.R;
import com.iti.android.tripapp.adapter.RawNotesAdapter;
import com.iti.android.tripapp.model.NoteDTO;
import com.iti.android.tripapp.model.Notes;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.utils.PrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity  implements AddTripView{

    private final static String API_KEY = "AIzaSyBLJv51oC0sfDw418dpUs2vu7xT4tMezyw";

    AddTripPresenter addTripPresenter;
    Double startLat ,startLng ,endLng ,endLat  ;
    EditText name, note, etDuration;
    Button addTrip;
    ImageView startDate, startTime, returnDate, returnTime, addNote ;
    TextView startDateText, startTimeText, returnDateText, returnTimeText;
    PlaceAutocompleteFragment startPlaceAutocompleteFragment, endPlaceAutocompleteFragment;
    String placeStartName ,placeDestination;
    Calendar myCalendar = Calendar.getInstance();
    Calendar currentCalendar = Calendar.getInstance();
    Calendar myCalendarRound = Calendar.getInstance();
    SwitchCompat roundSwitch;
    Spinner repeatSpinner, spPeriod;
    LinearLayout roundedLayout;
    ArrayList<NoteDTO> notes = new ArrayList<>();
    RecyclerView rvNotes;
    RawNotesAdapter adapter = null;
    boolean isRoundedTripChecked, isEditing;
    int repeatPosition, periodPosition;
    String repeated="";
    public int hours = 2;
    public int minutes;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        prefManager=new PrefManager(this);
        addTripPresenter=new AddTripPresenterImpl(this);
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

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 4) {
                    etDuration.setVisibility(View.GONE);
                    spPeriod.setVisibility(View.GONE);
                } else {
                    etDuration.setVisibility(View.VISIBLE);
                    spPeriod.setVisibility(View.VISIBLE);
                }
                repeatPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                periodPosition = i;
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
        repeatSpinner = findViewById(R.id.repeat_spinner);
        etDuration =  findViewById(R.id.period_count);
        spPeriod =  findViewById(R.id.unit_spinner);
        roundSwitch = findViewById(R.id.rounded_trip);
        roundedLayout=findViewById(R.id.rounded_layout);
        rvNotes = findViewById(R.id.notes);
        rvNotes.setNestedScrollingEnabled(false);
        rvNotes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateAlarmText();
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
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String delimiter = ":";
                        if (selectedMinute < 10)
                            delimiter = delimiter.concat("0");
                        startTimeText.setText(selectedHour + delimiter + selectedMinute);
                        myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute);
                        myCalendar.set(Calendar.SECOND, 0);
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
                int hour = myCalendarRound.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendarRound.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker2;
                mTimePicker2 = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String delimiter = ":";
                        if (selectedMinute < 10)
                            delimiter = delimiter.concat("0");
                        returnTimeText.setText(selectedHour + delimiter + selectedMinute);

                        myCalendarRound.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendarRound.set(Calendar.MINUTE, selectedMinute);
                        myCalendarRound.set(Calendar.SECOND, 0);
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
                myCalendarRound.set(Calendar.YEAR, year);
                myCalendarRound.set(Calendar.MONTH, monthOfYear);
                myCalendarRound.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                   updateAlarmTextRound();
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
    private void updateAlarmText() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        startDateText.setText(sdf.format(myCalendar.getTime()));
    }
    // update alarmDate Text
    private void updateAlarmTextRound() {
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
                placeStartName = (String) place.getName();
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
    private void reinitializeData(TripDTO trip) {
        isEditing = true;
        roundSwitch.setVisibility(View.GONE);
        name.setText(trip.getName());

        startPlaceAutocompleteFragment.setText(placeStartName = trip.getTrip_start_point());
        endPlaceAutocompleteFragment.setText(placeDestination = trip.getTrip_end_point());

        startDateText.setText(trip.getTrip_date());
        startTimeText.setText(trip.getTrip_time());
        hours = Integer.parseInt(trip.getTrip_time().split(":")[0]);
        minutes = Integer.parseInt(trip.getTrip_time().split(":")[1]);

        try {
            myCalendar.setTime(new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(trip.getTrip_date()));
            myCalendar.set(Calendar.HOUR_OF_DAY,hours);
            myCalendar.set(Calendar.MINUTE,minutes);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        repeated = trip.getRepeated();
        if (repeated.equals("")) {
            repeatSpinner.setSelection(0);
        } else if (repeated.equals("Daily")) {
            repeatSpinner.setSelection(1);
        } else if (repeated.equals("Weekly")) {
            repeatSpinner.setSelection(2);
        } else if (repeated.equals("Monthly")) {
            repeatSpinner.setSelection(3);
        } else if (repeated.lastIndexOf("Custom Period") != -1) {
            etDuration.setVisibility(View.VISIBLE);
            spPeriod.setVisibility(View.VISIBLE);
            String[] parts = repeated.split(":");
            etDuration.setText(parts[2]);
            if (parts[1].equals("d")) {
                spPeriod.setSelection(0);
            } else {
                spPeriod.setSelection(1);
            }
            repeatSpinner.setSelection(4);
        }

        startLat = trip.getTrip_start_point_latitude();
        startLng = trip.getTrip_start_point_longitude();
        endLat = trip.getTrip_end_point_latitude();
        endLng = trip.getTrip_end_point_longitude();
        notes = trip.getNotes().getNotes();
        setAdapterAfterUpdate();
    }
    public void addNote(String note) {
        notes.add(new NoteDTO(false, note));
        setAdapterAfterUpdate();
    }
    private void setAdapterAfterUpdate() {
        this.note.setText("");
        rvNotes.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new RawNotesAdapter(notes);
            rvNotes.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();
    }

    private void updateTrip(TripDTO trip) {
        TripDTO tripDTO = new TripDTO(prefManager.getUserId(), name.getText().toString(), placeStartName, placeDestination,
                startLat,startLng ,endLat,endLng , startDateText.getText().toString() , startTimeText.getText().toString()
                ,repeated,"waited", new Notes(notes),trip.getRoundStatus());
        tripDTO.setId(trip.getId());
        addTripPresenter.updateTrip(tripDTO,myCalendar);
        finish();
    }

    public void addTrip(){
        String trip_name = name.getText().toString();
        String startTime= startTimeText.getText().toString();
        String startDate=startDateText.getText().toString();
        if (trip_name.equals("") || startTime.equals("") ||startDate.equals("") ||
                placeStartName == null || placeDestination == null) {
            Toast.makeText(this, "missing fields!", Toast.LENGTH_LONG).show();
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
            } else if (repeatPosition==4) {
                if (!etDuration.getText().toString().equals("")) {
                    if (periodPosition == 0)
                        repeated="Custom Period:d:" + etDuration.getText().toString();
                    else
                        repeated="Custom Period:w:" + etDuration.getText().toString();
                }
            }
            updateFirebaseAndCreateAlarms();
            finish();
        }

    }

    private void updateFirebaseAndCreateAlarms() {
        TripDTO tripDTO = new TripDTO(prefManager.getUserId(), name.getText().toString(), placeStartName, placeDestination,
                startLat,startLng ,endLat ,endLng, startDateText.getText().toString() , startTimeText.getText().toString()
                ,repeated,"waited", new Notes(notes),"single trip");
        addTripPresenter.updateFirebaseAndCreateAlarms(tripDTO,myCalendar);
        if (isRoundedTripChecked) {
            if (myCalendarRound.compareTo(myCalendar) <= 0) {
                Toast.makeText(this, "Cannot round before going", Toast.LENGTH_SHORT).show();
            } else {
                TripDTO tripRoundDTO = new TripDTO(prefManager.getUserId(), name.getText().toString(), placeDestination, placeStartName,
                        endLat, endLng, startLat, startLng, returnDateText.getText().toString(), returnTimeText.getText().toString()
                        , repeated, "waited", new Notes(notes),"round");
                addTripPresenter.updateFirebaseAndCreateAlarms(tripRoundDTO,myCalendarRound);
            }
        }
    }
}