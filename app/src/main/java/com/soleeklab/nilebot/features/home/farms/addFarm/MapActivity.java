package com.soleeklab.nilebot.features.home.farms.addFarm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.soleeklab.nilebot.BaseActivity;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.data.Constants;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.utils.LocationUtil;
import com.soleeklab.nilebot.utils.PermissionsUtils;
import com.soleeklab.nilebot.utils.listeners.GetCurrentLocationCallback;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    @BindView(R.id.fab)
    FloatingActionButton location;
    public static String LAT = "pet_lat";
    public static String LNG = "pet_lng";
    public static final int RC_PERMISSIONS = 111;
    @BindView(R.id.tv_toolbar_tittle)
    TextView tvToolbarTittle;

    @Inject
    MyCache myCache;

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.txt_map_location)
    TextView txtMapLocation;

    @BindString(R.string.google_maps_key_api)
    String apiKey;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private LocationUtil locationUtil;
    List<Address> addresses;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_map);
        ButterKnife.bind(this);
        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(this);


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        ImageView searchIcon = (ImageView) ((LinearLayout) autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_white));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("Place: ", "Place: " + place.getName() + ", " + place.getId());
                if (place.getLatLng() != null) {
                    moveCamera(place.getLatLng().latitude, place.getLatLng().longitude, true);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("ERRORPLACE", status.getStatusMessage() + " --- " + status.toString());
            }
        });
        geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    protected View getView() {
        return null;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO get location and pet commerce
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        moveCamera(Constants.EGYPT_TAHRIR_LAT1, Constants.EGYPT_TAHRIR_LNG1, true);


        if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {

            if (getIntent().getDoubleExtra("lat", Constants.EGYPT_TAHRIR_LAT1) != 0)
                moveCamera(getIntent().getDoubleExtra("lat", Constants.EGYPT_TAHRIR_LAT1),
                        getIntent().getDoubleExtra("lng", Constants.EGYPT_TAHRIR_LAT1), true);

            else {
                if (hasMapsPermission()) {
//            location.setVisibility(View.VISIBLE);
                    getMyLocation();

                } else {
                    requestMapsPermission();
                }
            }

        } else {
            if (hasMapsPermission()) {
                mMap.setMyLocationEnabled(true);

//            location.setVisibility(View.VISIBLE);
                getMyLocation();

            } else {
                requestMapsPermission();
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {

                        CameraPosition position = mMap.getCameraPosition();

                        try {
                            addresses = geocoder.getFromLocation(position.target.latitude, position.target.longitude, 1);
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String country = addresses.get(0).getCountryName();
                            txtMapLocation.setVisibility(View.VISIBLE);
                            txtMapLocation.setText(address.replaceAll("null", " "));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CameraPosition position = mMap.getCameraPosition();
                try {
                    addresses = geocoder.getFromLocation(position.target.latitude, position.target.longitude, 1);
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String country = addresses.get(0).getCountryName();
                    txtMapLocation.setVisibility(View.VISIBLE);
                    txtMapLocation.setText(address.replaceAll("null", " "));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);


    }


    @OnClick({R.id.fab, R.id.btn_add_location, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                hideInputType();
                getMyLocation();
                break;
            case R.id.btn_add_location:
                if (mMap != null) {
                    LatLng latLng = mMap.getCameraPosition().target;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(LAT, latLng.latitude);
                    resultIntent.putExtra(LNG, latLng.longitude);

                    myCache.setLat(latLng.latitude);
                    myCache.setLng(latLng.longitude);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                break;
            case R.id.btn_back:

                onBackPressed();
                break;

        }

    }


    private void getMyLocation() {
        if (!PermissionsUtils.hasPermissions(this, permissions)) {
            requestMapsPermission();
        } else {
            locationUtil = new LocationUtil(this);
            locationUtil.getLocation(new GetCurrentLocationCallback() {
                @Override
                public void getCurrentLocationSuccess(double lat, double lng) {
                    moveCamera(lat, lng, true);

                }

                @Override
                public void askForPermission() {
                    moveCamera(Constants.EGYPT_TAHRIR_LAT1, Constants.EGYPT_TAHRIR_LNG1, true);
                }

                @Override
                public void askForGPS(Status status) {
                    try {
                        status.startResolutionForResult(
                                MapActivity.this, 1000);
                    } catch (IntentSender.SendIntentException ignored) {

                    }
                }
            });
        }
    }

    private void moveCamera(double lat, double lng, boolean animate) {
        mMap.clear();
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newLatLngZoom(new LatLng(lat, lng), Constants.MAPS_DEFAULT_ZOOM);
        if (animate)
            mMap.animateCamera(cameraUpdate);
        else
            mMap.moveCamera(cameraUpdate);
    }

    private void requestMapsPermission() {
        ActivityCompat.requestPermissions(this, permissions, RC_PERMISSIONS);
    }

    private boolean hasMapsPermission() {
        return PermissionsUtils.hasPermissions(this, permissions);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // TODO - check if all permission granted not just one

        if (requestCode == RC_PERMISSIONS) {
            if (hasMapsPermission()) {
                mapFragment.getMapAsync(this);

            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000:
                //Reference: https://developers.google.com/android/reference/com/google/android/gms/location/SettingsApi
                switch (resultCode) {
                    case RESULT_OK:
                        mapFragment.getMapAsync(this);
                        // All required changes were successfully made
                        break;
                    case RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                if (hasMapsPermission()) {
//                    location.setVisibility(View.VISIBLE);
                    getMyLocation();
                }
                break;


        }

    }


    @Override
    public void showToast(String message) {

    }
}

