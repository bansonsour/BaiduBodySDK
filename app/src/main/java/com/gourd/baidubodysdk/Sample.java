package com.gourd.baidubodysdk;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 项目名称：BaiduBodySDK
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2019/7/1 15:55
 *
 * @version V1.0
 */
public class Sample {

    //设置APPID/AK/SK
    public static final String APP_ID = "16685706";
    public static final String API_KEY = "TfXsudmKVX8RivKaYazhKs7e";
    public static final String SECRET_KEY = "ibvRUQUCz6dVpEjmROfuVMTTewiv7Nn9";

    public static final String Root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Baidubody" + File.separator;
    public static final String FrescoPath = Root + "fresco";
    public static final String TempPath = Root + "temp";
    private static AipBodyAnalysis client;

    public static void init() {
        if (client == null) {
            client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);
        }
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        File rootDir = new File(Root);
        if (rootDir != null && !rootDir.exists()) {
            rootDir.mkdirs();
        }
        rootDir = new File(FrescoPath);
        if (rootDir != null && !rootDir.exists()) {
            rootDir.mkdirs();
        }
        rootDir = new File(TempPath);
        if (rootDir != null && !rootDir.exists()) {
            rootDir.mkdirs();
        }

    }

    public static AipBodyAnalysis getClient() {
        return client;
    }


    public static void main(String[] args) {
        // 初始化一个AipBodyAnalysis

        AipBodyAnalysis client = getClient();
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
        String path = "test.jpg";
        JSONObject res = client.bodyAnalysis(path, new HashMap<String, String>());
        try {
            System.out.println(res.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void sample() throws JSONException, IOException {

        AipBodyAnalysis client = getClient();
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        //options.put("type", "labelmap");
        options.put("type", "foreground");


        // 参数为本地路径
        String fileName = "scale.jpg";
        String image = Root + fileName;

        JSONObject res = client.bodySeg(image, options);
        if (res != null) {
            System.out.println(res.toString(2));
            String fgBase64 = res.optString("foreground");


            String rawOutFile = Root + "out" + fileName.split("\\.")[0] + ".png";
            String byteOutFile = Root + "byte" + fileName.split("\\.")[0] + ".png";
            String saveOutFile = Root + "save" + fileName.split("\\.")[0] + ".png";
            String compOutFile = Root + "comp" + fileName.split("\\.")[0] + ".png";
            String JpgOutFile = Root + "jpg" + fileName.split("\\.")[0] + ".jpg";

            Util.writeBytesToFileSystem(Base64Util.decode(fgBase64), rawOutFile);
            if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                PngConverter.compressPng(Base64Util.decode(fgBase64), byteOutFile);
                PngConverter.savePNG4TransBg(Base64Util.decode(fgBase64), saveOutFile);
                PngConverter.compressPng(rawOutFile, compOutFile);
                PngConverter.convertToJpg(rawOutFile, JpgOutFile);
            }
        }

        // 参数为二进制数组
       /* byte[] file = Util.readFileByBytes(image);
        res = client.bodySeg(file, options);
        System.out.println(res.toString(2));*/
    }
}
