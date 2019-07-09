package com.gourd.baidubodysdk.util;

import android.text.TextUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class UrlStringUtils {

    public static final String SUFFIX_JPG = ".jpg";
    public static final String SUFFIX_JPEG = ".jpeg";
    public static final String SUFFIX_PNG = ".png";
    public static final String SUFFIX_GIF = ".gif";

    /**
     * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = truncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        // 每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], URLDecoder.decode(arrSplitEqual[1]));
            } else {
                if (arrSplitEqual[0] != "") {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    public static String truncateUrlPage(String strURL) {
        if (TextUtils.isEmpty(strURL)) {
            return null;
        }
        String strAllParam = null;
        strURL = strURL.trim();
        String[] arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    public enum EImgUrlSize {
        SIZE_64_64, SIZE_120_120, SIZE_300_300,
    }

    /**
     * description 获取对应尺寸的图片
     *
     * @param url
     * @param imgSize
     * @return
     */
    public static String converImageUrl(String url, EImgUrlSize imgSize) {
        if (TextUtils.isEmpty(url) || isAnimatedUrl(url)) {
            return url;
        }

        String retUrl = url;
        int lastIndex = retUrl.lastIndexOf("/");
        if (lastIndex >= 0 && lastIndex < retUrl.length() - 1) {
            String name = url.substring(lastIndex + 1);
            switch (imgSize) {
                case SIZE_64_64:
                    retUrl = url.substring(0, lastIndex + 1) + "64_64_" + name;
                    break;
                case SIZE_120_120:
                    retUrl = url.substring(0, lastIndex + 1) + "120_120_" + name;
                    break;
                case SIZE_300_300:
                    if (name.startsWith("300_300_")) {      //不要问我为什么要加上这句，因为要兼容iOS的一个bug，导致了图片无法显示
                        retUrl = url;
                    } else {
                        retUrl = url.substring(0, lastIndex + 1) + "300_300_" + name;
                    }
                    break;
            }
        }
        return retUrl;
    }

    public static boolean isAnimatedUrl(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return false;
        }

        String retUrl = imgUrl;
        int lastIndex = retUrl.lastIndexOf("/");
        if (lastIndex >= 0 && lastIndex < retUrl.length() - 1) {
            String name = imgUrl.substring(lastIndex + 1);
            name = name.toLowerCase();
            if (name.contains(".gif") || name.contains(".webp")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取path中的文件后缀名，包含" ."
     *
     * @param path 包含文件后缀名，且以后缀名结尾的字符串
     * @return 返回如.png 字符串(如果path为null或者"",则返回.bak)
     */
    public static String suffix(String path) {
        return suffix(path, ".bak");
    }
    public static String suffix(String path, String defaultSuffix) {
        if (!TextUtils.isEmpty(path)) {
            int suffixIndex = path.lastIndexOf(".");
            if (suffixIndex > 0 && suffixIndex < path.length() - 1) {
                String suffix = path.substring(suffixIndex);
                String lowerCaseSuffix = suffix.toLowerCase();
                if (lowerCaseSuffix.contains(SUFFIX_JPG)) {
                    return SUFFIX_JPG;
                } else if (lowerCaseSuffix.contains(SUFFIX_JPEG)) {
                    return SUFFIX_JPEG;
                } else if (lowerCaseSuffix.contains(SUFFIX_PNG)) {
                    return SUFFIX_PNG;
                } else if (lowerCaseSuffix.contains(SUFFIX_GIF)) {
                    return SUFFIX_GIF;
                }
            }
        }
        return defaultSuffix;
    }

    /**
     * 获取url中的文件名称(不含后缀)
     *
     * @param url
     * @return
     */
    public static String getNameFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int nameIndex = url.lastIndexOf("/") + 1;
            int suffixIndex = url.lastIndexOf(".");
            if (nameIndex >= 0 && suffixIndex >= 0 && suffixIndex < url.length() - 1 && nameIndex < suffixIndex) {
                return url.substring(nameIndex, suffixIndex);
            }
        }

        return "";
    }

    public static String getFullNameFromUrl(String url) {
        String name = getNameFromUrl(url);
        if ("".equals(name)) {
            return name;
        }

        String suffix = suffix(url);

        return name + suffix;
    }

    public static String getVideoFileFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int nameIndex = url.lastIndexOf("/") + 1;
            if (nameIndex >= 0) {
                return url.substring(nameIndex);
            }
        }
        return Long.toString(System.currentTimeMillis()) + ".mp4";//返回时间作为名称
    }

    /**
     * 对形如xxxxx.jpg?w=540&h=960的path截取长宽标记 ? 之前的字符串
     *
     * @param path
     * @return
     */
    public static String originalPurePath(String path) {
        if (!TextUtils.isEmpty(path)) {
            int lastIndex = path.lastIndexOf("?");
            if (lastIndex > 0) {
                return path.substring(0, lastIndex);
            }
        }

        return path;
    }

    public static String converImageUrl4RealSize(String url) {
        if (url != null && url.length() > 0) {
            url = url.replace("/120_120_", "/");
            url = url.replace("/300_300_", "/");
            url = url.replace("/64_64_", "/");
        }
        return url;
    }
}
