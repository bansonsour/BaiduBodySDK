package com.gourd.baidubodysdk.util;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.yy.bimodule.resourceselector.resource.ImageLoader;

/**
 * 用于资源选择器的ImageLoader
 *
 * Created on 2018/12/25
 *
 * @author weitianpeng
 */
public class ImageSelectorLoader implements ImageLoader {

    private static final int SAMPLE_SQUARE_SIDE =
            DensityUtils.getWidthPx(AppUtils.getAppContext()) / 3;

    @Override
    public void displayImage(Context context, String path, SimpleDraweeView imageView) {
        ImageSelectorUtil.setDraweeViewUri(imageView, path,
                new ResizeOptions(SAMPLE_SQUARE_SIDE, SAMPLE_SQUARE_SIDE));
    }
}
