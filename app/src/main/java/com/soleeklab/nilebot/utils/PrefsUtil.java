package com.soleeklab.nilebot.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.soleeklab.nilebot.NilebotApplication;

public class PrefsUtil {


    private static final String DEFAULT_APP_PREFS_NAME = "yamam-default-prefs";

    private static SharedPreferences getPrefs() {

        return NilebotApplication.get().getSharedPreferences(DEFAULT_APP_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveString(String key, String value) {
        getPrefs().edit().putString(key, value).apply();
    }
    public static void saveInt(String key, int value) {
        getPrefs().edit().putInt(key, value).apply();
    }
    public static int getInt(String key) {
        return getPrefs().getInt(key,0);
    }

    public static String getString(String key) {
        return getPrefs().getString(key, null);
    }


    public static void saveBoolean(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return getPrefs().getBoolean(key, false);
    }


    }