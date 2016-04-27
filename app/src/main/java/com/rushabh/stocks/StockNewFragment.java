package com.rushabh.stocks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rushabh.stocks.modelclasses.StockNames;


public class StockNewFragment extends Fragment {

    static String ARG_PARAM1 = "stock";

    public StockNewFragment() {
        // Required empty public constructor
    }


    public static StockNewFragment newInstance(StockNames stockNames) {
        StockNewFragment fragment = new StockNewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, stockNames);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_new, container, false);
    }


}
