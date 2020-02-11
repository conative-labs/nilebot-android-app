package com.soleeklab.nilebot.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.soleeklab.nilebot.NilebotApplication;


public class NetworkUtil {


    private static ConnectivityManager connectivityManager() {
        return (ConnectivityManager) NilebotApplication.get().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) NilebotApplication.get().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

}
