//package com.aiwue.controller;
//
//import com.aiwue.framework.AiwueLog;
//import com.aiwue.utils.NetUtil;
//import com.aiwue.utils.AiwueConfig;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by nci on 2016/9/2.
// */
//public class MyFavoritesController {
//    protected static final String TAG = "MyFavoritesController";
//
//    public static void getMyFavoritesListFromNetwork() {
//        Map<String, String> params = new HashMap<>();
//        params.put(ParamNames.INDEX, "0");
//        params.put(ParamNames.SIZE, "20");
//
//        NetUtil.loadData(AiwueConfig.GET_FAVORITE_ARTICLE_LIST, NetUtil.METHOD_GET, params, new NetUtil.ResponseCallback() {
//
//
//            @Override
//            public void onResponse(String result) {
//                AiwueLog.d(TAG, "getPersonalDataFromNetwork onResponse result:" + result);
//            }
//
//            @Override
//            public void onFailed(String errorInfo) {
//                AiwueLog.e(TAG, "getPersonalDataFromNetwork onFailed errorInfo:" + errorInfo);
//            }
//        });
//    }
//
//    private static class ParamNames {
//        public static final String APP_KEY = "appKey";
//        public static final String ACCESS_TOKEN = "accessToken";
//        public static final String USER_ID = "userId";
//        public static final String TYPE = "Type";
//        public static final String INDEX = "pIndex";
//        public static final String SIZE = "pSize";
//    }
//}
