package com.iti.android.tripapp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
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
import com.iti.android.tripapp.services.FloatingIconService;
import com.iti.android.tripapp.services.alarm.AlarmHelper;
import com.iti.android.tripapp.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayman on 2019-02-08.
 */

public class UpComingTripAdapter extends RecyclerView.Adapter<UpComingTripAdapter.MyViewHolder>{

    private Context context;
    private List<TripDTO> associationsTitle =  new ArrayList<>();

    private PrefManager prefManager;
    private FireBaseHelper fireBaseHelper;

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

            tripTV.setText(tripDTO.getName());
            startLoc.setText("from :" +tripDTO.getTrip_start_point());
            endLoc.setText("to :" +tripDTO.getTrip_end_point());
            timeTv.setText(tripDTO.getTrip_time());
            dateTv.setText(tripDTO.getTrip_date());

//                    .diskCacheStrategy(DiskCacheStrategy.ALL)//"https://aymanfikryeng.000webhostapp.com/images/Resturants/La-Casona-logo-for-a-restaurant.jpg")//
//                   .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_home_black_24dp)).into(associationImage);// .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_home_black_24dp))
//            Picasso.with(context).load("")
//                    .fit().centerCrop()
//                    .placeholder(R.drawable.ic_home_black_24dp)
//                    .into(associationImage);
                gridCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });



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
                                   MyAppDB.getAppDatabase(context).tripDao().updateTour(tripDTO);
                                   //update fire base
                                    showDirection(tripDTO);
                                    startFloatingWidgetService();
                                    break;
                                case R.id.edit:
                                    // open edit page


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
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });



            }
    }
    //open google maps and finish activity
    public void showDirection (TripDTO tripDTO){
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tripDTO.getTrip_end_point_latitude() + "," +
                tripDTO.getTrip_start_point_longitude()
                + "&travelmode=driving");
        //Uri.parse("http://maps.google.com/maps?saddr=" + 31.267048 + "," + 29.994168 + "&daddr=" +31.207751 + "," + 29.911807));
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
    private void startFloatingWidgetService() {
        Intent intent = new Intent(context, FloatingIconService.class);
        ArrayList<String> noteList = new ArrayList<>();
        //TODO Receive actual data from db
        noteList.add("Note1");
        noteList.add("Note2");
        noteList.add("Note3");
        noteList.add("Note4");
        noteList.add("Note5");
        noteList.add("Note6");
        intent.putExtra("noteList", noteList);
        context.startService(intent);

    }

}
