package com.facemerge.util;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.gourd.baidubodysdk.Sample.Root;

/**
 * @ClassName: ImageUI
 * @description: TODO
 * @author: 小帅丶
 * @create: 2019-05-18
 **/
public class ImageUI {
    private static final long serialVersionUID = 1L;
    private static final String  SavePath = Root;

    private Bitmap image;

    public ImageUI() {
        this.image = null;
    }


    /**
     * 显示图片
     * @param title
     * @param src
     */
    public void imshow(String title, Mat src) {
        this.image = convert2Bitmap(src);
        saveBitmap2Img(image);
    }

    private Bitmap convert2Bitmap(Mat src) {
        Mat result = Mat.zeros(src.rows(), src.cols(), src.type());
        Imgproc.cvtColor(src, result, Imgproc.COLOR_RGB2BGRA);
        Bitmap bmp = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(result, bmp);
        return bmp;
    }

    private void saveBitmap2Img(Bitmap bitmap) {
        File file=new File(SavePath,"FaceMerge1.jpg");
        if(file.exists()){
            file.delete();
        }
        try {
            FileOutputStream outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();//释放bitmap的资源，这是一个不可逆转的操作
    }
}
