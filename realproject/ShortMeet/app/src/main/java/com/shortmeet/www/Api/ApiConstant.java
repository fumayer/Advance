package com.shortmeet.www.Api;

import android.os.Environment;

import java.io.File;

/**
 * Created by Fenglingyue on 2017/8/20.
 */

public interface ApiConstant {
    //根地址
//    String BASE_URR = "http://124.65.241.210:2000/Api/";
   //  String BASE_URR = "http://192.168.0.70/api.shotmeet.com/";
     String BASE_URR="http://api.shortmeet.com.cn:2201/";
     //李伟
//    String BASE_URR = "http://192.168.0.79/api.shortmeet.com/";
     //  String BASE_URL ="http://192.168.0.70:80/api.shortmeet.com/User/";
     // String BASE_URL ="//192.168.0.70:80/api.shortmeet.com/UserSet/";
     // String BASE_URL ="192.168.0.70:80/api.shortmeet.com/Video/";

    /**
     * 阿里初始化的三个字符串
     */
    public static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    public static final String accessKeyId = "LTAI5OVZ5B5hdFQf";
    public static final String accessKeySecret = "6DWFkueZjE40tmKp3IHmzi5H1cXCex";
    //阿里上传需要的buketName
    public static final String BUCKETNAME = "tlgshortmeeetspace";  //正式


    // Fly 注：apk 更新
    public static final String DOWNLOAD_APK_URL = "download_apk_url";
    public static final String DOWNLOAD_APK_VERSION_CODE = "download_apk_version_code";


   public static final String COMPOSE_PATH= Environment.getExternalStorageDirectory()+ File.separator + "shortmeet/";
    public static final String COMPOSE_PATH_EDITOR= Environment.getExternalStorageDirectory()+ File.separator + "shortmeet/editor/";
    public static final String COMPOSE_PATH_CROP= Environment.getExternalStorageDirectory()+ File.separator + "shortmeet/crop/";
    public static final String COMPOSE_PATH_COVER= Environment.getExternalStorageDirectory()+ File.separator + "shortmeet/cover/";
    public static final String COMPOSE_PAYH_DRAFT=Environment.getExternalStorageDirectory()+ File.separator + "shortmeet/draft/";

}
