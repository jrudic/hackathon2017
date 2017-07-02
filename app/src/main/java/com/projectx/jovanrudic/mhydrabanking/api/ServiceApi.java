package com.projectx.jovanrudic.mhydrabanking.api;

import android.support.annotation.NonNull;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.projectx.jovanrudic.mhydrabanking.MyApplication;
import com.projectx.jovanrudic.mhydrabanking.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by urossimic on 6/30/17.
 */

public class ServiceApi {

    public interface Listener<T> {
        void onSuccess(T response);

        void onError(VolleyError error);
    }

    //example
    public static void sendDummieData(String test, @NonNull final Listener<User> callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoint.USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(new User());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });

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
