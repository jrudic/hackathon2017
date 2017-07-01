package com.projectx.jovanrudic.mhydrabanking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.projectx.jovanrudic.mhydrabanking.api.ServiceApi;
import com.projectx.jovanrudic.mhydrabanking.location.LocationUtl;
import com.projectx.jovanrudic.mhydrabanking.model.LocationModel;
import com.projectx.jovanrudic.mhydrabanking.model.ResponseMessage;

/**
 * Created by jovanrudic on 7/2/17.
 */

public class HydraService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                LocationModel lm = LocationUtl.getUserLocation(context);
                sendLocationData(lm);
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onDestroy() {
        Log.i("JR: service - ", "Service stopped");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private void sendLocationData(LocationModel lm) {
        ServiceApi.sendLocationData(lm.getLat(), lm.getLong(), lm.getAddressailable(), lm.getCity
                (), lm.getState(), lm.getCountry(), lm.getPostalCode(), lm.getKnownName(), lm
                .getTimeStamp(), new ServiceApi.Listener<ResponseMessage>() {
            @Override
            public void onSuccess(ResponseMessage response) {
                if (response.getError() == 0){
                    Log.i("JR: service - ", "Service uploaded with success.");
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.i("JR: service - ", "Service try to upload with no success.");
            }
        });
    }
}
