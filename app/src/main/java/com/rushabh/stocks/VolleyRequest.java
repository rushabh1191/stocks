package com.rushabh.stocks;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class VolleyRequest {


    public static String TAG = "volley";

    public static final String URL_LOOKUP="Lookup";
    public static final String URL_Quote="Quote";

    private VolleyResponseListener onVolleyResponse;

    HashMap<String,String> params;
    public VolleyRequest(String url, final int requestId, final VolleyResponseListener listener) {

        makeRequest(url, requestId, listener);

    }

    void makeRequest(String url, final int requestId, final VolleyResponseListener listener) {
        StockApp app = StockApp.getInstance();

        StringRequest volleyRequest = createRequest(url, requestId, listener);
        app.addRequest(volleyRequest, requestId + "");
    }

    public VolleyRequest(HashMap<String, String> params, int requestId, VolleyResponseListener listener,String type) {
        String url = makeUrl(params,type);
        makeRequest(url, requestId, listener);

    }

    public VolleyRequest(String url,HashMap<String, String> headers, int requestId, VolleyResponseListener listener) {

        this.params=headers;
        StockApp app = StockApp.getInstance();

        StringRequest volleyRequest = createRequest(headers,url,1,listener);
        app.addRequest(volleyRequest, requestId + "");

    }


    public String makeUrl(HashMap<String, String> params,String type) {

        Iterator<String> keys = params.keySet().iterator();
        String param = "";

        while (keys.hasNext()) {
            String key = keys.next();
            param = key + "=" + params.get(key) + "&";
        }
        return "http://dev.markitondemand.com/MODApis/Api/v2/"+type+"/json?" + param;
    }

    public StringRequest createRequest(String finalUrl, final int requestId, final VolleyResponseListener listener) {

        onVolleyResponse = listener;

        StringRequest request = new StringRequest(Request.Method.GET, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (onVolleyResponse != null) {

                    Log.d("beta", response);
                    onVolleyResponse.responseRecieved(response, requestId);


                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        if (onVolleyResponse != null)
                            onVolleyResponse.errorRecieved(error, requestId);
                    }
                }
        );

        request.setShouldCache(false);

        int socketTimeout = 30000;
        request.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return request;
    }

    public StringRequest createRequest(final HashMap<String,String> headers, String finalUrl, final int requestId, final VolleyResponseListener listener) {

        onVolleyResponse = listener;

        StringRequest request = new StringRequest(Request.Method.GET, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (onVolleyResponse != null) {

                    Log.d("beta", response);
                    onVolleyResponse.responseRecieved(response, requestId);


                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        if (onVolleyResponse != null)
                            onVolleyResponse.errorRecieved(error, requestId);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String,String> header= new HashMap<>();

                String accountKeyEnc="OTBsalJDTHVxWEoxa05XWVZleXRBYUpDM0ZLTGpyNTRwVHRmYmcraUlMTTo5MGxqUkNMdXFYSjFrTldZVmV5dEFhSkMzRktManI1NHBUdGZiZytpSUxN";
                Log.d("beta",accountKeyEnc);

                String autValue=String.format("Basic %s", accountKeyEnc);
                header.put("Authorization",autValue);

                return  header;
            }


        };

        request.setShouldCache(false);

        int socketTimeout = 30000;
        request.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.d("beta","Request "+request.toString());
        return request;
    }
}

