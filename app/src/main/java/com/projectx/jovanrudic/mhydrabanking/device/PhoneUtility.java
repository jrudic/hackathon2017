package com.projectx.jovanrudic.mhydrabanking.device;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.projectx.jovanrudic.mhydrabanking.model.PhoneModel;

/**
 * Created by jovanrudic on 6/30/17.
 */

public class PhoneUtility {

    public static PhoneModel getPhoneData(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);

        String tempImei = telephonyManager.getDeviceId();
        String imei =  tempImei == null ? "" : tempImei;

        String tempPhoneNumber = telephonyManager.getLine1Number();
        String phoneNumber =  tempPhoneNumber == null ? "" : tempPhoneNumber;

        String os = Build.VERSION.RELEASE;
        String deviceModel = Build.MODEL;
        return new PhoneModel(imei, phoneNumber, deviceModel, os);
    }
}
