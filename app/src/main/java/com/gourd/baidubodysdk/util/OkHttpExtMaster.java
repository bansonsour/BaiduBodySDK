package com.gourd.baidubodysdk.util;

import android.util.Log;

import com.gourd.baidubodysdk.BuildConfig;
import com.gourd.baidubodysdk.RetryNetworkInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import okhttp3.logging.HttpLoggingInterceptor.Logger;

/**
 * 项目名称：bi
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2018/3/27 16:38
 *
 * @version V1.0
 */
public class OkHttpExtMaster {
    private static final OkHttpExtMaster ourInstance = new OkHttpExtMaster();
    private static OkHttpClient httpClient;

    public static OkHttpExtMaster getInstance() {
        return ourInstance;
    }

    private OkHttpExtMaster() {
    }

    public static OkHttpClient getHttpClient() {
        if(httpClient == null) {
            synchronized(OkHttpClient.class) {
                if(httpClient == null) {
                    HttpLoggingInterceptor loggingInterceptor = null;
                    if(BuildConfig.DEBUG) {
                        loggingInterceptor = new HttpLoggingInterceptor(new Logger() {
                            public void log(String message) {
                                Log.e("OkHttpLogging", message);
                            }
                        });
                        loggingInterceptor.setLevel(Level.BODY);
                    }

                    Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(60L, TimeUnit.SECONDS);
                    builder.writeTimeout(60L, TimeUnit.SECONDS);
                    builder.readTimeout(60L, TimeUnit.SECONDS);
                    if((BuildConfig.DEBUG) && loggingInterceptor != null) {
                        builder.addInterceptor(loggingInterceptor);
                    }
                    builder.addInterceptor(new RetryNetworkInterceptor());
                    builder.retryOnConnectionFailure(true);

                    httpClient = builder.build();
                    httpClient.dispatcher().setMaxRequestsPerHost(10);
                }
            }
        }

        return httpClient;
    }
}
