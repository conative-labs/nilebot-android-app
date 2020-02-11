package com.soleeklab.nilebot.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.soleeklab.nilebot.R;


/**
 * Created by omaraboulfotoh on 9/26/17.
 */

public class GeneralUtils {

    public static boolean checkPermission(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
//        checkNotNull(fragmentManager);
//        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();

    }


    public static void replaceFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }


    public static void navigateToFragment(Activity context, Fragment fragment, String tag, boolean isStack,
                                          boolean isSlideAnimation,
                                          boolean existBefore) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

        if (!existBefore) {
            //if (!currentFragment.equals(tag)) {
            ft.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);


            if (isStack) {
                ft.addToBackStack(tag);
                ft.add(R.id.frame_home, fragment, tag);
            } else ft.replace(R.id.frame_home, fragment, tag);
            ft.commit();
        }
    }

    public static void navigateToFilterFragment(Activity context, Fragment fragment, String tag, boolean isStack,
                                                boolean isSlideAnimation,
                                                boolean existBefore) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

        if (!existBefore) {
            //if (!currentFragment.equals(tag)) {

            ft.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);

            if (isStack) {
                ft.addToBackStack(tag);
                ft.add(R.id.frame_filter, fragment, tag);
            } else ft.replace(R.id.frame_filter, fragment, tag);
            ft.commit();
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static void slide_down(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

}
