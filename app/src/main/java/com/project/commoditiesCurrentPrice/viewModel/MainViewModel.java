package com.project.commoditiesCurrentPrice.viewModel;

import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.commoditiesCurrentPrice.Application;
import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.model.RecordsModel;
import com.project.commoditiesCurrentPrice.repository.Repository;
import com.project.commoditiesCurrentPrice.utils.Constants;
import com.project.commoditiesCurrentPrice.utils.SharedPreferencesHelper;
import com.project.commoditiesCurrentPrice.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private final Repository repository;
    private final MutableLiveData<List<Record>> apiData;
    private final MutableLiveData<Boolean> error;
    private final MainViewModel.DatabaseProcessInterface listener;

    public MainViewModel(Repository repository, MainViewModel.DatabaseProcessInterface listener){
        this.repository = repository;
        apiData = new MutableLiveData<>();
        error = new MutableLiveData<>();
        this.listener = listener;
    }

    public MutableLiveData<List<Record>> getApiData() {
        return apiData;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }

    public void loadRecords(){
        repository.updateApiQueryMap();
        repository.add(
                repository.getRecordsFromAPI()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(RecordsModel::getRecords)
                        .subscribe(new Consumer<List<Record>>() {
                                       @Override
                                       public void accept(List<Record> recordList) throws Exception {
                                           if (recordList != null && !recordList.isEmpty()) {
                                               apiData.postValue(recordList);
                                               error.postValue(false);
                                           } else {
                                               error.postValue(true);
                                           }
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable error) throws Exception {
                                           MainViewModel.this.error.postValue(true);
                                       }
                                   }
                        )
        );
    }

    public void onClear(){
        repository.dispose();
    }

    public void purgeDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    repository.deleteAllRecordsFromDB();
                    Objects.requireNonNull(Looper.myLooper()).quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void insertRecordsToDatabase(List<Record> recordList, int pageCount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    if(pageCount == 1) {
                        SharedPreferencesHelper.getInstance().setDBEntryTime(Util.getCurrentDateAndTime());
                        repository.deleteAllRecordsFromDB();
                    }
                    repository.insertRecordsToDB(recordList);
                    Objects.requireNonNull(Looper.myLooper()).quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void bIsLoadDataFromDatabase() {
        listener.onReadRecordFromDBCompleted(repository.readRecordsFromDB());
    }

    public boolean bIsDatabaseExpired() {
        String dbEntryTime = SharedPreferencesHelper.getInstance().getDBEntryTime();
        if(dbEntryTime != null) {
            long lastSavedTime = dbEntryTime.isEmpty() ? 0 : Long.parseLong(dbEntryTime);
            long currentTime = Long.parseLong(Util.getCurrentDateAndTime());
            return (currentTime - lastSavedTime) > Constants.DB_EXPIRY_TIME;
        }
        return true;
    }

    public void closeDatabase() {
        repository.closeDB();
    }

    public interface DatabaseProcessInterface {

        void onReadRecordFromDBCompleted(List<Record> recordList);

    }


}
