package com.rushabh.stocks;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by rushabh on 5/9/14.
 */
public class PreferenceHelper {


    private SharedPreferences preference;





    public PreferenceHelper(Context context){
        try {
            preference=context.getSharedPreferences("com.adda.app", Context.MODE_PRIVATE);
        }
        catch (Exception e)
        {

        }


    }


    public boolean isKeyPresent(String key){

        Map<String,?> map=preference.getAll();
        return map.containsKey(key);
    }
    public ArrayList<String> getAllKeys(){

        Map<String, ?> map=preference.getAll();
        ArrayList<String> keys=new ArrayList<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    public void saveInt(String key,int value)
    {
        preference.edit().putInt(key,value).apply();
    }


    public void removeKey(String symbol) {
        preference.edit().remove(symbol).apply();
    }
}
