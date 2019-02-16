package com.iti.android.tripapp.screens.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.adapter.HistoryTripAdapter;
import com.iti.android.tripapp.adapter.UpComingTripAdapter;
import com.iti.android.tripapp.model.TripDTO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }
    private RecyclerView historyRV;
    HistoryTripAdapter historyTripAdapter;
    ProgressBar progressBar;
    ArrayList<TripDTO> tripDTOArrayList;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        historyRV = view.findViewById(R.id.historyRV);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        historyRV.setLayoutManager(mLayoutManager);
        historyTripAdapter = new HistoryTripAdapter(getContext(), tripDTOArrayList);
        tripDTOArrayList=null;
        //getTripDTOArrayList();

        return  view;

    }

}
