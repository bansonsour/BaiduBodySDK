package com.gourd.baidubodysdk;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 项目名称：BaiduBodySDK
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2019/7/1 17:00
 *
 * @version V1.0
 */
public class PngConverter {

    @RequiresApi(api = VERSION_CODES.KITKAT)
    public static void convertToJpg(String pngFilePath, String jpgFilePath) {
        BitmapFactory.Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inDither = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pngFilePath, options);
        System.out.println("convertToJpg--->bitmap.hasAlpha()::::" + bitmap.hasAlpha());
        bitmap.setHasAlpha(true);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jpgFilePath))) {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)) {
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = VERSION_CODES.KITKAT)
    public static void compressPng(String pngFilePath, String comPngFilePath) {
        BitmapFactory.Options options = new Options();
        options.inPreferredConfig = Config.RGB_565;
        options.inDither = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pngFilePath, options);
        System.out.println("compressPng-->bitmap.hasAlpha()::::" + bitmap.hasAlpha());
        bitmap.setHasAlpha(true);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(comPngFilePath))) {
            if (bitmap.compress(CompressFormat.PNG, 100, bos)) {
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = VERSION_CODES.KITKAT)
    public static void compressPng(byte[] data, String comPngFilePath) {
        BitmapFactory.Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inDither = true;
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        System.out.println("compressPng-->bitmap.hasAlpha()::::" + bitmap.hasAlpha());
        bitmap.setHasAlpha(true);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(comPngFilePath))) {
            if (bitmap.compress(CompressFormat.PNG, 100, bos)) {
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePNG4TransBg(byte[] data, String newImgpath) {
        //复制Bitmap  因为png可以为透明，jpg不支持透明，把透明底明变成白色
        BitmapFactory.Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inDither = true;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        //主要是先创建一张白色图片，然后把原来的绘制至上去
        Bitmap outB = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(outB);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bitmap, 0, 0, null);
        File file = new File(newImgpath);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (outB.compress(CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
