package com.projectx.jovanrudic.mhydrabanking;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by jovanrudic on 6/30/17.
 */

public class PhoneUtility {

    public static PhoneModel getPhoneData(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);

        String imei =  telephonyManager.getDeviceId();
        String phoneNumber = telephonyManager.getLine1Number();
        String os = Build.VERSION.RELEASE;
        String deviceModel = Build.MODEL;
        return new PhoneModel(imei, phoneNumber, deviceModel, os);
    }
}
