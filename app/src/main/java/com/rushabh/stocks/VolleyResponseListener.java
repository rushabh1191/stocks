package com.rushabh.stocks;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by rushabh on 23/12/15.
 */
public interface VolleyResponseListener {

    void responseRecieved(String jsonObject, int requestId);
    void errorRecieved(VolleyError error, int requestId);


}
