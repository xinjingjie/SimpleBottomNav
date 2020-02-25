package com.example.simplebottomnav.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton INSTANCE;
    private RequestQueue queue;

    public static VolleySingleton getINSTANCE(Context context) {
        if ( INSTANCE == null ) {
            INSTANCE = new VolleySingleton(context);
        }
        return INSTANCE;
    }

    private VolleySingleton(Context context) {
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public RequestQueue getQueue() {
        if ( queue == null ) {
            Log.d("did", "getQueue: is null");
        }
        return queue;
    }
}
