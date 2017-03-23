package com.bbk.yang.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public final static String BASE_URL = "http://api.thinkpage.cn/v3/weather/";
    public final static String KEY = "etb467ryhtnwmrxv";


    @BindView(R.id.click_me_BN)
    Button clickMeBN;
    @BindView(R.id.result_TV)
    TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.click_me_BN)
    public void onClick() {
//        getWeatherLiveJustRetrofit();
//        getWeatherLiveRetrofitAndRxJava();
        getWeatherInfo();
    }

    private void getWeatherLiveJustRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<WeatherLiveBean> call = weatherService.getWeatherLive(KEY, "北京");
        call.enqueue(new Callback<WeatherLiveBean>() {

            @Override
            public void onResponse(Call<WeatherLiveBean> call, Response<WeatherLiveBean> response) {
                resultTV.setText(response.body().getResults().get(0).getLocation().toString());
                Gson gson = new Gson();
                XLog.json(gson.toJson(response.body()));
            }

            @Override
            public void onFailure(Call<WeatherLiveBean> call, Throwable t) {
                resultTV.setText(t.getMessage());
            }
        });

    }

    private void getWeatherLiveRetrofitAndRxJava() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        WeatherService movieService = retrofit.create(WeatherService.class);
        movieService.getWeatherLiveUseRxJava(KEY, "北京")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherLiveBean>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "Get BeijingWeather Completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        resultTV.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(WeatherLiveBean weatherLiveBean) {
                        resultTV.setText(weatherLiveBean.getResults().get(0).getLocation().toString());
                    }
                });
    }

    private Subscriber<WeatherLiveBean> mSubscriber;

    private void getWeatherInfo() {
        mSubscriber = new Subscriber<WeatherLiveBean>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this, "Get BeijingWeather Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                resultTV.setText(e.getMessage());
            }

            @Override
            public void onNext(WeatherLiveBean weatherLiveBean) {
                resultTV.setText(weatherLiveBean.getResults().get(0).getLocation().toString());
            }
        };
        HttpMethods.getInstance().getWeatherLiveBean(mSubscriber, KEY, "北京");
    }


}
