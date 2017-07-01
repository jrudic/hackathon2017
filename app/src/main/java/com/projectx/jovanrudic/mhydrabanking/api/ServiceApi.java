package com.projectx.jovanrudic.mhydrabanking.api;

import android.support.annotation.NonNull;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.projectx.jovanrudic.mhydrabanking.MyApplication;
import com.projectx.jovanrudic.mhydrabanking.model.ResponseMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by urossimic on 6/30/17.
 */

public class ServiceApi {

    public static final String USER_ID = "user_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String POSTAL_CODE = "postal_code";
    public static final String KNOWN_ADDRESS = "known_address";
    public static final String TIME_STAMP = "time_stamp";
    public static final String FAKE_USER = "123";
    public static final String IMEI = "imei";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PHONE_MODEL = "phone_model";
    public static final String OS = "os";
    public static final String ERROR_MESSAGE = "error_message";
    public static final String ERROR = "error";

    public interface Listener<T> {
        void onSuccess(T response);

        void onError(VolleyError error);
    }

    //example
    public static void sendLocationData(final String latitude,
                                      final String longitude,
                                      final String address,
                                      final String city,
                                      final String state,
                                      final String country,
                                      final String postalCode,
                                      final String knownAddress,
                                      final String timeStamp,
                                        @NonNull final Listener<ResponseMessage> callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    String errorMessage = jo.getString("error_message");
                    int error = jo.getInt("error");
                    callback.onSuccess(new ResponseMessage(error, errorMessage));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(USER_ID, FAKE_USER); //User id will be provided from bank
                params.put(LATITUDE, latitude);
                params.put(LONGITUDE, longitude);
                params.put(ADDRESS, address);
                params.put(CITY, city);
                params.put(STATE, state);
                params.put(COUNTRY, country);
                params.put(POSTAL_CODE, postalCode);
                params.put(KNOWN_ADDRESS, knownAddress);
                params.put(TIME_STAMP, timeStamp);
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public static void sendPhoneData(final String imei,
                                        final String phone_number,
                                        final String phone_model,
                                        final String os,
                                        @NonNull final Listener<ResponseMessage> callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.DEVICE, new
                Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    String errorMessage = jo.getString(ERROR_MESSAGE);
                    int error = jo.getInt(ERROR);
                    callback.onSuccess(new ResponseMessage(error, errorMessage));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(USER_ID, FAKE_USER); //User id will be provided from bank
                params.put(IMEI, imei);
                params.put(PHONE_NUMBER, phone_number);
                params.put(PHONE_MODEL, phone_model);
                params.put(OS, os);
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public static void sendSafeLocationData(final String latitude,
                                            final String longitude,
                                     @NonNull final Listener<ResponseMessage> callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.SAFE_LOCATION, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            String errorMessage = jo.getString(ERROR_MESSAGE);
                            int error = jo.getInt(ERROR);
                            callback.onSuccess(new ResponseMessage(error, errorMessage));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(USER_ID, FAKE_USER); //User id will be provided from bank
                params.put(LATITUDE, latitude);
                params.put(LONGITUDE, longitude);
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }


    // example

//    public static void getUserProfile(final String uuid, final String profileUuid, final Listener<User> responseListener) {
//        CustomRequest request = new CustomRequest(Request.Method.POST, Endpoints.GET_USER_PROFILE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    responseListener.onResponse(User.parseUser(new JSONObject(response)));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Oops something went wrong
//                responseListener.onError(error);
//
//                if (error instanceof NetworkError) {
//                    noInternetMessage();
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put(C.FOLLOWING_ID_TAG, profileUuid);
//                params.put(C.UUID_TAG, uuid);
//                return params;
//            }
//
//        };
//
//        YummieApplication.getInstance().addToRequestQueue(request);
//    }
}
