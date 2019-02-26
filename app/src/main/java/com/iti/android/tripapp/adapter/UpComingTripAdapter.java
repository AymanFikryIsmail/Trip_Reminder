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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iti.android.tripapp.R;
import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.Notes;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.model.map_model.GsonResponse;
import com.iti.android.tripapp.model.map_model.MapLeg;
import com.iti.android.tripapp.model.map_model.MapResponse;
import com.iti.android.tripapp.ui.add_trip_mvp.AddTripActivity;
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

public class UpComingTripAdapter extends RecyclerView.Adapter<UpComingTripAdapter.MyViewHolder>{

    private Context context;
    private List<TripDTO> associationsTitle;

    private PrefManager prefManager;
    private FireBaseHelper fireBaseHelper;
    private  ShowDetailsAdapter adapter;
    RecyclerView rvShowNotes;
    String  url;
    public UpComingTripAdapter(Context context,List<TripDTO> tripDTOList){
        this.context = context;
        this.associationsTitle = tripDTOList;
        prefManager=new PrefManager(context);
        fireBaseHelper=new FireBaseHelper();
    }

    @Override
    public UpComingTripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UpComingTripAdapter.MyViewHolder holder, int position) {
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
            getMAp(tripDTO.getTrip_start_point_latitude(), tripDTO.getTrip_start_point_longitude(),
                    tripDTO.getTrip_end_point_latitude() ,tripDTO.getTrip_end_point_longitude(),tripImage);

            tripTV.setText(tripDTO.getName());
            startLoc.setText("From : " +tripDTO.getTrip_start_point());
            endLoc.setText("To : " +tripDTO.getTrip_end_point());
            timeTv.setText(tripDTO.getTrip_time());
            dateTv.setText(tripDTO.getTrip_date());

            popupMenuTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    @SuppressLint("RestrictedApi")
                    Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenu);

                    PopupMenu popupMenu =new PopupMenu(wrapper,popupMenuTxt);
                    popupMenu.inflate(R.menu.trip_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.start:
                                    // update status started also update in firebase

                                    tripDTO.setTripStatus("started");
                                    MyAppDB.getAppDatabase(context).tripDao().updateTrip(tripDTO);
                                    AlarmHelper.cancelAlarm(context.getApplicationContext(), tripDTO.getId());
                                   //update fire base
                                    fireBaseHelper.updateTripOnFirebase(tripDTO);
                                    showDirection(tripDTO);
                                    startFloatingWidgetService(tripDTO);
                                    break;
                                case R.id.edit:
                                    // open edit page
                                    context.startActivity(new Intent(context, AddTripActivity.class).putExtra("tripDTO", tripDTO));

                                    break;
                                case R.id.delete:

                                    // delete from db and firebase

                                    final AlertDialog alertdialog;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Delete Confirmation");
                                    builder.setMessage("Are you sure to delete");
                                    builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface _dialog, int which) {
                                            //do your work here
                                            // update status started also update in firebase
                                            MyAppDB.getAppDatabase(context).tripDao().delete(tripDTO);
                                            AlarmHelper.cancelAlarm(context.getApplicationContext(), tripDTO.getId());
                                            fireBaseHelper.removeTripFromFirebase(tripDTO);
                                            associationsTitle.remove(tripDTO);
                                            notifyDataSetChanged();
                                        }
                                    });
                                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface _dialog, int which) {
                                            _dialog.dismiss();
                                        }
                                    });

                                    alertdialog = builder.create();
                                    alertdialog.show();
                                    break;
                                case R.id.note :

                                    // ViewGroup viewGroup = findViewById(android.R.id.content);
                                    View dialogView = LayoutInflater.from(context).inflate(R.layout.show_notes , null, false);

                                    AlertDialog.Builder build = new AlertDialog.Builder(context);
                                  TextView  trip_name =  dialogView.findViewById(R.id.trip_name);
                                    dialogView.findViewById(R.id.trip_distance).setVisibility(View.GONE);
                                     dialogView.findViewById(R.id.trip_duration).setVisibility(View.GONE);
                                    ImageView mapImg =  dialogView.findViewById(R.id.mapImg);
                                    trip_name.setText(tripDTO.getName());
                                    Glide.with(context).load(url).apply(RequestOptions.fitCenterTransform()
                                            .placeholder(R.drawable.app_logo))
                                            .into(mapImg);

                                    rvShowNotes = (RecyclerView) dialogView.findViewById(R.id.showNotes);
                                    rvShowNotes.setLayoutManager(new LinearLayoutManager(context));
//                                    if (adapter==null){
                                    adapter= new ShowDetailsAdapter(tripDTO.getNotes().getNotes());
                                    rvShowNotes.setAdapter(adapter);
//                                    }
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



            }
    }

    public  void getMAp(double stLat, double stLng , double endLat , double endLng,final ImageView imageView ){
        final double avgLat=(stLat+endLat)/2;
        final double avgLong=(stLng+endLng)/2;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GsonResponse service =retrofit.create(GsonResponse.class);
        String key="AIzaSyCeYHDhDctqGmb5APIdyWrd-imDO2DkQHc";
        Call<MapResponse> call=service.getCountries(stLat+","+stLng,endLat+","+endLng,key);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                List<MapLeg> mapRoutes=new ArrayList<>();

                String point= response.body().getRoutes().get(0).getOverview_polyline().getPoints();
                 url="https://maps.googleapis.com/maps/api/staticmap?center=" + avgLat + "," + avgLong + "&" +
                        "zoom=8&size=500x200&maptype=roadmap&path=weight:7%10Ccolor:orange%7Cenc:" + point
                        + "&key=AIzaSyCeYHDhDctqGmb5APIdyWrd-imDO2DkQHc";

                Glide.with(context).load(url).apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.logo3))
                        .into(imageView);
            }
            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

                //Toast.makeText(context, "",Toast.LENGTH_LONG).show();
            }
        });

    }
    //open google maps and finish activity
    public void showDirection (TripDTO tripDTO){
        Uri gmmIntentUri1 = Uri.parse("google.navigation:q=" + tripDTO.getTrip_end_point_latitude() + "," +
                tripDTO.getTrip_start_point_longitude()
                + "&travelmode=driving");
        //Uri.parse("http://maps.google.com/maps?saddr=" + 31.267048 + "," + 29.994168 + "&daddr=" +31.207751 + "," + 29.911807));
        Uri gmmIntentUri =Uri.parse("http://maps.google.com/maps?saddr=" +tripDTO.getTrip_start_point_latitude() + "," +
                tripDTO.getTrip_start_point_longitude()
                + "&daddr=" +tripDTO.getTrip_end_point_latitude() + "," + tripDTO.getTrip_end_point_longitude());

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity( context.getPackageManager()) != null) {
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
        }
          AlarmHelper.cancelAlarm(context, tripDTO.getId());
    }
    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService(TripDTO tripDTO) {
        Intent intent = new Intent(context, FloatingIconService.class);
        Notes notes = tripDTO.getNotes();
        if (notes.getNotes().size() != 0) {
            intent.putExtra("noteList", tripDTO);
            context.startService(intent);
        }
    }

}
