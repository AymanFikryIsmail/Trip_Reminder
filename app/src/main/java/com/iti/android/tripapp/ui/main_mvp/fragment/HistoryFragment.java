package com.iti.android.tripapp.ui.main_mvp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.iti.android.tripapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        BottomNavigationView navigation = view.findViewById(R.id.historytabs);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        HistoryListFragment homeFragment = new HistoryListFragment();
        loadFragment(homeFragment);
        return  view;

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.history_tab:
                    HistoryListFragment homeFragment = new HistoryListFragment();
                    loadFragment(homeFragment);
                    return true;
                case R.id.map_tab:
                    MapRoutesFragment mapRoutesFragment  = new MapRoutesFragment();
                        loadFragment(mapRoutesFragment);
                    return true;
            }
            return false;
        }
    };
    private void  loadFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction=getChildFragmentManager().beginTransaction();
        // fragmentTransaction .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
