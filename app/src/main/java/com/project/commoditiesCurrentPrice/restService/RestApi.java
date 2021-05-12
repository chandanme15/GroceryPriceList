package com.project.commoditiesCurrentPrice.restService;

import com.project.commoditiesCurrentPrice.model.RecordsModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RestApi {

    @GET("resource/9ef84268-d588-465a-a308-a864a43d0070")
    Observable<RecordsModel> getRecords(@QueryMap Map<String, String> filter);
}
