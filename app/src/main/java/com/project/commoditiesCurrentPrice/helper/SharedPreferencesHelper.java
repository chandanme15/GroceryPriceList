package com.project.commoditiesCurrentPrice.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String DATE = "date";
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context){
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
    }

    public void setDate(String date){
        sharedPreferences.edit()
                .putString(DATE, date)
                .apply();
    }

    public String getDate(){
        return sharedPreferences.getString(DATE,null);
    }


}
