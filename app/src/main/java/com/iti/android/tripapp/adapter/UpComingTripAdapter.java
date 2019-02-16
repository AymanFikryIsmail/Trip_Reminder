package com.iti.android.tripapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.model.TripDTO;
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
    public UpComingTripAdapter(Context context,List<TripDTO> tripDTOList){
        this.context = context;
        this.associationsTitle = tripDTOList;
        prefManager=new PrefManager(context);

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

        public TextView cellTv , address_txt, membership,pending_txt;
        public ImageView associationImage  ;
        public CardView gridCardView;

        public MyViewHolder(View itemView) {
            super(itemView);
//            cellTv = itemView.findViewById(R.id.cellTvCustomGridId);
//            associationImage = itemView.findViewById(R.id.associationImageId);
//            membership = itemView.findViewById(R.id.membership_id);
//            address_txt = itemView.findViewById(R.id.address_id);
//            gridCardView = itemView.findViewById(R.id.cardViewCustomGridId);
//            pending_txt = itemView.findViewById(R.id.pending_txt);



        }

        public void bind(final TripDTO tripDTO){

//            cellTv.setText("");
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
                pending_txt.setVisibility(View.GONE);

            }

    }

}
