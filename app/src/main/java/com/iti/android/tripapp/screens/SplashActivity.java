package com.iti.android.tripapp.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.iti.android.tripapp.R;

public class SplashActivity extends AppCompatActivity implements AnimationListener {

    private ImageView boosters;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        animation.setAnimationListener(this);

        boosters = findViewById(R.id.app_anim_boosters);
        boosters.startAnimation(animation);

        new LoadingTask(this).execute();
    }


    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        animation.setAnimationListener(this);
        boosters.startAnimation(animation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    class LoadingTask extends AsyncTask<Void, Void, Void> {

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
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("isFirstTime", true))
                activity.startActivity(new Intent(SplashActivity.this, WalkThroughActivity.class));
            else
                activity.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }
    }
}
