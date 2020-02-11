package com.soleeklab.nilebot.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class LocalHelper {

    public static final String LANGUAGE_ARABIC = "ar";
    public static final String LANGUAGE_ENGLISH = "en";
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static final String PREFERENCE_NAME = "Preference";
    private static LocalHelper instance;
    private static SharedPreferences sharedPreferences;
    private Context context;

    private LocalHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        this.context = context;
    }


    public static LocalHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LocalHelper(context);
            return instance;
        } else
            return instance;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("NewApi")
    public static void ChangeDesignToRTL(Activity activity) {

        String Lang = sharedPreferences.getString(SELECTED_LANGUAGE, LANGUAGE_ENGLISH);
        // This code change the design to support arabic lang.
        if (Lang.equals(LANGUAGE_ARABIC)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR)
                    activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("NewApi")
    public static void ChangeDesignToLTR(Activity activity) {

        String Lang = sharedPreferences.getString(SELECTED_LANGUAGE, LANGUAGE_ENGLISH);
        // This code change the design to support arabic lang.
        if (!Lang.equals(LANGUAGE_ARABIC)) {
            // This code change the design to support arabic lang.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                if (activity.getWindow().getDecorView().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
                    activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    public static String getLanguage() {
        return sharedPreferences.getString(SELECTED_LANGUAGE, Locale.getDefault().getLanguage());
    }

    public void setLanguage(String language) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
        updateResources(language);

    }

    private void updateResources(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(Locale.getDefault());
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }

}
