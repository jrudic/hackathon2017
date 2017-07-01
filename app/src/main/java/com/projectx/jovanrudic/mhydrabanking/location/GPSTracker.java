package com.projectx.jovanrudic.mhydrabanking.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

/**
 * Created by jovanrudic on 7/1/17.
 */

public class GPSTracker implements LocationListener {
    public interface ILocationCallback {
        void onLocationChanged();
    }

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meter

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 500; // 0.5 sec

    private final Context mContext;
    private boolean mCanGetLocation;
    private double mLatitude;
    private double mLongitude;

    private ILocationCallback locationCallback;
    private LocationManager mLocationManager;

    public GPSTracker(Context context) {
        this.mContext = context;

        getLocation();
    }

    public void setLocationCallback(ILocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    private void getLocation() {
        // location provider
        String provider;

        // flag for GPS status
        boolean isGPSEnabled;

        // flag for network status
        boolean isNetworkEnabled;

        mLocationManager =
                (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled =
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // both providers are available, we will choose better one
        if (isGPSEnabled && isNetworkEnabled) {
            Criteria criteria = new Criteria();
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(false);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            provider = mLocationManager.getBestProvider(criteria, false);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = mLocationManager.getLastKnownLocation(provider);
            mLocationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.getMainLooper());

            if (location != null) {
                onLocationChanged(location);
            }

        } else if (isGPSEnabled) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this, Looper.getMainLooper());
            provider = LocationManager.GPS_PROVIDER;
            Location location = mLocationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if (location != null) {
                onLocationChanged(location);
            }
        } else if (isNetworkEnabled) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this, Looper.getMainLooper());
            provider = LocationManager.NETWORK_PROVIDER;
            Location location = mLocationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if (location != null) {
                onLocationChanged(location);
            }
        } else {
            this.mCanGetLocation = false;
        }
    }

    public void stopUsingGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(GPSTracker.this);
        }
    }

    public boolean canGetLocation() {
        return this.mCanGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            setLocation(location);
        }

        if (locationCallback != null) {
            locationCallback.onLocationChanged();
        }
    }

    private void setLocation(Location location) {
        mCanGetLocation = true;
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        stopUsingGPS();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}

