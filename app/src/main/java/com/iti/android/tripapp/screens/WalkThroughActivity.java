package com.iti.android.tripapp.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.adapter.ViewPageAdapter;
import com.iti.android.tripapp.utils.PrefManager;

public class WalkThroughActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PrefManager prefManager;
    private TextView[] dots;
    Button btnNext,btnSkip;
    private ViewPageAdapter viewPageAdapter;
    private LinearLayout dotsLayout;

    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager =new PrefManager(this);
        if(!prefManager.getIsFirst())
        {
            launchHomeScreen();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_walk_through);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        layouts=new int[]{R.layout.walk_through1,R.layout.walk_through2,R.layout.walk_through3};

        addBottomDots(0);


        changeStatusColor();

        viewPageAdapter = new ViewPageAdapter(this, layouts);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

    }

    private void addBottomDots(int position)
    {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[position]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[position].setTextColor(colorsActive[position]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);


            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("Start");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("Next");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void changeStatusColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void launchHomeScreen(){
        prefManager.setFirst(false);
        Intent i=new Intent(this,SignInActivity.class);
        startActivity(i);
        finish();
    }
}
