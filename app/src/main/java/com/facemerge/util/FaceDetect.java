package com.facemerge.util;

import com.facemerge.bean.FaceV3DetectBean;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;

import org.json.JSONObject;
import org.opencv.core.Point;

import java.util.HashMap;

/**
 * @ClassName: FaceDetect
 * @description: 人脸检测接口返回处理方法
 * @author: 小帅丶
 * @create: 2019-05-18
 **/
public class FaceDetect {
    private static AipFace aipFace;
    //请填写自己应用的appid apikey secretkey
    //设置APPID/AK/SK
    public static final String APP_ID = "16685706";
    public static final String API_KEY = "TfXsudmKVX8RivKaYazhKs7e";
    public static final String SECRET_KEY = "ibvRUQUCz6dVpEjmROfuVMTTewiv7Nn9";

    static {
        aipFace = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    }

    /**
     * 人脸检测并返回72关键点
     *
     * @param path 图片路径
     * @return org.opencv.core.Point[]
     * @author 小帅丶
     * @date 2019/5/18
     **/
    public static Point[] detect(String path) {
        Point[] points;
        try {
            HashMap<String, String> options = new HashMap<String, String>();
            String image = Base64Util.encode(Util.readFileByBytes(path));
            String type = "BASE64";
            //options.put("face_field", "age,beauty,expression,face_shape,gender,glasses,landmark,landmark150,race,quality,eye_status,emotion,face_type");
            //options.put("face_field", "age,beauty,face_shape,landmark,landmark150,face_type");

            options.put("face_field", "beauty,landmark72,landmark");
            options.put("max_face_num", "2");
            options.put("face_type", "LIVE");
            options.put("liveness_control", "NONE");

            JSONObject jsonObject = aipFace.detect(image, type, options);
            System.out.println("aipFace.detect Json-->" + jsonObject != null? jsonObject.toString():"jsonObject is null");
            com.alibaba.fastjson.JSON object = com.alibaba.fastjson.JSON.parseObject(jsonObject.toString());
            FaceV3DetectBean bean = com.alibaba.fastjson.JSONObject.toJavaObject(object, FaceV3DetectBean.class);
            int k72 = bean.getResult().getFace_list().get(0).getLandmark72().size();
            points = new Point[k72];
            for (int i = 0; i < k72; i++) {
                double x = bean.getResult().getFace_list().get(0).getLandmark72().get(i).getX();
                double y = bean.getResult().getFace_list().get(0).getLandmark72().get(i).getY();
                points[i] = new Point(x, y);
            }
            return points;
        } catch (Exception e) {
            System.out.println("错误了" + e.getMessage());
            return null;
        }
    }
}
