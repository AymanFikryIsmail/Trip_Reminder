package com.iti.android.tripapp.ui.main_mvp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.adapter.UpComingTripAdapter;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.utils.PrefManager;

import java.util.List;

public class UpComingFragment extends Fragment {

    public UpComingFragment() {
        // Required empty public constructor
    }
    private RecyclerView upComingTripRV;
    UpComingTripAdapter upComingTripAdapter;
    ProgressBar progressBar;
    List<TripDTO> tripDTOArrayList;

    TextView emptyText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    PrefManager prefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_up_coming, container, false);
        prefManager=new PrefManager(getContext());
        upComingTripRV = view.findViewById(R.id.upComingTripRV);
        emptyText = view.findViewById(R.id.emptyText);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        upComingTripRV.setLayoutManager(mLayoutManager);
        tripDTOArrayList= MyAppDB.getAppDatabase(getContext()).tripDao().getAllTrips("waited", prefManager.getUserId());
        if (tripDTOArrayList.size()!=0)
            emptyText.setVisibility(View.GONE);

        upComingTripAdapter = new UpComingTripAdapter(getContext(), tripDTOArrayList);
        upComingTripRV.setAdapter(upComingTripAdapter);
        return  view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
