package com.gourd.baidubodysdk;

import android.app.Application;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.gourd.baidubodysdk.util.AppUtils;
import com.gourd.baidubodysdk.util.OkHttpExtMaster;

import java.io.File;

/**
 * 项目名称：BaiduBodySDK
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2019/7/8 20:31
 *
 * @version V1.0
 */
public class FaceMergeApp extends Application {
    private static final String TAG_FRESCO_LIB = "FrescoLibrary";

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.setContext(this);      //工具类

        Sample.init();
        initFresco();
    }

    private void initFresco() {
        FLog.setMinimumLoggingLevel(BuildConfig.DEBUG ? FLog.VERBOSE : FLog.INFO);
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(new File(Sample.FrescoPath))
                .setMaxCacheSize(200 * 1024 * 1024)//200MB
                .build();

        //配置基本路径,并设置setDownsampleEnabled()以解决ResizeOptions不能解决的一些情况
        //配置Fresco统一使用Okhttp3的自定的网络层
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(this, OkHttpExtMaster.getHttpClient())
                .setDownsampleEnabled(true)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setMemoryTrimmableRegistry(new MemoryTrimmableRegistry() {
                    @Override
                    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                        Log.i(TAG_FRESCO_LIB, String.format("registerMemoryTrimmable %s", trimmable));
                        if (null != trimmable) {
                        }
                    }

                    @Override
                    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
                        Log.i(TAG_FRESCO_LIB, String.format("unregisterMemoryTrimmable %s", trimmable));
                        if (null != trimmable) {
                        }
                    }
                })
                .build();
        Fresco.initialize(this, imagePipelineConfig);
    }
}
