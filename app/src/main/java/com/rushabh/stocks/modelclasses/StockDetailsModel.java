package com.rushabh.stocks.modelclasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rushabh on 30/04/16.
 */
public class StockDetailsModel {

    @SerializedName("Symbol")
    public String symbol;
    @SerializedName("Name")
    public String name;
    @SerializedName("MarketCap")
    public double marketcap;
    @SerializedName("LastPrice")
    public double price;
    @SerializedName("ChangePercent")
    public double pecent;

}
