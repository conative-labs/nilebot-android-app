package com.soleeklab.nilebot.utils;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.soleeklab.nilebot.R;

public class FragmentNavigationHelper {


    static String currentFragment = "";


    public static void navigateToFragment(Activity context, Fragment fragment, String tag, boolean isStack,
                                          boolean isSlideAnimation,
                                          boolean existBefore) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

        if (!existBefore) {
            //if (!currentFragment.equals(tag)) {
            if (!isSlideAnimation)
                ft.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);
            else {
                ft.setCustomAnimations(R.anim.left_enter, R.anim.right_out);
            }

            if (isStack) {
                ft.addToBackStack(tag);
                ft.add(R.id.fragment_container, fragment, tag);
            } else ft.replace(R.id.fragment_container, fragment, tag);
            ft.commit();
        }
        //}

    }

}
