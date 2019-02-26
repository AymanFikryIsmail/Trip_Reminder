package com.iti.android.tripapp.ui.main_mvp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.adapter.HistoryTripAdapter;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.utils.PrefManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryListFragment extends Fragment {


    public HistoryListFragment() {
        // Required empty public constructor
    }

    private RecyclerView historyRV;
    HistoryTripAdapter historyTripAdapter;
    List<TripDTO> tripDTOArrayList;
    PrefManager prefManager;
    TextView emptyText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history_list, container, false);
        prefManager=new PrefManager(getContext());
        historyRV = view.findViewById(R.id.historyRV);
        emptyText = view.findViewById(R.id.emptyText);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        historyRV.setLayoutManager(mLayoutManager);
        tripDTOArrayList=null;
        tripDTOArrayList= MyAppDB.getAppDatabase(getContext()).tripDao().getAllTrips("started", prefManager.getUserId());
        if (tripDTOArrayList.size()!=0)
            emptyText.setVisibility(View.GONE);

        historyTripAdapter = new HistoryTripAdapter(getContext(), tripDTOArrayList);
        historyRV.setAdapter(historyTripAdapter);

        return  view;
    }

}
