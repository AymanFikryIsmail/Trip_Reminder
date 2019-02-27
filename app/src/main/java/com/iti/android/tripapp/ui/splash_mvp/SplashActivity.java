package com.iti.android.tripapp.ui.splash_mvp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.ui.main_mvp.MainActivity;
import com.iti.android.tripapp.ui.WalkThroughActivity;
import com.iti.android.tripapp.ui.login_mvp.SignInActivity;
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
                R.anim.move);
        animBounce.setAnimationListener(this);
        car.startAnimation(animBounce);
    }


    @Override
    public void onAnimationEnd(Animation animation) {
        if (prefManager.getIsFirst() )
            startActivity(new Intent(SplashActivity.this, WalkThroughActivity.class));
        else if(prefManager.getUserId().equals("")){
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);

        }
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // Animation is repeating
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // Animation started
    }

}
