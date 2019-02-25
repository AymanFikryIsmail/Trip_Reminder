package com.iti.android.tripapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.model.map_model.GsonResponse;
import com.iti.android.tripapp.model.map_model.MapLeg;
import com.iti.android.tripapp.model.map_model.MapResponse;
import com.iti.android.tripapp.services.FloatingIconService;
import com.iti.android.tripapp.services.alarm.AlarmHelper;
import com.iti.android.tripapp.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ayman on 2019-02-08.
 */

public class HistoryTripAdapter extends RecyclerView.Adapter<HistoryTripAdapter.MyViewHolder>{

    private Context context;
    private List<TripDTO> associationsTitle =  new ArrayList<>();

    private PrefManager prefManager;
    private FireBaseHelper fireBaseHelper;

    RecyclerView rvShowNotes;
    private  ShowDetailsAdapter adapter;
    TextView  trip_name , trip_distance , trip_duration;
    public HistoryTripAdapter(Context context,List<TripDTO> tripDTOList){
        this.context = context;
        this.associationsTitle = tripDTOList;
        prefManager=new PrefManager(context);
        fireBaseHelper=new FireBaseHelper();

    }

    @Override
    public HistoryTripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_row, parent, false);
        return new HistoryTripAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryTripAdapter.MyViewHolder holder, int position) {
        holder.bind(associationsTitle.get(position));
    }


    @Override
    public int getItemCount() {
        return associationsTitle.size();
    }


    //----------------------------------View Holder Class-------------------------------------------
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tripTV , timeTv, dateTv, startLoc , endLoc ,popupMenuTxt;
        public ImageView tripImage  ;
        public CardView gridCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            gridCardView = itemView.findViewById(R.id.card_view);
            tripTV = itemView.findViewById(R.id.tripTV);
            tripImage = itemView.findViewById(R.id.tripImage);
            timeTv = itemView.findViewById(R.id.timeTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            startLoc = itemView.findViewById(R.id.startLoc);
            endLoc = itemView.findViewById(R.id.endLoc);
            popupMenuTxt = itemView.findViewById(R.id.popupMenuId);


        }

        public void bind(final TripDTO tripDTO){

            tripTV.setText(tripDTO.getName());
            startLoc.setText("from :" +tripDTO.getTrip_start_point());
            endLoc.setText("to :" +tripDTO.getTrip_end_point());
            timeTv.setText(tripDTO.getTrip_time());
            dateTv.setText(tripDTO.getTrip_date());

//            Picasso.with(context).load("")
//                    .fit().centerCrop()
//                    .placeholder(R.drawable.ic_home_black_24dp)
//                    .into(associationImage);

            popupMenuTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    @SuppressLint("RestrictedApi")
                    Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenu);

                    PopupMenu popupMenu =new PopupMenu(wrapper,popupMenuTxt);
                    popupMenu.inflate(R.menu.trip_history_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.note:
                                    View dialogView = LayoutInflater.from(context).inflate(R.layout.show_notes , null, false);
                                    rvShowNotes = (RecyclerView) dialogView.findViewById(R.id.showNotes);
                                    TextView  trip_name =  dialogView.findViewById(R.id.trip_name);
                                    trip_distance =  dialogView.findViewById(R.id.trip_distance);
                                    trip_duration =  dialogView.findViewById(R.id.trip_duration);

                                    trip_name.setText(tripDTO.getName());
                                    getMAp(tripDTO.getTrip_start_point_latitude(), tripDTO.getTrip_start_point_longitude(),
                                            tripDTO.getTrip_end_point_latitude() ,tripDTO.getTrip_end_point_longitude(),
                                            trip_distance ,trip_duration);
                                    rvShowNotes.setLayoutManager(new LinearLayoutManager(context));
                                    adapter= new ShowDetailsAdapter(tripDTO.getNotes().getNotes());
                                    rvShowNotes.setAdapter(adapter);
                                    //}
                                    AlertDialog.Builder build = new AlertDialog.Builder(context);
                                    build.setView(dialogView);

                                    final AlertDialog alertDialog = build.create();
                                    alertDialog.show();

                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            gridCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                                   }
            });

        }
    }

    public  void getMAp(double stLat, double stLng , double endLat , double endLng , final TextView  trip_distance , final TextView trip_duration){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GsonResponse service =retrofit.create(GsonResponse.class);
        String key="AIzaSyCeYHDhDctqGmb5APIdyWrd-imDO2DkQHc";
        Call<MapResponse> call=service.getCountries(stLat+","+stLng,endLat+","+endLng,key);
//                   AIzaSyCeYHDhDctqGmb5APIdyWrd-imDO2DkQHc&fbclid=IwAR0SzqGJcx4O8HkvLij_sXZuFCgkad_lntijQD05XybFpDPdIuJWmtn5aeQ
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                List<MapLeg> mapRoutes=new ArrayList<>();
                String distance = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();
                String duration = response.body().getRoutes().get(0).getLegs().get(0).getDuration().getText();

                trip_distance.setText("Trip distance : " + distance);
                trip_duration.setText("Trip duration : " + duration);

            }
            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

                Toast.makeText(context, "",Toast.LENGTH_LONG).show();
            }
        });

    }
}
