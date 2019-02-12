package com.iti.android.tripapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ayman on 2019-02-12.
 */

public class ViewPageAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;

    private Context context;
       private int[] layouts;

    public ViewPageAdapter(Context context,int[] layouts) {
        this.layoutInflater = layoutInflater;
        this.context=context;
        this.layouts=layouts;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layouts[position], container, false);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject( View view,  Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,Object object) {
        View view=(View)object;
        container.removeView(view);
    }
}


