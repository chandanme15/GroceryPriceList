package com.project.commoditiesCurrentPrice.webService.service;

import android.content.Context;

import com.project.commoditiesCurrentPrice.helper.SharedPreferencesHelper;
import com.project.commoditiesCurrentPrice.model.RecordsModel;
import com.project.commoditiesCurrentPrice.webService.rest.RestClient;

import java.util.Map;

import io.reactivex.Observable;

public class APIService {
    private SharedPreferencesHelper sharedPreferencesHelper;
    private RestClient restClient;

    public APIService(Context context){
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
        restClient = new RestClient();
    }

    public void setDate(String date){
        sharedPreferencesHelper.setDate(date);
    }

    public String getDate(){
        return sharedPreferencesHelper.getDate();
    }

    public Observable<RecordsModel> getRecords(Map<String, String> map){
        return restClient.getApi().getRecords(map);
    }
}
