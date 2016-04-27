package com.rushabh.stocks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.annotations.SerializedName;
import com.rushabh.stocks.modelclasses.StockNames;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


public class StockNewsFragment extends Fragment implements VolleyResponseListener {

    static String ARG_PARAM1 = "stock";

    public StockNames stockNames;

    @Bind(R.id.lv_news)
    ListView listView;


    public StockNewsFragment() {
        // Required empty public constructor
    }


    public static StockNewsFragment newInstance(StockNames stockNames) {
        StockNewsFragment fragment = new StockNewsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, stockNames);

        fragment.setArguments(args);
        return fragment;
    }

    public void fetchNews(){

        HashMap<String,String> params=new HashMap<>();

        String auth="90ljRCLuqXJ1kNWYVeytAaJC3FKLjr54pTtfbg+iILM";
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.putOpt("user",auth);
            jsonObject.putOpt("password",auth);
            params.put("Authorization",jsonObject.toString());

            String url="https://api.datamarket.azure.com/Bing/Search/v1/News?Query=%27"+
                    stockNames.symbol+"%27&$format=json";
            new VolleyRequest(url,params,1,this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();

        stockNames= (StockNames) bundle.getSerializable(ARG_PARAM1);

        fetchNews();

        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_stock_new, container, false);
        ButterKnife.bind(this,view);
        return  view;
    }


    @Override
    public void responseRecieved(String jsonObject, int requestId) {

        Log.d("beta respnse",jsonObject);
    }

    @Override
    public void errorRecieved(VolleyError error, int requestId) {
        Log.d("beta errep",error.toString());
    }
}

class NewsAdapter extends BaseAdapter{

    ArrayList<NewsModel> newsList;
    LayoutInflater inflater;

    public NewsAdapter(ArrayList<NewsModel> list, LayoutInflater inflater){
        this.inflater=inflater;
        this.newsList=list;
    }
    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public NewsModel getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsViewHolder holder;
        if(convertView==null){

            convertView=inflater.inflate(R.layout.item_news,parent,false);
            holder=new NewsViewHolder(convertView);

            convertView.setTag(holder);
        }
        else{

            holder= (NewsViewHolder) convertView.getTag();
        }

        NewsModel model=getItem(position);
        holder.tvNewsTitle.setText(model.newsTitle);
        return convertView;
    }

    class NewsViewHolder{

        @Bind(R.id.tv_news_title)
        TextView tvNewsTitle;

        NewsViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}

class NewsModel{
    @SerializedName("Title")
    String newsTitle;

    @SerializedName("Descriptionn")
    String desciption;

    @SerializedName("Source")
    String source;

    @SerializedName("Date")
    String date;

}