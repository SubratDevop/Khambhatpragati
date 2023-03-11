package com.khambhatpragati.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.khambhatpragati.mvp.view.MVPView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bhagvatee1.gupta on 25-11-2016.
 */

public class ServiceConnector {

    private static final String TAG = ServiceConnector.class.getName();

    private static MVPView mvpView;

    private static int TIMEOUT = 30000;// 15 Second

    public ServiceConnector(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    public void post(Context context, String url, Map<String, Object> jsonRequestParams) {
        Log.d(TAG, "url = " + url);
        JSONObject jsonObject = new JSONObject(jsonRequestParams);
        Log.d(TAG, "JSON POST Request = " + jsonObject.toString());
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonRequestParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, "JSON POST Response = " + jsonObject.toString());
                        mvpView.onSuccess(jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Volley Error in Response = " + error.getMessage());
                        mvpView.onError(error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            mvpView.onError("Network timeout error");
                        } else if (error instanceof AuthFailureError) {
                            mvpView.onError("There was an authentication failure while performing a Request.");
                        } else if (error instanceof ServerError) {
                            mvpView.onError("ServerError");
                        } else if (error instanceof NetworkError) {
                            mvpView.onError("NetworkError");
                        } else if (error instanceof ParseError) {
                            mvpView.onError("ParseError, Server's response could not be parsed.");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    public void get(Context context, String url) {
        //Log.d(TAG, "url = " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonString) {
                        //Log.d(TAG, "JSON GET Response = " + jsonString);
                        mvpView.onSuccess(jsonString);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mvpView.onError(error.getClass().getSimpleName());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void post(Context context, String url, String jsonRequest) {
        //Log.d(TAG, "url = " + url);
        //Log.d(TAG, "JSON POST Request = " + jsonRequest);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest objectRequest = null;
        try {
            objectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(jsonRequest),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            //Log.d(TAG, "JSON POST Response = " + jsonObject.toString());
                            mvpView.onSuccess(jsonObject.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mvpView.onError(error.getClass().getSimpleName());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue.add(objectRequest);
    }

    private JSONObject parseToJson(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            //Log.d(TAG, e.getStackTrace().toString());
            e.printStackTrace();
        }
        return null;

    }
}