package com.soleeklab.nilebot.data.repo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ApplicationContextInjector {

    private static Application context;

    public static Context getApplicationContext() {
        if (context == null) {
            throw new IllegalStateException("Trying to getByPosition the context, before setting it");
        }
        return context;
    }

    public static void setApplicationContext(Application context) {
        ApplicationContextInjector.context = context;
    }

    public static SharedPreferences sharedPreferences() {
        return getDefaultSharedPreferences(getApplicationContext());
    }
}