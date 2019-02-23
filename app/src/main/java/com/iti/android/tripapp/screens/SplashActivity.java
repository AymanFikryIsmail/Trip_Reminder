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

    private ImageView boosters;
    private Animation animation;
    ProgressBar pBar;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefManager =new PrefManager(this);
         pBar = findViewById(R.id.progressBar);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_up);
        animation.setAnimationListener(this);

        boosters = findViewById(R.id.app_logo);
        boosters.startAnimation(animation);
        pBar.setProgress(0);
        pBar.setMax(100);
       new LoadingTask(this).execute();
    }


    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
//        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
//        animation.setAnimationListener(this);
//        boosters.startAnimation(animation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    class LoadingTask extends AsyncTask<Void, Integer, Void> {

        Activity activity;

        LoadingTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar pBar = activity.findViewById(R.id.progressBar);
            pBar.setIndeterminate(true);
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
            pBar.animate().alpha(0).setDuration(400).start();

            if ( prefManager.getIsFirst() )
                activity.startActivity(new Intent(SplashActivity.this, WalkThroughActivity.class));
            else if(prefManager.getUserId().equals("")){
                activity.startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            } else {
                activity.startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pBar.setProgress(values[0]);

        }
    }
}
