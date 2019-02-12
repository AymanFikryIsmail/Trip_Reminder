package com.iti.android.tripapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ayman on 2019-02-08.
 */

public class SharedPref {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences constants
    private static final String PREF_NAME = "MyPreference";
    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //set the first time to use the app
    public void setFirst(Boolean isFirst)
    {
        editor.putBoolean("isFirst",isFirst);
        editor.commit();
    }

    //check for the first time to use the app
    public boolean getIsFirst()
    {
        return pref.getBoolean("isFirst",true);
    }

}
