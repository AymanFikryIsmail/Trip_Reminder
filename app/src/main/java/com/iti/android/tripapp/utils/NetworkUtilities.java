package com.iti.android.tripapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


/**
 * Created by ayman on 2018-01-15.
 */

public final class NetworkUtilities {

    private static String baseUrl="";

    public static void networkConnectionFailure(Context context){
        if (context!= null){
//            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Network failure ", Toast.LENGTH_SHORT).show();
        }
    }

    //check if mobile is connected to network or not
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void onResponseFailure(Context context,String errorMsg){
        if (context!= null){
//            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getBaseUrl() {

        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        NetworkUtilities.baseUrl = baseUrl;
    }
}
