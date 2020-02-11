package com.soleeklab.nilebot.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;


/**
 * Created by Saif on 1/25/2017.
 */

public class PermissionsUtils {


    public static final String[] IMAGE_PERMISSIONS =
            {Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public static final String[] QR_PERMISSIONS =
            {Manifest.permission.CAMERA};


    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;


    public static final String[] DOWNLOAD_PERMISSIONS =
            {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final String[] WRITE_PERMISSIONS =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public static final int IMAGE_PERMISSION_RESPONSE = 1;
    public static final int DOWNLOAD_PERMISSION_RESPONSE = 2;

    public static boolean isPermissionGranted(Activity activity, String permissionName) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(permissionName)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");
                return true;
            } else {
                Log.v("permission", "Permission is revoked");
                ActivityCompat.requestPermissions(activity,
                        new String[]{permissionName}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted");
            return true;
        }
    }

    /**
     * @return whether all the required permission for taking and picking an image are granted or not
     */
    public static boolean areImagePermissionsGranted(Context context) {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        for (String permission : IMAGE_PERMISSIONS)
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }

    public static boolean isAllPermissionGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static boolean requestPermission(String permission, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        2);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
