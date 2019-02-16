package com.iti.android.tripapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.iti.android.tripapp.model.UserDTO;

/**
 * Created by ayman on 2019-02-08.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences constants
    private static final String PREF_NAME = "MyPreference";
    public PrefManager(Context context) {
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

    public String getUserId() {
        return pref.getString("userId","");
    }

    public void setUserId(String userId) {
        editor.putString("userId", userId);
        editor.commit();
    }



    public UserDTO getUserData() {
        Gson gson = new Gson();
        String json = pref.getString("UserDTO", "");
        UserDTO registeration = gson.fromJson(json, UserDTO.class);
        return registeration;

    }

    public void setUserData(UserDTO registeration) {
        Gson gson = new Gson();
        String json = gson.toJson(registeration);
        editor.putString("UserDTO", json);
        editor.commit();
        editor.commit();
    }


}
