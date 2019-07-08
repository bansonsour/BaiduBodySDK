package com.facemerge.util;

import android.content.Context;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

/**
 * 项目名称：BaiduBodySDK
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2019/7/8 15:19
 *
 * @version V1.0
 */
public class OpenUtils {
    public static Bitmap mat2bitmap(Context context, int resId) {
        Mat srcMat = null;
        Mat resultMat = new Mat();
        try {
            srcMat = Utils.loadResource(context, resId, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Imgproc.cvtColor(srcMat, resultMat, Imgproc.COLOR_RGB2BGRA);
        Bitmap bmp = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, bmp);
        return bmp;
    }
    
}
