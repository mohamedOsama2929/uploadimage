package com.example.osos.uploadimage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String baseUrl="http://demo.extra4it.net/blogger/api/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient(){

        if (retrofit==null){

            retrofit=new Retrofit.Builder().baseUrl(baseUrl).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
        }
        return retrofit;

    }
}
