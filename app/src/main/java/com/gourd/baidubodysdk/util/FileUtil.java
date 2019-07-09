package com.gourd.baidubodysdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 项目名称：BaiduBodySDK
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2019/7/9 11:22
 *
 * @version V1.0
 */
public class FileUtil {
    /*
     * 获取Asset内的文件夹
     * @param fileName 必须是完整文件名（文件名+格式）
     */
    public static void getFileFromAsset(Context context, String fileName, String saveFilePath) {
        //检查是否存在sd卡
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "请插入sd卡", Toast.LENGTH_LONG).show();
            return;
        }

        InputStream fileStream = null;
        try {
            //获取指定Assets文件流
            fileStream = context.getResources().getAssets().open(fileName);
            //转化为bitmap对象
            Bitmap bitmap = BitmapFactory.decodeStream(fileStream);
            saveInSdCard(bitmap, fileName, saveFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 保存到sb卡内
     * @param fileName 必须是完整文件名（文件名+格式）
     * @param bitmap
     */
    public static void saveInSdCard(Bitmap bitmap, String filename,String saveFilePath) throws IOException {

        /*
         * 在Android中1.5、1.6的sdcard目录为/sdcard，而Android2.0以上都是/mnt/sdcard，
         * 因此如果我们在保存时直接写具体目录会不妥，因此我们可以使用:
         * Environment.getExternalStorageDirectory();获取sdcard目录；
         */
        String directory = saveFilePath;
        File rootFile = new File(directory);
        //如不存在文件夹，则新建文件夹
        if (!rootFile.exists())
            rootFile.mkdir();
        //在文件夹下加入获取的文件
        File file = new File(directory, filename);
        try {
            //文件输出流
            FileOutputStream out = new FileOutputStream(file);
            //bitmp压缩到本地，原图就100
            if (filename.contains("png") || filename.contains(".PNG")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
            } else {
                bitmap.compress(CompressFormat.JPEG, 80, out);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
