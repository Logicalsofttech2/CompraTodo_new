package com.logicals.compratodo.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

/**
 * Created by Raghvendra Sahu on 01/11/2019.
 */
public class VolleySingleton {
    private static VolleySingleton mInstance;
   static Context context;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(TabLayout.OnTabSelectedListener context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton((Context) context);
        }
        return mInstance;
    }

    public static VolleySingleton getInstance(Context context) {
        if (mInstance == null) {

            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }
  /*  public static VolleySingleton getInstance(ActivitySignUp activitySignUp) {
        if (mInstance == null) {

            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }
    public static VolleySingleton getInstance(ActivityLogin activityLogin) {
        if (mInstance == null) {

            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }
    public static VolleySingleton getInstance(CategoryActivity categoryActivity) {
        if (mInstance == null) {

            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }*/


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
