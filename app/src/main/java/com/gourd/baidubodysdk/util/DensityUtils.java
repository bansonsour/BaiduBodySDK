package com.gourd.baidubodysdk.util;

import android.content.Context;

/**
 * <p>Project: bi</p>
 * <p>Description: DensityUtils</p>
 * <p>Copyright (c) 2016 www.duowan.com Inc. All rights reserved.</p>
 * <p>Company: YY Inc.</p>
 *
 * @author: Aragon.Wu
 * @date: 2016-05-30
 * @vserion: 1.0
 */
public class DensityUtils {

    public static int dip2px(float dpValue) {
        return dip2px(AppUtils.getAppContext(), dpValue);
    }

    public static int px2dip(float pxValue) {
        return px2dip(AppUtils.getAppContext(), pxValue);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getHeightPx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getWidthPx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeightPx() {
        return AppUtils.getAppContext().getResources().getDisplayMetrics().heightPixels;
    }


    public static int getWidthPx() {
        return AppUtils.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static float getDensity() {
        return AppUtils.getAppContext().getResources().getDisplayMetrics().density;
    }
}
