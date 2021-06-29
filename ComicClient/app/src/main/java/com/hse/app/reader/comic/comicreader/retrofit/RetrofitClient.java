package com.hse.app.reader.comic.comicreader.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit instance;
    public static Retrofit getInstance(){

        if (instance == null)
            instance = new Retrofit.Builder().baseUrl("http://mobiledevbackend.herokuapp.com/manga/")
                           .addConverterFactory(GsonConverterFactory.create())
                           .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return instance;
    }
}
