package com.iti.android.tripapp.screens;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import com.iti.android.tripapp.screens.fragments.HistoryFragment;
import com.iti.android.tripapp.screens.fragments.UpComingFragment;
import com.iti.android.tripapp.utils.PrefManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    TextView user_name;
    TextView user_email;
    String email;
    View header;
    PrefManager prefManager;
    private static final int REQUEST_CODE = 123;
    private FireBaseHelper fireBaseHelper;
    private int attemptCount = 1;
    private final static int ATTEMPT_LIMIT = 3;


    private Fragment upComingFragment;
    private Fragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar);
         toolbar.setTitle("UpComing Trips");
        setSupportActionBar(toolbar);
        fireBaseHelper=new FireBaseHelper();
        prefManager=new PrefManager(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=" + 31.267048 + "," + 29.994168 + "&daddr=" +31.207751 + "," + 29.911807));
                startActivity(new Intent(MainActivity.this, AddTripActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false)
                        .setIcon(R.drawable.logout_icon)
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


//        UpComingFragment upComingFragment=new UpComingFragment();
//        loadFragment(upComingFragment,"UpComing Trips");
        header = navigationView.getHeaderView(0);
        if (savedInstanceState == null && getSupportFragmentManager().findFragmentByTag(toolbar.getTitle().toString()) == null) {
            upComingFragment=new UpComingFragment();
            loadFragment(upComingFragment,"UpComing Trips");
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
//        UpComingFragment upComingFragment=new UpComingFragment();
//        loadFragment(upComingFragment,"UpComing Trips");
//        upComingFragment =  getSupportFragmentManager().findFragmentByTag("UpComing Trips");
            Fragment fragment ;//=  getSupportFragmentManager().findFragmentByTag(toolbar.getTitle().toString());
            if (toolbar.getTitle().toString().equals("UpComing Trips")){
                fragment=new UpComingFragment();
            }else {
                fragment=new HistoryFragment();
            }
        loadFragment(fragment,toolbar.getTitle().toString());
        user_name = header.findViewById(R.id.profile_name);
      //  user_name.setText(prefManager.getUserData().getName());
        user_email = header.findViewById(R.id.website);
          user_email.setText(prefManager.getUserData().getEmail());

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
//            finish();
//            startActivity(getIntent());
             upComingFragment=new UpComingFragment();
            loadFragment(upComingFragment,"UpComing Trips");
        } else if (id == R.id.nav_history) {
             historyFragment=new HistoryFragment();
            loadFragment(historyFragment,"Trip History");

        } else if (id == R.id.nav_sync) {

            sync();
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

        MyAppDB.getAppDatabase(this).tripDao().deleteByUserId(prefManager.getUserId());
     fireBaseHelper.retrieveUserTripsFromFirebase(prefManager.getUserId(), new FireBaseCallBack() {
            @Override
            public void getTrips(ArrayList<TripDTO> trips) {
                MyAppDB.getAppDatabase(MainActivity.this).tripDao().insertAll(trips);
                UpComingFragment upComingFragment=new UpComingFragment();
                loadFragment(upComingFragment,"UpComing Trips");

            }
        });
    }
}
