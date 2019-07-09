package com.gourd.baidubodysdk.util;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * 项目名称：BaiduBodySDK
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2019/7/8 20:29
 *
 * @version V1.0
 */
class ImageSelectorUtil {


    public static void setDraweeViewUri(SimpleDraweeView imageView, String path, ResizeOptions resizeOptions) {
        setDraweeViewUri(imageView, path, resizeOptions, null, true);
    }


    private static void setDraweeViewUri(SimpleDraweeView imageView, String path, ResizeOptions resizeOptions, ControllerListener listener, boolean gifPlay) {

        if (!TextUtils.isEmpty(path)) {

            Uri uri;

            if (path.startsWith("https://") || path.startsWith("http://") || path.startsWith("res://") || path.startsWith("asset://") || path.startsWith("android")) {
                uri = Uri.parse(path);
            } else {
                uri = Uri.fromFile(new File(path));
            }

            ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setAutoRotateEnabled(true)
                    .setResizeOptions(resizeOptions);

            DraweeController controller = Fresco
                    .newDraweeControllerBuilder()
                    .setImageRequest(imageRequestBuilder.build())
                    .setOldController(imageView.getController())
                    .setControllerListener(listener)
                    .setAutoPlayAnimations(UrlStringUtils.isAnimatedUrl(path) && gifPlay)
                    .build();
            imageView.setController(controller);
        }
    }
}
