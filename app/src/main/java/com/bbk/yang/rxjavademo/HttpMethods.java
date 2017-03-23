package com.bbk.yang.rxjavademo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yang on 2017/3/23.
 */

public class HttpMethods {

    public final static String BASE_URL = "http://api.thinkpage.cn/v3/weather/";
    public final static String KEY = "etb467ryhtnwmrxv";

    private static final int TIME_OUT = 5;

    private Retrofit mRetrofit;
    private WeatherService mWeatherService;

    private HttpMethods() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mWeatherService = mRetrofit.create(WeatherService.class);
    }

    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getWeatherLiveBean(Subscriber<WeatherLiveBean> subscriber, String key, String location) {
        mWeatherService.getWeatherLiveUseRxJava(key, location)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getResultCode() != 0) {
                throw new ApiException(httpResult.getResultCode());
            }
            return httpResult.getData();
        }
    }

}
