package com.soleeklab.nilebot.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.soleeklab.nilebot.data.repo.ApplicationContextInjector;
import com.soleeklab.nilebot.utils.listeners.GetCurrentLocationCallback;

/**
 * Created by omar on 03/03/17.
 */

public class LocationUtil {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1099;

    public static final int GPS_SETTINGS_REQUEST_CODE = 1055;

    private Context context;
    private LocationManager locationManager;
    double longitude = 0;
    double latitude = 0;
    private FusedLocationProviderClient mFusedLocationClient;

    // to invoke a function when location detected

    public LocationUtil(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean checkLocationManagerNetworkProviderIsEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getLocation(final GetCurrentLocationCallback locationCallback) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ApplicationContextInjector.getApplicationContext());

        boolean isNetworkEnabled = checkLocationManagerNetworkProviderIsEnabled();

        if (!isNetworkEnabled) {
            Log.e("location", "Network not enabled");
            buildAlertMessageNoGps(locationCallback);
            //homePresenter.enableLangBtn();
        } else {
            Log.e("location", "Network Enabled");

            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: 20/05/17  requist permission from user
                locationCallback.askForPermission();


            } else {

                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            locationCallback.getCurrentLocationSuccess(latitude, longitude);
                        } else {
                            Log.e("location", "NULL");
                        }
                    }
                });


//                        // TODO: 20/05/17 update user with new location

            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void trackLocation(final GetCurrentLocationCallback locationCallback) {
        boolean isNetworkEnabled = checkLocationManagerNetworkProviderIsEnabled();

        Log.e("location", "getLocation called");

        if (!isNetworkEnabled) {
            Log.e("location", "Network not enabled");

            buildAlertMessageNoGps(locationCallback);
            //homePresenter.enableLangBtn();
        } else {
            Log.e("location", "Network Enabled");

            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: 20/05/17  requist permission from user

                locationCallback.askForPermission();


            } else {


                LocationRequest request = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(15000);

                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            locationCallback.getCurrentLocationSuccess(latitude, longitude);
                        } else {
                            Log.e("location", "NULL");
                        }
                    }
                });


            }
        }
    }


    private void buildAlertMessageNoGps(final GetCurrentLocationCallback getCurrentLocationCallback) {
// TODO: 20/05/17  request location dilag
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        getCurrentLocationCallback.askForGPS(status);
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });

    }
}
