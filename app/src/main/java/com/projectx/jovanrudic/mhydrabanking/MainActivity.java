package com.projectx.jovanrudic.mhydrabanking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectx.jovanrudic.mhydrabanking.device.PhoneUtility;
import com.projectx.jovanrudic.mhydrabanking.location.GPSTracker;
import com.projectx.jovanrudic.mhydrabanking.location.LocationUtl;
import com.projectx.jovanrudic.mhydrabanking.model.LocationModel;
import com.projectx.jovanrudic.mhydrabanking.model.PhoneModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker mGpsTracker;
    private double mLatitude;
    private double mLongitude;
    public static final int DEFAULT_MAP_ZOOM = 13;

    LocationDBHelper mLocationDBHelper;


    private Button mAddLocationToSecureListButton;
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

      //  mLocationDBHelper = LocationDBHelper.getLocationDataBaseHelper(MainActivity.this);
        mAddLocationToSecureListButton = (Button) findViewById(R.id.addToSecureLocationButtone);
        mLicenseCheckBox = (CheckBox) findViewById(R.id.checkBox);

        mAddLocationToSecureListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLicenseCheckBox.isChecked()){
                    //TODO JR Send new location on server
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
        LocationModel lm = getLocationData();

        sendData();
       // saveDataToLocalDataBase(lm);
    }

    private LocationModel getLocationData(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String address = "";
        String city = "";
        String state = "";
        String country = "";
        String postalCode = "";
        String knownName = "";

        try {
            addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = LocationUtl.getCompleteAddressString(this, mLatitude, mLongitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // result to returned, by documents it recommended 1 to 5

        return new LocationModel(Double.toString(mLatitude), Double.toString
                (mLongitude), getTimeStamp(), address, city, state, country, postalCode, knownName);
    }

    private void saveDataToLocalDataBase(LocationModel lm) {
        mLocationDBHelper.createRow(lm.getTimeStamp(), lm.getLat(), lm.getLong());
    }

    private void sendData() {
        //TODO send data to internet :)
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

    private String getTimeStamp(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        return dateformat.format(c.getTime());
    }
}
