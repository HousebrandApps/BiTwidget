package com.housbrandapps.lunotickerwidget;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;
import retrofit2.http.Query;


class LunoTickerRepo {
    private Observer<LunoTickerResponse> subscriber;
    private LunoTickerService service;

    LunoTickerRepo(@NonNull LunoTickerService service) {
        this.service = service;
    }

    void setSubscriber(Observer<LunoTickerResponse> subscriber) {
        this.subscriber = subscriber;
    }

    void getTickerResponse(String pair) {
        try {
            service.getTickerByPair(pair)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "getLoginResponse: ", e);
        }
    }

    interface LunoTickerService {
        @GET("api/1/ticker")
        Observable<LunoTickerResponse> getTickerByPair(
                final @Query("pair") String pair
        );
    }

    class LunoTickerResponse {
        @SerializedName("timestamp")
        Long timeStamp;

        @SerializedName("bid")
        String bid;

        @SerializedName("ask")
        String ask;

        @SerializedName("last_trade")
        String lastTrade;

        @SerializedName("rolling_24_hour_volume")
        String rolling24HourValue;

        @SerializedName("pair")
        String pair;
    }
}
