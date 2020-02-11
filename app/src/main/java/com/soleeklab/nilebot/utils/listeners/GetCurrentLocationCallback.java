package com.soleeklab.nilebot.utils.listeners;

import com.google.android.gms.common.api.Status;

public interface GetCurrentLocationCallback {
    void getCurrentLocationSuccess(double lat, double lng);

    void askForPermission();

    void askForGPS(Status status);

}