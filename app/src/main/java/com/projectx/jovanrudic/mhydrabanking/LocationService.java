package com.projectx.jovanrudic.mhydrabanking;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.projectx.jovanrudic.mhydrabanking.location.GPSTracker;

/**
 * Created by jovanrudic on 7/1/17.
 */

public class LocationService extends Service {

    private IBinder mBinder;

    private GPSTracker.ILocationCallback locationCallback = new GPSTracker.ILocationCallback() {
        @Override
        public void onLocationChanged() {
          //TODO Send data to internet
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new LocationServiceBinder();
    }

    /**
     * SipService binder, the SipService API for the rest of the app.
     */
    private class LocationServiceBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }
}
