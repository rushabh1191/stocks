package com.rushabh.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rushabh.stocks.modelclasses.StockNames;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockInformationActivity extends AppCompatActivity {


    private static final String STOCK_NAME = "stock_name";
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Bind(R.id.tabs)
    TabLayout tabs;

    StockNames stockNames;

    MenuItem favItem;

    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_information);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper=new PreferenceHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        stockNames= (StockNames) getIntent().getExtras().get(STOCK_NAME);

        setTitle(stockNames.name);


        ArrayList<Fragment> fragments=new ArrayList<>();

        StockDetailsFragment fragment=StockDetailsFragment.newInstance(stockNames);
        fragments.add(fragment);

        ChartFragment chartFragment=ChartFragment.getInstance(stockNames);
        fragments.add(chartFragment);

        StockNewsFragment stockNewsFragment=StockNewsFragment.newInstance(stockNames);
        fragments.add(stockNewsFragment);
//        fragments.add(new StockDetailsFragment());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(mViewPager);

    }

    public static Intent getIntent(StockNames stockNames, Context context){
        Intent intent = new Intent(context,StockInformationActivity.class);
        intent.putExtra(STOCK_NAME,stockNames);
        return intent;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_information, menu);
        return true;
    }

    void showFavUnfav(boolean isFav){

        int icon=isFav?R.drawable.ic_favorite_filled:R.drawable.ic_favorite_border;
        favItem.setIcon(icon);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        favItem=menu.findItem(R.id.id_fav);
        showFavUnfav(preferenceHelper.isKeyPresent(stockNames.symbol));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
        }
        else if(id==R.id.id_fav){
            boolean isKeyPresent=preferenceHelper.isKeyPresent(stockNames.symbol);
            if(isKeyPresent){
                preferenceHelper.removeKey(stockNames.symbol);
            }
            else{
                preferenceHelper.saveInt(stockNames.symbol,1);
            }
            showFavUnfav(!isKeyPresent);

        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stock_information, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public ArrayList<Fragment> fragmentArrayList;
        public SectionsPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragmentArrayList)
        {
            super(fm);
            this.fragmentArrayList=fragmentArrayList;
        }

        @Override
        public Fragment getItem(int position) {


            return  fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragmentArrayList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Current";
                case 1:
                    return "Historical";
                case 2:
                    return "News";
            }
            return null;
        }
    }
}
