package com.rushabh.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rushabh.stocks.helpers.ConfirmationWindow;
import com.rushabh.stocks.helpers.Utils;
import com.rushabh.stocks.modelclasses.StockDetailsModel;
import com.rushabh.stocks.modelclasses.StockNames;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class MainActivity extends AppCompatActivity implements VolleyResponseListener, View.OnClickListener {


    @Bind(R.id.stockName)
    AutoCompleteTextView etStockEntry;

    @Bind(R.id.pb)
    ProgressBar pb;

    Handler handler;



    ArrayList<StockNames> listOfStocks=new ArrayList<>();

    StockNameListAdapter stockNameListAdapter;

    FavListAdapter favListAdapter;

    @Bind(R.id.sw_switch)
    Switch aSwitch;

    @Bind(R.id.lvFav)
    RecyclerView lvFavList;

    StockNames stockName;

    ArrayList<StockDetailsModel> stockDetailList=new ArrayList<>();

    PreferenceHelper preferenceHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        etStockEntry.setThreshold(2);
        pb.setVisibility(View.GONE);
        stockNameListAdapter =new StockNameListAdapter(listOfStocks,this);

        handler=new Handler();
        etStockEntry.setAdapter(stockNameListAdapter);
        preferenceHelper=new PreferenceHelper(this);

        aSwitch.setChecked(preferenceHelper.isAutoRefresh());

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferenceHelper.saveAutoRefresh(isChecked  );
            }
        });

        favListAdapter=new FavListAdapter(stockDetailList,handler,runnable,this,this);
        lvFavList.setLayoutManager(new LinearLayoutManager(this));

        lvFavList.setAdapter(favListAdapter);

        lvFavList.setHasFixedSize(true);
        lvFavList.setAdapter(favListAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(favListAdapter);
        ItemTouchHelper mItemTouchHelper =  new ItemTouchHelper(callback);

        mItemTouchHelper.attachToRecyclerView(lvFavList);


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


/*        lvFavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockDetailsModel detailsModel=favListAdapter.getItem(position);

                stockName=new StockNames(detailsModel);
                getQuote(stockName);

            }
        });
        */
        etStockEntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<listOfStocks.size()){
                    stockName= stockNameListAdapter.getItem(position);
                }
            }
        });
    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {

            refreshList();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        refreshList();
    }

    @OnClick(R.id.iv_refresh)
    void refreshList(){


        new Thread(){

            @Override
            public void run() {
                super.run();
                PreferenceHelper preferenceHelper=new PreferenceHelper(MainActivity.this);
                ArrayList<String> keys=preferenceHelper.getAllKeys();
                final ArrayList<StockDetailsModel> list=new ArrayList<StockDetailsModel>();
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this, null);
                Gson gson=new Gson();


                for(int i=0;i<keys.size();i++) {

                    RequestFuture<String> future = RequestFuture.newFuture();
                    StringRequest request = createRequest(keys.get(i), future);
                    requestQueue.add(request);



                    try {
                        String response = future.get(30, TimeUnit.SECONDS);

                        StockDetailsModel model=gson.fromJson(response,StockDetailsModel.class);

                        list.add(model);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }


                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stockDetailList.clear();
                        stockDetailList.addAll(list);
                        favListAdapter.notifyDataSetChanged();
                        if(aSwitch.isChecked()){
                            handler.removeCallbacks(runnable);
                            handler.postDelayed(runnable,10000);
                        }


                    }
                });
            }
        }.start();


    }

    StringRequest createRequest(String stockNames,RequestFuture future){
        StringRequest request = null;



        String url="http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol=" + stockNames ;
        request = new StringRequest(Request.Method.GET, url, future, future) {
        };
        request.setShouldCache(false);
        int socketTimeout = 0;
        request.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return request;
    }


    @OnClick(R.id.btn_clear)
    void reset(){
        etStockEntry.setText("");
        stockName=null;
    }

    void getQuote(StockNames stockNames){
        Intent intent=StockInformationActivity.getIntent(stockName,this);
        startActivity(intent);
    }
    @OnClick(R.id.btn_get_quote)
    void getQuote(){


        if(etStockEntry.getText().toString().trim().length()==0){
            new ConfirmationWindow(this,"Error","Please enter stock name","OK","'");
        }
       else{
            if(stockName!=null){
               getQuote(stockName);
            }
            else{
                new ConfirmationWindow(this,"Error","Please select stock from list","OK","'");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            if (etStockEntry.getText().length() >= 3) {
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("hi",1);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int i = savedInstanceState.getInt("hi");
        Log.d("beta","Restored instance "+i);
    }

    @Override
    public void responseRecieved(String response, int requestId) {
        pb.setVisibility(View.GONE);
        try {
            JSONArray jsonArray=new JSONArray(response);

            int lenghth=jsonArray.length();

            Gson gson=new Gson();
            listOfStocks.clear();
            stockNameListAdapter.notifyDataSetChanged();
            for(int i=0;i<lenghth;i++){

                StockNames stockNames=gson.fromJson(jsonArray.getString(i),StockNames.class);
                listOfStocks.add(stockNames);
            }
            stockNameListAdapter.notifyDataSetChanged();




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void errorRecieved(VolleyError error, int requestId) {
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        FavView view= (FavView) v.getTag();
        int position=view.position;



        StockDetailsModel detailsModel=favListAdapter.getItem(position);
        stockName=new StockNames(detailsModel);
        getQuote(stockName);

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


class FavView extends RecyclerView.ViewHolder{

    @Bind(R.id.tv_fav_stockname)
    TextView tvStockName;

    @Bind(R.id.tv_fav_market_cap)
    TextView tvMarketCap;

    @Bind(R.id.tv_fav_price)
    TextView tvPrice;

    @Bind(R.id.tv_fav_stocknsymbol)
    TextView tvSymbol;

    @Bind(R.id.tv_fav_change)
    TextView tvChange;

    int position;

    public FavView(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
class FavListAdapter extends RecyclerView.Adapter<FavView> implements ItemTouchHelperAdapter {

    ArrayList<StockDetailsModel> detailsModelArrayList;

    LayoutInflater inflater;
    Context context;

    View.OnClickListener listener;

    Handler handler;
    Runnable runnable;

    FavListAdapter(ArrayList<StockDetailsModel> detailsModelArrayList, Handler handler,Runnable runnable
            ,Context context, View.OnClickListener listener) {

        this.handler = handler;
        this.runnable=runnable;
        this.context = context;
        this.detailsModelArrayList = detailsModelArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener=listener;
    }

    public StockDetailsModel getItem(int position) {
        return detailsModelArrayList.get(position);
    }

    @Override
    public FavView onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.item_stock_fav_list, parent, false);
        FavView holder = new FavView(convertView);


        convertView.setTag(holder);
        convertView.setOnClickListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(FavView holder, int position) {
        StockDetailsModel model = getItem(position);
        holder.tvStockName.setText(model.name);
        holder.position=position;
        holder.tvSymbol.setText(model.symbol);
        holder.tvChange.setText(Utils.round(model.pecent) + " % ");
        int color = model.pecent < 0 ? android.R.color.holo_red_dark : android.R.color.holo_green_light;

        holder.tvChange.setBackgroundColor(context.getResources().getColor(color));
        holder.tvMarketCap.setText("Market Cap " + Utils.truncateNumber(model.marketcap));
        holder.tvPrice.setText("$ " + model.price);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return detailsModelArrayList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(final int position) {

        final PreferenceHelper preferenceHelper=new PreferenceHelper(context);
        if(preferenceHelper.isAutoRefresh()){
            handler.removeCallbacks(runnable);
        }
        new ConfirmationWindow(context,"Confirm","Are you sure you want to remove?","OK ","Cancel"){
            @Override
            protected void setPositiveResponse() {
                super.setPositiveResponse();
                StockDetailsModel model=detailsModelArrayList.remove(position);

                preferenceHelper.removeKey(model.symbol);
                notifyDataSetChanged();

                if(preferenceHelper.isAutoRefresh()){
                    handler.postDelayed(runnable,10000);
                }
            }

            @Override
            protected void setNegativeResponse() {
                super.setNegativeResponse();
                notifyDataSetChanged();
            }
        };
        Log.d("beta","SD"+position);
    }
}

interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}

class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

}