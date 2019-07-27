package com.snh.module_netapi.requestApi;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.snh.library_base.BuildConfig;
import com.snh.library_base.utils.Contans;
import com.snh.library_base.utils.SPUtils;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class RetrofitProxy {
    private static Retrofit retrofit = null;
    private static OkHttpClient mOkHttpClient = null;


    /**
     * initialize
     */
    public static Retrofit init(final Context context) {

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
        Log.i("http","http");
        mOkHttpClient = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder())
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
                    .baseUrl(SPUtils.getInstance(context).getString(Contans.SP_HOSt))
                    .callbackExecutor(executor)
                    .client(mOkHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用RxJava作为回调适配器
                    .build();
        return  retrofit;
    }
}
