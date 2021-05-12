package com.project.commoditiesCurrentPrice.restService;

import com.project.commoditiesCurrentPrice.utils.Constants;
import com.project.commoditiesCurrentPrice.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.project.commoditiesCurrentPrice.utils.Constants.REQUEST_TIME_OUT;

public class RestClient {

    private final RestApi restApi;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL).client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        restApi = retrofit.create(RestApi.class);
    }

    public RestApi getApi(){
        return restApi;
    }

    //get and configure okhttp client
    private OkHttpClient getHttpClient() {

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        if(BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        //add interceptor
        client.addInterceptor(loggingInterceptor);

        client.readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS);
        client.connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS);

        return client.build();
    }
}
