package com.care;

import com.care.domain.User;

import java.math.BigDecimal;

/**
 * Created by nujian on 16/2/17.
 */
public class Constants {

    public static final ThreadLocal<Integer> RESPONSE_CODE_THREAD_LOCAL = new ThreadLocal<Integer>();

    public static final ThreadLocal<User> CURRENT_USER_THREAD_CACHE = new ThreadLocal<User>();

    public static final ThreadLocal<String> HEADER_SECURITY_TOKEN_THREAD_LOCAL = new ThreadLocal<String>();

    public static final Class[] EXCLUDE_NOTIFY_EXPCETIONS = new Class[]{};

    public static final BigDecimal INIT_COST = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_DOWN);

    public static final String USER_LOGIN_TOKEN = "USER_LOGIN_TOKEN";

    public static final String BAIDU_MAP_PATH = "http://api.map.baidu.com/staticimage/v2";

    public static String baidu_map_key;

    public static String upload_base_dir;

    public static Integer ORDER_ADDRESS_PIC_WIDTH = 400;

    public static Integer ORDER_ADDRESS_PIC_HEIGHT = 400;

    public static Integer ORDER_ADDRESS_PIC_ZOOM = 17;

    public static void setUploadBaseDir(String uploadBaseDir) {
        Constants.upload_base_dir = uploadBaseDir;
    }

    public static String images_access_baseurl;

    public static void setImagesAccessBaseUrl(String imagesAccessBaseUrl) {
        Constants.images_access_baseurl = imagesAccessBaseUrl;
    }

    public static void setBaiduMapKey(String baiduMapKey) {
        Constants.baidu_map_key = baiduMapKey;
    }
}


