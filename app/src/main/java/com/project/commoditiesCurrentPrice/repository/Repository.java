package com.project.commoditiesCurrentPrice.repository;

import android.content.Context;

import androidx.room.Room;

import com.project.commoditiesCurrentPrice.dbRoom.RecordsDB;
import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.model.RecordsModel;
import com.project.commoditiesCurrentPrice.utils.Constants;
import com.project.commoditiesCurrentPrice.webService.service.APIService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class Repository {

    private static Repository instance;
    private RecordsDB recordsDB;
    private APIService apiService;
    private CompositeDisposable disposable;
    private Map<String, String> queryMap;

    public static Repository getInstance(Context context) {
        if(null == instance) {
            synchronized (Repository.class) {
                if(null == instance) {
                    instance = new Repository(context);
                }
            }
        }
        return instance;
    }

    private Repository(Context context) {
        recordsDB = Room.databaseBuilder(context, RecordsDB.class, RecordsDB.class.getName()).build();
        apiService = new APIService(context);
        disposable = new CompositeDisposable();
        queryMap = new HashMap<>();
        initApiQueryMap();
    }

    public Observable<RecordsModel> getRecordsFromAPI(){
        return apiService.getRecords(queryMap);
    }

    public void dispose() {
        if(disposable != null ) disposable.dispose();
    }

    public void add(Disposable disposable) {
        if(this.disposable != null ) this.disposable.add(disposable);
    }

    public void insertRecordsToDB(List<Record> recordList){
        recordsDB.recordsDao().insertRecords(recordList);
    }

    public void deleteAllRecordsFromDB(){
        recordsDB.recordsDao().deleteAllRecords();
    }

    public List<Record> readRecordsFromDB(){
        return recordsDB.recordsDao().getRecords();
    }

    public void closeDB() {
        recordsDB.close();
    }

    public void initApiQueryMap(){
        queryMap.put(Constants.QueryMap.ATTRIBUTE_API_KEY, Constants.API_KEY);
        queryMap.put(Constants.QueryMap.ATTRIBUTE_FORMAT, Constants.API_RESPONSE_FORMAT);
        queryMap.put(Constants.QueryMap.ATTRIBUTE_LIMIT, Constants.NO_OF_RECORDS_PER_REQUEST);
    }

    public void updateApiQueryMap(){
        queryMap.put(Constants.QueryMap.ATTRIBUTE_OFFSET, String.valueOf((Constants.PAGE_COUNT - 1) * 10));
    }
}
