package com.my.localizadorapp.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClients {

  //  private static final String BASE_URL ="https://myspotbh.com/localizador/webservice/";
    private static final String BASE_URL ="https://technorizen.com/localizador/webservice/";
    private static RetrofitClients mInstance;
    private Retrofit retrofit;

    private RetrofitClients(){
        final  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(400, TimeUnit.SECONDS)
                .readTimeout(400, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

public static synchronized RetrofitClients getInstance(){

        if (mInstance == null){
       mInstance = new RetrofitClients();
        }
        return mInstance;
    }

  public Api getApi(){
   return retrofit.create(Api.class);

  }

}
