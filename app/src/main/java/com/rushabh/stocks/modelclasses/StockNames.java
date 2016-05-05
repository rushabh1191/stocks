package com.rushabh.stocks.modelclasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rushabh on 27/04/16.
 */
public class StockNames implements Serializable{

    @SerializedName("Symbol")
    public String symbol;
    @SerializedName("Name")
    public String name;
    @SerializedName("Exchange")
    public String exchange;

    public StockNames(StockDetailsModel detailsModel) {
        name=detailsModel.name;
        symbol=detailsModel.symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
