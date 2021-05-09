package com.project.commoditiesCurrentPrice.viewModel;

import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.commoditiesCurrentPrice.Application;
import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.model.RecordsModel;
import com.project.commoditiesCurrentPrice.repository.Repository;
import com.project.commoditiesCurrentPrice.utils.CacheTime;
import com.project.commoditiesCurrentPrice.utils.Constants;
import com.project.commoditiesCurrentPrice.utils.FileSystem;
import com.project.commoditiesCurrentPrice.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private Repository repository;
    private MutableLiveData<List<Record>> apiData;
    private MutableLiveData<Boolean> error;
    private MainViewModel.DatabaseProcessInterface listener;

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
                        List<CacheTime> cacheTime = new ArrayList<>();
                        cacheTime.add(new CacheTime(Long.parseLong(Util.getCurrentDateAndTime())));
                        FileSystem.ReWriteFile(Application.getInstance(), Constants.DatabaseTimeFile, cacheTime);
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
        try {
            String temp = FileSystem.ReadFromFile(Application.getInstance(), Constants.DatabaseTimeFile).replaceAll("[\\D+]", "");
            long lastSavedTime = temp.isEmpty() ? 0 : Long.parseLong(temp);
            long currentTime = Long.parseLong(Util.getCurrentDateAndTime());
            return (currentTime - lastSavedTime) > Constants.DB_EXPIRY_TIME;
        }
        catch (Exception e) {
            //
        }
        return false;
    }

    public void closeDatabase() {
        repository.closeDB();
    }

    public interface DatabaseProcessInterface {

        void onReadRecordFromDBCompleted(List<Record> recordList);

    }


}
