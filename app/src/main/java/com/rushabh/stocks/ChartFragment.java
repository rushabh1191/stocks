package com.rushabh.stocks;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.rushabh.stocks.modelclasses.StockNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment implements VolleyResponseListener {


    private static final String ARG1 = "arg";
    @Bind(R.id.webview)
    WebView webView;

    String data;

    StockNames stockNames;
    public ChartFragment() {
        // Required empty public constructor
    }

    void fetchStockInformation(){

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("Normalize",false);
            jsonObject.put("NumberOfDays",1095);
            jsonObject.put("DataPeriod","Day");

            JSONArray ar=new JSONArray();

            JSONObject stockInfo=new JSONObject();
            stockInfo.put("Symbol",stockNames.symbol);
            stockInfo.put("Type","price");

            JSONArray ar1=new JSONArray();
            ar1.put("ohlc");
            stockInfo.put("params",ar1);
            ar.put(stockInfo);
            jsonObject.put("Elements",ar);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://dev.markitondemand.com/MODApis/Api/v2/InteractiveChart/json?" +
                "parameters=" + jsonObject.toString();

        new VolleyRequest(url,1,this);


    }

    public static ChartFragment getInstance(StockNames stockNames){

        ChartFragment chartFragment=new ChartFragment();

        Bundle bundle=new Bundle();
        bundle.putSerializable(ARG1,stockNames);
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    void loadWebView(String data){
        if(webView!=null){
            webView.addJavascriptInterface(new WebAppInterface(getActivity(),this.data,stockNames.symbol), "Android");
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.loadUrl("file:///android_asset/index.html");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this,view);

        Bundle bundle=getArguments();
        this.stockNames= (StockNames) bundle.getSerializable(ARG1);

        fetchStockInformation();

        return  view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void responseRecieved(String response, int requestId) {

        this.data
                = response;
        loadWebView(response);
    }

    @Override
    public void errorRecieved(VolleyError error, int requestId) {

    }
}
class WebAppInterface {
    Context mContext;

    /** Instantiate the
     *  interface and set the context */
    String data;
    String symbol;
    WebAppInterface(Context c,String data,String symbol) {
        mContext = c;
        this.data=data;
        this.symbol=symbol;
    }

    @JavascriptInterface
    public String getStockData() {
//        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        return data;
    }
    @JavascriptInterface
    public String getSymbol(){
        return  symbol;
    }
    /** Show a toast from the web page */

}
