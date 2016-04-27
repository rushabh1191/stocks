package com.rushabh.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.rushabh.stocks.helpers.ConfirmationWindow;
import com.rushabh.stocks.modelclasses.StockNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements VolleyResponseListener {


    @Bind(R.id.stockName)
    AutoCompleteTextView etStockEntry;

    @Bind(R.id.pb)
    ProgressBar pb;

    Handler handler;



    ArrayList<StockNames> listOfStocks=new ArrayList<>();

    StockNameListAdapter adapter;

    StockNames stockName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        etStockEntry.setThreshold(2);
        pb.setVisibility(View.GONE);
        adapter=new StockNameListAdapter(listOfStocks,this);

        etStockEntry.setAdapter(adapter);

        handler=new Handler();

        etStockEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stockName=null;
                handler.removeCallbacks(searchRunnable);
                handler.postDelayed(searchRunnable, 500);
            }
        });

        etStockEntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<listOfStocks.size()){
                    stockName=adapter.getItem(position);
                }
            }
        });
    }


    @OnClick(R.id.btn_clear)
    void reset(){
        etStockEntry.setText("");
        stockName=null;
    }

    @OnClick(R.id.btn_get_quote)
    void getQuote(){
        if(etStockEntry.getText().toString().trim().length()==0){
            new ConfirmationWindow(this,"Error","Please enter stock name","OK","'");
        }
       else{
            if(stockName!=null){
                Intent intent=StockInformationActivity.getIntent(stockName,this);
                startActivity(intent);
            }
            else{
                new ConfirmationWindow(this,"Error","Please select stock from list","OK","'");
            }
        }
    }
    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (etStockEntry.getText().length() >= 2) {
                searchStock(etStockEntry.getText().toString());
            }
        }
    };

    void searchStock(String input){

        HashMap<String,String> params=new HashMap<>();
        params.put("input",input);
        new VolleyRequest(params,1,this,VolleyRequest.URL_LOOKUP);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void responseRecieved(String response, int requestId) {
        pb.setVisibility(View.GONE);
        try {
            JSONArray jsonArray=new JSONArray(response);

            int lenghth=jsonArray.length();

            Gson gson=new Gson();
            listOfStocks.clear();
            for(int i=0;i<lenghth;i++){

                StockNames stockNames=gson.fromJson(jsonArray.getString(i),StockNames.class);
                listOfStocks.add(stockNames);
            }
            adapter.notifyDataSetChanged();




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void errorRecieved(VolleyError error, int requestId) {
        pb.setVisibility(View.GONE);
    }
}


class StockNameListAdapter extends ArrayAdapter<StockNames>{

    ArrayList<StockNames> list;

    Context context;

    LayoutInflater inflater;
    StockNameListAdapter(ArrayList<StockNames> list, Context context){
        super(context,R.layout.item_stock_name,list);
        this.list=list;
        this.context=context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StockNames getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StockViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_stock_name,parent,false);
            holder=new StockViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder= (StockViewHolder) convertView.getTag();
        }


        StockNames s=getItem(position);
        holder.tvStockName.setText(s.symbol);
        holder.tvStockExchange.setText(s.name+"("+s.exchange+")");
        return convertView;
    }

    class StockViewHolder{

        @Bind(R.id.tv_stock_name)
        TextView tvStockName;

        @Bind(R.id.tv_stock_exchange)
        TextView tvStockExchange;
        StockViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}


