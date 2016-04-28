package com.rushabh.stocks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.rushabh.stocks.modelclasses.StockNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


public class StockNewsFragment extends Fragment implements VolleyResponseListener {

    static String ARG_PARAM1 = "stock";

    public StockNames stockNames;

    @Bind(R.id.lv_news)
    ListView listView;
    ArrayList<NewsModel> newsModelArrayList=new ArrayList<>();
    NewsAdapter adapter;


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

    public void fetchNews() {

        HashMap<String, String> params = new HashMap<>();

        String url = "https://api.datamarket.azure.com/Bing/Search/v1/News?Query=%27" +
                stockNames.symbol + "%27&$format=json";

        new VolleyRequest(url, params, 1, this);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        stockNames = (StockNames) bundle.getSerializable(ARG_PARAM1);


        fetchNews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_new, container, false);
        ButterKnife.bind(this, view);

        adapter=new NewsAdapter(newsModelArrayList,inflater);

        listView.setAdapter(adapter);
        return view;
    }


    @Override
    public void responseRecieved(String response, int requestId) {

//        Log.d("beta respnse", jsonObject);

        try {
            JSONObject jsonObject=new JSONObject(response);

            JSONArray news=jsonObject.getJSONObject("d").getJSONArray("results");

            Gson gson=new Gson();
            for(int i=0;i<news.length();i++){
                NewsModel newsModel=gson.fromJson(news.getString(i),NewsModel.class);
                newsModelArrayList.add(newsModel);
            }

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void errorRecieved(VolleyError error, int requestId) {
    }
}

class NewsAdapter extends BaseAdapter {

    ArrayList<NewsModel> newsList;
    LayoutInflater inflater;

    public NewsAdapter(ArrayList<NewsModel> list, LayoutInflater inflater) {
        this.inflater = inflater;
        this.newsList = list;
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
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_news, parent, false);
            holder = new NewsViewHolder(convertView);

            convertView.setTag(holder);
        } else {

            holder = (NewsViewHolder) convertView.getTag();
        }

        NewsModel model = getItem(position);
        holder.tvNewsTitle.setText(Html.fromHtml("<u>"+model.newsTitle+"</u>"));

        Log.d("beta","d"+model.desciption);
        holder.tvNewsDesciprtion.setText(model.desciption);
        holder.tvSource.setText("Publisher : "+model.source);
        holder.tvDate.setText("Date : "+model.date);

        return convertView;
    }

    class NewsViewHolder {

        @Bind(R.id.tv_news_title)
        TextView tvNewsTitle;

        @Bind(R.id.tv_news_description)
        TextView tvNewsDesciprtion;

        @Bind(R.id.tv_source)
        TextView tvSource;
        @Bind(R.id.tv_date)
        TextView tvDate;

        NewsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

class NewsModel {
    @SerializedName("Title")
    String newsTitle;

    @SerializedName("Description")
    String desciption;

    @SerializedName("Source")
    String source;

    @SerializedName("Date")
    String date;

}