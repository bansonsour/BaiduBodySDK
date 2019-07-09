package com.gourd.baidubodysdk;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目名称：bi
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2018/3/28 14:34
 *
 * @version V1.0
 */
public class RetryNetworkInterceptor implements Interceptor {

    public int mMaxRetry = 3;//最大重试次数
    private int mRetryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    public RetryNetworkInterceptor() {
    }

    public RetryNetworkInterceptor(int maxRetry) {
        this.mMaxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && mRetryNum < mMaxRetry) {
            mRetryNum++;
            response = chain.proceed(request);
            Log.e("OkHttpLogging", "mRetryNum=" + mRetryNum);
        }
        return response;
    }
}

