package com.iti.android.tripapp.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.utils.PrefManager;

public class SplashActivity extends AppCompatActivity implements AnimationListener {

    Animation animBounce;
    ImageView car;
    int index;

    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefManager =new PrefManager(this);
        index = 0;
        car = findViewById(R.id.car);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_up_left);
        animBounce.setAnimationListener(this);
        car.startAnimation(animBounce);
        new LoadingTask(this).execute();
    }


    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
        // check for fade in animation
        switch (index % 4) {
            case 0:
                animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_down_left_straight);
                break;
            case 1:
                animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up_right);
                break;
            case 2:
                animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_down_right_straight);
                break;
            case 3:
                animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up_left);
                break;
        }
        index++;
        animBounce.setAnimationListener(this);
        car.startAnimation(animBounce);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Animation is repeating
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Animation started
    }

    class LoadingTask extends AsyncTask<Void, Integer, Void> {

        Activity activity;

        LoadingTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//                Thread.sleep(4000);
                for (int i=0;i<100; i++){
                    try {
                        Thread.sleep(10);
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (prefManager.getIsFirst() )
                activity.startActivity(new Intent(SplashActivity.this, WalkThroughActivity.class));
            else if(prefManager.getUserId().equals("")){
                activity.startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            } else {
                activity.startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }
    }
}
