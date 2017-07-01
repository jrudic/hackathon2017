package com.projectx.jovanrudic.mhydrabanking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectx.jovanrudic.mhydrabanking.api.ServiceApi;
import com.projectx.jovanrudic.mhydrabanking.device.PhoneUtility;
import com.projectx.jovanrudic.mhydrabanking.location.GPSTracker;
import com.projectx.jovanrudic.mhydrabanking.location.LocationUtl;
import com.projectx.jovanrudic.mhydrabanking.model.LocationModel;
import com.projectx.jovanrudic.mhydrabanking.model.PhoneModel;
import com.projectx.jovanrudic.mhydrabanking.model.ResponseMessage;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker mGpsTracker;
    private double mLatitude;
    private double mLongitude;
    public static final int DEFAULT_MAP_ZOOM = 13;

    LocationDBHelper mLocationDBHelper;


    private CheckBox mLicenseCheckBox;

    private GPSTracker.ILocationCallback locationCallback = new GPSTracker.ILocationCallback() {
        @Override
        public void onLocationChanged() {
            getMyLocation();
        }
    };

    public static final int MY_PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, HydraService.class));

        mLocationDBHelper = new LocationDBHelper(getApplicationContext(), LocationDBHelper
                .DATABASE_NAME, null, LocationDBHelper.DATABASE_VERSION);
        Button mAddLocationToSecureListButton = (Button) findViewById(R.id.addToSecureLocationButtone);
        mLicenseCheckBox = (CheckBox) findViewById(R.id.checkBox);

        mAddLocationToSecureListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLicenseCheckBox.isChecked()){
                    ServiceApi.sendSafeLocationData(Double.toString(mLatitude), Double
                                    .toString(mLongitude),
                            new ServiceApi.Listener<ResponseMessage>() {
                                @Override
                                public void onSuccess(ResponseMessage response) {
                                    if (response.getError() == 0) {
                                        Log.i("Test", "win2");
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Log.i("Test", "not2");
                                }
                            });
                    Toast.makeText(MainActivity.this, "New secure location is saved!", Toast
                            .LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please read terms and conditions!", Toast
                            .LENGTH_SHORT).show();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGpsTracker = new GPSTracker(this);
        mGpsTracker.setLocationCallback(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                } else {

                    // TODO JR need to be implemented
                }
                return;
            }
        }
    }

    private void initData() {
        PhoneModel pm = PhoneUtility.getPhoneData(this);
        LocationModel lm = LocationUtl.getLocationData(this, mLatitude, mLongitude);

        sendData(lm, pm);
        saveDataToLocalDataBase(lm);
    }

    private void saveDataToLocalDataBase(LocationModel lm) {
        mLocationDBHelper.createRow(lm.getTimeStamp(), lm.getLat(), lm.getLong());
    }

    private void sendData(LocationModel lm, PhoneModel pm) {
        sendLocationData(lm);
        sendPhoneData(pm);
    }

    private void sendPhoneData(PhoneModel pm) {
        ServiceApi.sendPhoneData(pm.getImei(), pm.getPhoneNumber(), pm.getPhoneNumber(), pm.getOs(),
                new ServiceApi.Listener<ResponseMessage>() {
                    @Override
                    public void onSuccess(ResponseMessage response) {
                        if (response.getError() == 0) {
                            Log.i("Test", "win2");
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.i("Test", "not2");
                    }
                });
    }

    private void sendLocationData(LocationModel lm) {
        ServiceApi.sendLocationData(lm.getLat(), lm.getLong(), lm.getAddressailable(), lm.getCity
                (), lm.getState(), lm.getCountry(), lm.getPostalCode(), lm.getKnownName(), lm
                .getTimeStamp(), new ServiceApi.Listener<ResponseMessage>() {
            @Override
            public void onSuccess(ResponseMessage response) {
                if (response.getError() == 0){
                    Log.i("Test", "win");
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.i("Test", "not");
            }
        });
    }


    @Override
    public void onMapReady(final GoogleMap map) {
        MapsInitializer.initialize(this);

        mMap = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
            getMyLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                mLatitude = latLng.latitude;
                mLongitude = latLng.longitude;
            }
        });

        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGpsTracker.stopUsingGPS();
    }

    private void getMyLocation() {
        if (mGpsTracker.canGetLocation()) {
            mLatitude = mGpsTracker.getLatitude();
            mLongitude = mGpsTracker.getLongitude();
        }

        CameraUpdate cameraUpdate = null;

        try {
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude),
                    DEFAULT_MAP_ZOOM);
        } catch (NullPointerException e) {
            //Shit happens YOLO Hackathon
        }

        if (cameraUpdate != null) {
            mMap.moveCamera(cameraUpdate);
        }
    }
}
