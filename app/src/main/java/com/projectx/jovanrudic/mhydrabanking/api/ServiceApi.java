package com.projectx.jovanrudic.mhydrabanking.api;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.projectx.jovanrudic.mhydrabanking.model.User;


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
    }
}
