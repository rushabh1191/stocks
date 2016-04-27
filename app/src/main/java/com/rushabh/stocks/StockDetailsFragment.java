package com.rushabh.stocks;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.rushabh.stocks.helpers.Utils;
import com.rushabh.stocks.modelclasses.StockNames;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockDetailsFragment extends Fragment implements VolleyResponseListener {


    private static final String ARG_PARAM1 = "stock_name";

    @Bind(R.id.et_name)
    EditText etName;

    @Bind(R.id.et_symbol)
    EditText etSymbol;

    @Bind(R.id.et_last_price)
    EditText etStockPrice;

    @Bind(R.id.et_change)
    EditText etChange;
    @Bind(R.id.et_volume)
    EditText etVolume;

    @Bind(R.id.et_timestamp)
    EditText etTimestamp;

    @Bind(R.id.et_market_cap)
    EditText etMarketCap;

    @Bind(R.id.et_change_ytd)
    EditText etChangeYTD;

    @Bind(R.id.et_high)
    EditText etHigh;

    @Bind(R.id.et_low)
    EditText etLow;

    @Bind(R.id.et_open)
    EditText etOpen;

    @Bind(R.id.iv_stock_image)
    ImageView ivStockImage;



    StockNames stockNames;
    public StockDetailsFragment() {
        // Required empty public constructor
    }
    public static StockDetailsFragment newInstance(StockNames stockNames) {
        StockDetailsFragment fragment = new StockDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, stockNames);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle=getArguments();
        stockNames= (StockNames) bundle.getSerializable(ARG_PARAM1);

        seachStock();;
    }

    void seachStock(){
        HashMap<String,String> params=new HashMap<>();
        params.put("symbol",stockNames.symbol);
        new VolleyRequest(params,1,this,VolleyRequest.URL_Quote);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_stock_details, container, false);
        ButterKnife.bind(this,view);

        String url= "http://chart.finance.yahoo.com/t?s="+stockNames.symbol+"&lang=en-US&width=400&height=300";
        Log.d("beta",url);
        Picasso.with(getActivity()).load(url).into(ivStockImage);

        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void responseRecieved(String jsonObject, int requestId) {
        try {
            JSONObject json=new JSONObject(jsonObject);
            etName.setText(json.getString("Name"));
            etSymbol.setText(json.getString("Symbol"));
            etStockPrice.setText(json.getString("LastPrice"));
            etVolume.setText(json.getString("Volume"));
            etChange.setText(Utils.round(json.getDouble("Change"))+ " ( "
                    +Utils.round(json.getDouble("ChangePercent"))+" % )");//ChangePercent
            etChangeYTD.setText(json.getString("ChangeYTD"));
            etMarketCap.setText(Utils.truncateNumber(json.getDouble("MarketCap")));
            etTimestamp.setText(json.getString("Timestamp"));
            etHigh.setText(json.getString("High"));
            etLow.setText(json.getString("Low"));
            etOpen.setText(json.getString("Open"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

   /* {
        "Status":"SUCCESS",
            "Name":"Apple Inc",
            "Symbol":"AAPL",
            "LastPrice":97.75,
            "Change":-6.59999999999999,
            "ChangePercent":-6.32486823191183,
            "Timestamp":"Wed Apr 27 15:28:33 UTC-04:00 2016",
            "MSDate":42487.6448263889,
            "MarketCap":541982988250,
            "Volume":6655097,
            "ChangeYTD":105.26,
            "ChangePercentYTD":-7.13471404142125,
            "High":98.7,
            "Low":95.69,
            "Open":96.02
    }
*/
    @Override
    public void errorRecieved(VolleyError error, int requestId) {

    }
}
