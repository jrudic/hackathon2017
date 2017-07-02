package com.projectx.jovanrudic.mhydrabanking.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.projectx.jovanrudic.mhydrabanking.model.LocationModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by jovanrudic on 7/1/17.
 */

public class LocationUtl {

    public static String getCompleteAddressString(Context context, double lat, double lng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public static LocationModel getLocationData(Context context, Double lat, Double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        String address = "";
        String city = "";
        String state = "";
        String country = "";
        String postalCode = "";
        String knownName = "";

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

            String tempAddress = addresses.get(0).getAddressLine(0);
            address = tempAddress == null ? "" : tempAddress;

            String tempCity = addresses.get(0).getLocality();
            city = tempCity == null ? "" : tempCity;

            String tempState = addresses.get(0).getAdminArea();
            state = tempState == null ? "" : tempState;

            String tempCountry = addresses.get(0).getCountryName();
            country = tempCountry == null ? "" : tempCountry;

            String tempPostalCode = addresses.get(0).getPostalCode();
            postalCode = tempPostalCode == null ? "" : tempPostalCode;

            knownName = LocationUtl.getCompleteAddressString(context, lat, lng);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException ie) {
            ie.printStackTrace();
            return new LocationModel(Double.toString(lat), Double.toString
                    (lng), getTimeStamp(), "", "", "", "", "", "");
        }

        return new LocationModel(Double.toString(lat), Double.toString
                (lng), getTimeStamp(), address, city, state, country, postalCode, knownName);
    }

    private static String getTimeStamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        return dateformat.format(c.getTime());
    }

    public static LocationModel getUserLocation(Context context) {
        Double lat = 0.0;
        Double lng = 0.0;
        GPSTracker gpsTracker = new GPSTracker(context);
        if (gpsTracker.canGetLocation()) {
            lat = gpsTracker.getLatitude();
            lng = gpsTracker.getLongitude();
        }
        return getLocationData(context, lat, lng);
    }
}
