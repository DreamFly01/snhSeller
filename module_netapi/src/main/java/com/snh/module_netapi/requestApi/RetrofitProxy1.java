package com.snh.module_netapi.requestApi;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.BuildConfig;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class RetrofitProxy1 {
    private static Retrofit retrofit = null;
    private static OkHttpClient mOkHttpClient = null;


    /**
     * initialize
     */
    public static Retrofit init(final Context context  ) {

        Executor executor;
        executor = Executors.newCachedThreadPool();
        Gson gson = new GsonBuilder().create();
        LoggingInterceptor httpLog = new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build();


        mOkHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .addNetworkInterceptor(httpLog)
//                .addInterceptor(heardInterceptor)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();



            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())//使用String作为数据转换器
                    .baseUrl("https://shop.snihen.com:8448/")
                    .callbackExecutor(executor)
                    .client(mOkHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用RxJava作为回调适配器
                    .build();
return retrofit;
    }
}
