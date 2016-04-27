package com.rushabh.stocks;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by rushabh on 26/04/16.
 */
public class StockApp extends Application {
    RequestQueue requestQueue;
    private static StockApp mInstance;

    public static final String TAG = StockApp.class
            .getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static synchronized StockApp getInstance(){
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            HurlStack stack = new HurlStack();
            requestQueue = Volley.newRequestQueue(this, stack);
        }

        return requestQueue;
    }

    public void addRequest(StringRequest request, String tag) {
        request.setTag(tag == null ? TAG : tag);
        getRequestQueue().add(request);
    }
}
