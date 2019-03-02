package com.iti.android.tripapp.ui.main_mvp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.android.tripapp.R;
import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.helpers.local.FireBaseCallBack;
import com.iti.android.tripapp.helpers.local.database.MyAppDB;
import com.iti.android.tripapp.model.TripDTO;
import com.iti.android.tripapp.model.UserDTO;
import com.iti.android.tripapp.ui.add_trip_mvp.AddTripActivity;
import com.iti.android.tripapp.ui.login_mvp.SignInActivity;
import com.iti.android.tripapp.ui.main_mvp.fragment.HistoryFragment;
import com.iti.android.tripapp.ui.main_mvp.fragment.UpComingFragment;
import com.iti.android.tripapp.utils.NetworkUtilities;
import com.iti.android.tripapp.utils.PrefManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    TextView user_name;
    TextView user_email;
    String email;
    NavigationView navigationView;
    View header;
    PrefManager prefManager;
    private static final int REQUEST_CODE = 123;
    private FireBaseHelper fireBaseHelper;
    private int attemptCount = 1;
    private final static int ATTEMPT_LIMIT = 3;


    private Fragment upComingFragment;
    private Fragment historyFragment;
    private AdView mAdView;
    private ProgressBar progressBar;

    private  CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress);

        MobileAds.initialize(this, "ca-app-pub-3894715747861785~1513826605");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the ic_user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the ic_user is about to return
                // to the app after tapping on an ad.
            }
        });


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Upcoming Trips");
        prefManager=new PrefManager(this);

        setSupportActionBar(toolbar);
        fireBaseHelper=new FireBaseHelper();
        prefManager=new PrefManager(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddTripActivity.class));
            }
        });
         coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false)
                        .setIcon(R.drawable.ic_logo)
                        .setTitle(R.string.permissions_dialog_title)
                        .setMessage(R.string.permissions_dialog_msg)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
        }
        header = navigationView.getHeaderView(0);

        if (savedInstanceState == null && getSupportFragmentManager().findFragmentByTag(toolbar.getTitle().toString()) == null) {
            upComingFragment=new UpComingFragment();
            loadFragment(upComingFragment,"Upcoming Trips");
        }
    }

    private void loadFragment(Fragment fragment, String barTitle){
        toolbar.setTitle(barTitle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container,fragment,barTitle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
            Fragment fragment ;//=  getSupportFragmentManager().findFragmentByTag(toolbar.getTitle().toString());
            if (toolbar.getTitle().toString().equals("Upcoming Trips")){
                fragment=new UpComingFragment();
            }else {
                fragment=new HistoryFragment();
            }
        loadFragment(fragment,toolbar.getTitle().toString());
        UserDTO user = prefManager.getUserData();
        Log.i("TESTING", user.getEmail() + " " + user.getName());
        user_name = header.findViewById(R.id.profile_name);
        user_name.setText(user.getName()!=null?user.getName():"");
        user_email = header.findViewById(R.id.email);
        user_email.setText(user.getEmail());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragment",1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String toastMsg = "Attempt " + attemptCount + " out of " + ATTEMPT_LIMIT + " has failed\n Retrying...";
        if (!Settings.canDrawOverlays(this)) {
            if (attemptCount != ATTEMPT_LIMIT) {
                attemptCount++;
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_CODE);
            } else {
                toastMsg = "Attempt " + attemptCount + " out of " + ATTEMPT_LIMIT + " has failed\n Terminating...";
                finish();
            }
            issueToast(toastMsg);
        }
    }

    private void issueToast(String toastMsg) {
        Toast toast = Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
             upComingFragment=new UpComingFragment();
            loadFragment(upComingFragment,"Upcoming Trips");
        } else if (id == R.id.nav_history) {
             historyFragment=new HistoryFragment();
            loadFragment(historyFragment,"Trip History");
        } else if (id == R.id.nav_sync) {
            item.setChecked(false);
            if (NetworkUtilities.isOnline(this)) {
                sync();
            }else {
                Snackbar snackbar= Snackbar.make(coordinatorLayout,"Please, Connect to Internet",Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryDark));
                snackbar.show();
            }
        } else if (id == R.id.nav_logout) {
            prefManager.setUserId("");
            FirebaseAuth.getInstance().signOut();
             GoogleSignInClient mGoogleSignInClient = null;
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            mGoogleSignInClient.revokeAccess()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            Intent intent=new Intent(this,SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sync(){
        showProgress();
        MyAppDB.getAppDatabase(this).tripDao().deleteByUserId(prefManager.getUserId());
        fireBaseHelper.retrieveUserTripsFromFirebase(prefManager.getUserId(), new FireBaseCallBack() {
            @Override
            public void getTrips(ArrayList<TripDTO> trips) {
                MyAppDB.getAppDatabase(MainActivity.this).tripDao().insertAll(trips);
                Fragment fragment ;//=  getSupportFragmentManager().findFragmentByTag(toolbar.getTitle().toString());
                if (toolbar.getTitle().toString().equals("Upcoming Trips")){
                    fragment=new UpComingFragment();
                    navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                } else {
                    fragment=new HistoryFragment();
                    navigationView.getMenu().findItem(R.id.nav_history).setChecked(true);
                }
                hideProgress();
                loadFragment(fragment,toolbar.getTitle().toString());
            }
        });
    }
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}
