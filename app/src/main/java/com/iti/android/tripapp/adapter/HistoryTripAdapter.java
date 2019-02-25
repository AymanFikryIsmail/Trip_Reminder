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
import com.iti.android.tripapp.services.FloatingIconService;
import com.iti.android.tripapp.services.alarm.AlarmHelper;
import com.iti.android.tripapp.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

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

//                    .diskCacheStrategy(DiskCacheStrategy.ALL)//"https://aymanfikryeng.000webhostapp.com/images/Resturants/La-Casona-logo-for-a-restaurant.jpg")//
//                   .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_home_black_24dp)).into(associationImage);// .apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_home_black_24dp))
//            Picasso.with(context).load("")
//                    .fit().centerCrop()
//                    .placeholder(R.drawable.ic_home_black_24dp)
//                    .into(associationImage);
            gridCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.show_notes , null, false);
                    rvShowNotes = (RecyclerView) dialogView.findViewById(R.id.showNotes);
                    TextView  trip_name =  dialogView.findViewById(R.id.trip_name);
                    trip_name.setText(tripDTO.getName());
                    rvShowNotes.setLayoutManager(new LinearLayoutManager(context));
                    adapter= new ShowDetailsAdapter(tripDTO.getNotes().getNotes());
                    rvShowNotes.setAdapter(adapter);
                    //}
                    AlertDialog.Builder build = new AlertDialog.Builder(context);
                    build.setView(dialogView);

                    final AlertDialog alertDialog = build.create();
                    alertDialog.show();
                }
            });
            popupMenuTxt.setVisibility(View.GONE);

        }
    }
}
