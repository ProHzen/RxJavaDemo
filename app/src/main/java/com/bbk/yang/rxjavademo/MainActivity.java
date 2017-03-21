package com.bbk.yang.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        getWeatherLive();
    }

    private void getWeatherLive() {

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
            }

            @Override
            public void onFailure(Call<WeatherLiveBean> call, Throwable t) {
                resultTV.setText(t.getMessage());
            }
        });



    }
}
