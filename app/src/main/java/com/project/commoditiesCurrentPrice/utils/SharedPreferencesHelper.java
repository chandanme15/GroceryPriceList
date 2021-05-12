package com.project.commoditiesCurrentPrice.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.commoditiesCurrentPrice.Application;

import static com.project.commoditiesCurrentPrice.utils.Constants.DatabaseTime;

public class SharedPreferencesHelper {

    private static SharedPreferencesHelper instance;
    private final SharedPreferences sharedPreferences;

    private SharedPreferencesHelper(Context context){
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static SharedPreferencesHelper getInstance() {
        if (instance == null) {
            synchronized (SharedPreferencesHelper.class) {
                if (instance == null) {
                    instance = new SharedPreferencesHelper(Application.getInstance());
                }
            }
        }
        return instance;
    }

    public void setDBEntryTime(String date){
        sharedPreferences.edit().putString(DatabaseTime, date).apply();
    }

    public String getDBEntryTime(){
        return sharedPreferences.getString(DatabaseTime, null);
    }

}
