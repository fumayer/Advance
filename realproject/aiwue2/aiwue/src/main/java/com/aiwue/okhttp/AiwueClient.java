package com.aiwue.okhttp;


import android.content.Intent;
import android.text.TextUtils;

import com.aiwue.BuildConfig;
import com.aiwue.base.BaseActivity;
import com.aiwue.controller.UserController;
import com.aiwue.model.Article;
import com.aiwue.model.Banner;
import com.aiwue.model.Comment;
import com.aiwue.model.Note;
import com.aiwue.model.NullResult;
import com.aiwue.model.RecommendFriends;
import com.aiwue.model.User;
import com.aiwue.model.VCodeResult;
import com.aiwue.model.requestParams.GetBannerParams;
import com.aiwue.model.requestParams.GetNoteListParams;
import com.aiwue.model.requestParams.GetRandomArticleListParams;
import com.aiwue.model.requestParams.GetRecFriendsListParams;
import com.aiwue.ui.activity.BaseContentActivity;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.aiwue.base.AiwueConfig.AIWUE_BASE_URL;
import static com.aiwue.base.AiwueConfig.APP_KEY;

/**
 * okhttp客户端单例
 */
public class AiwueClient {
    private static final long DEFAULT_TIMEOUT = 20000;
    private static final long READ_TIMEOUT = 120000;
    private static final long WRITE_TIMEOUT = 120000;
//    public static Retrofit mRetrofit;
    private static AiwueClient s_instance;
    private AiwueService aiwueService;
    private static boolean hasFullHeader = false;

    private AiwueClient(String baseUrl) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request.Builder builder = chain.request().newBuilder();

                builder.addHeader("appkey", APP_KEY);
                if (UserController.getInstance().isLogined()) {
                    builder.addHeader("userId", UserController.getInstance().getUserId()+"");
                    builder.addHeader("accessToken", UserController.getInstance().getToken());
                }
                Request request= builder.build();

                if(UserController.getInstance().getUserId() != null && !TextUtils.isEmpty(UserController.getInstance().getToken())) {
                    hasFullHeader = true;
                }
                return chain.proceed(request);
            }
        });

        try {
//头条的服务器配置
//            httpClientBuilder.addInterceptor(new Interceptor() {
//                @Override
//                public okhttp3.Response intercept(Chain chain) throws IOException {
//                    Request.Builder builder = chain.request().newBuilder();
//                    builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547");
//                    builder.addHeader("Cache-Control", "max-age=0");
//                    builder.addHeader("Upgrade-Insecure-Requests", "1");
//                    builder.addHeader("X-Requested-With", "XMLHttpRequest");
//                    builder.addHeader("Cookie", "uuid=\"w:f2e0e469165542f8a3960f67cb354026\"; __tasessionId=4p6q77g6q1479458262778; csrftoken=7de2dd812d513441f85cf8272f015ce5; tt_webid=36385357187");
//                    return chain.proceed(builder.build());
//                }
//            });
//            OkHttpClient okHttpClient = httpClientBuilder.build();
//            mRetrofit = new Retrofit.Builder()
//                    .baseUrl(AiwueService.API_SERVER_URL)
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .client(okHttpClient)
//                    .build();
            //爱武艺服务器配置
            Retrofit retrofit = new Retrofit.Builder()
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
            aiwueService = retrofit.create(AiwueService.class);
        } catch (IllegalArgumentException e) {
            Timber.e(e);
        }
    }

    private static String getBaseUrl() {
        return AIWUE_BASE_URL;
    }

    public static AiwueService getInstance() {
        if (s_instance == null || s_instance.aiwueService == null || !hasFullHeader) {
            String baseUrl = getBaseUrl();
            if (!TextUtils.isEmpty(baseUrl)) {
                s_instance = new AiwueClient(baseUrl);
            } else {
                return null;
            }
        }
        return s_instance.aiwueService;
    }

    /**
     * @param isOnlyString 是否只解析字符串格式数据
     * @return
     */
//    public static Retrofit retrofit() {
//        if (mRetrofit == null) {
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            if (BuildConfig.DEBUG) {
//                // Log信息拦截器
//                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                //设置 Debug Log 模式
//                builder.addInterceptor(loggingInterceptor);
//            }
//
//            builder.addInterceptor(new Interceptor() {
//                @Override
//                public okhttp3.Response intercept(Chain chain) throws IOException {
//                    Request.Builder builder = chain.request().newBuilder();
//                    builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547");
//                    builder.addHeader("Cache-Control", "max-age=0");
//                    builder.addHeader("Upgrade-Insecure-Requests", "1");
//                    builder.addHeader("X-Requested-With", "XMLHttpRequest");
//                    builder.addHeader("Cookie", "uuid=\"w:f2e0e469165542f8a3960f67cb354026\"; __tasessionId=4p6q77g6q1479458262778; csrftoken=7de2dd812d513441f85cf8272f015ce5; tt_webid=36385357187");
//                    return chain.proceed(builder.build());
//                }
//            });
//
//            OkHttpClient okHttpClient = builder.build();
//            mRetrofit = new Retrofit.Builder()
//                    .baseUrl(AiwueService.API_SERVER_URL)
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .client(okHttpClient)
//                    .build();
//        }
//        return mRetrofit;
//    }


    public AiwueService getAiwueService() {
        return aiwueService;
    }

    private static void bindActivity(BaseActivity activity,
                                     BaseActivity.LifeCycleEvent event,
                                     final Subscription subscription) {
        if (getInstance() == null || subscription == null) return;

        if (activity != null) {
            activity.bindingEvent(subscription, event);
        }
    }

    private static Subscription doSubscribe(rx.Observable observable,
                                            Scheduler subscribeScheduler,
                                            Scheduler observeScheduler,
                                            BaseCallBack subscriber) {
        if (observable == null || subscriber == null) {
            return null;
        }

        if (subscribeScheduler != null) observable = observable.subscribeOn(subscribeScheduler);
        if (observeScheduler != null) observable = observable.observeOn(observeScheduler);

        return observable.subscribe(subscriber);
    }



    /*具体网络接口函数 start*/


    //爱武艺
    //获取验证码
    public static void sendVCode(String destionation, int operation,
                                 SubscriberCallBack<VCodeResult> callback) {
        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().sendVCode(destionation,operation), Schedulers.io(), AndroidSchedulers.mainThread(), callback);
        bindActivity(null, null, subscription);
    }
    //验证码登录
    public static void loginWithVCode(String mobile, int verifyId, String verifyCode, String deviceName,
                                      SubscriberCallBack<User>  callback) {
        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().loginWithVCode(mobile, verifyId, verifyCode,deviceName), Schedulers.io(), AndroidSchedulers.mainThread(), callback);
        bindActivity(null, null, subscription);
    }
    //账号密码登录
    public static void loginWithNormal(String account, String password,String ipAddr,
                                      SubscriberCallBack<User>  callback) {
        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().loginWithNormal(account, password, ipAddr), Schedulers.io(), AndroidSchedulers.mainThread(), callback);
        bindActivity(null, null, subscription);
    }
    //修改密码
    public static void resetPassword(String newpwd,
                                       SubscriberCallBack<NullResult>  callback) {
        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().resetPassword(newpwd), Schedulers.io(), AndroidSchedulers.mainThread(), callback);
        bindActivity(null, null, subscription);
    }
    //第三方登录
    public static void thirdPartyLogin(String openId, String openAccessToken, Integer type, String ipAddr,
                                       SubscriberCallBack<User> callback) {

        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().thirdPartyLogin(openId, type, openAccessToken, ipAddr), Schedulers.io(), AndroidSchedulers.mainThread(), callback);
        bindActivity(null, null, subscription);
    }
    //返回登录用户信息
    public static void getUserInfo(SubscriberCallBack<User> callback) {
        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().getUserInfo(), Schedulers.io(), AndroidSchedulers.mainThread(), callback);
        bindActivity(null, null, subscription);
    }
    //返回其他用户信息
    public static void getOtherUserInfo(Integer oUserId, String nickName, SubscriberCallBack<User> callback) {
        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().getOtherUserInfo(oUserId, nickName), Schedulers.io(), AndroidSchedulers.mainThread(), callback);
        bindActivity(null, null, subscription);
    }

    //获取随机文章列表
    public static void getRandomArticleList(BaseActivity activity,
                                       BaseActivity.LifeCycleEvent event,
                                       Scheduler subscribeScheduler,
                                       Scheduler observeScheduler,
                                       GetRandomArticleListParams params,
                                       SubscriberCallBack<List<Article>> callback) {

        if (getInstance() == null) return;
        Map<String, String> mm = params.getMapParams();
        Subscription subscription = doSubscribe(getInstance().getRandomArticleList(mm), subscribeScheduler, observeScheduler, callback);
        bindActivity(activity, event, subscription);
    }
    //获取文章详情
    public static void getArticleDetail(BaseActivity activity,
                                            BaseActivity.LifeCycleEvent event,
                                            Scheduler subscribeScheduler,
                                            Scheduler observeScheduler,
                                            Integer articleId,
                                            SubscriberCallBack<Article> callback) {

        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().getArticleDetail(articleId), subscribeScheduler, observeScheduler, callback);
        bindActivity(activity, event, subscription);
    }
    //获得动态（帖子）详情
    public static void getNoteDetail(BaseActivity activity,
                                     BaseActivity.LifeCycleEvent event,
                                     Scheduler subscribeScheduler,
                                     Scheduler observeScheduler,
                                     Integer articleId,
                                     SubscriberCallBack<Article> callback) {
        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().getDynaimicDetail(articleId), subscribeScheduler, observeScheduler, callback);
        bindActivity(activity, event, subscription);
    }

    //获取评论列表
    public static void getCommentList(BaseActivity activity,
                                      BaseActivity.LifeCycleEvent event,
                                      Scheduler subscribeScheduler,
                                      Scheduler observeScheduler,
                                      Integer contentType, Integer conentId, int page, int size,
                                      SubscriberCallBack<List<Comment>> callback) {

        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().getCommentList(contentType,conentId, page,size), subscribeScheduler, observeScheduler, callback);
        bindActivity(activity, event, subscription);
    }

    //获取笔记列表
    public static void getNoteList(BaseActivity activity,
                                   BaseActivity.LifeCycleEvent event,
                                   Scheduler subscribeScheduler,
                                   Scheduler observeScheduler,
                                   GetNoteListParams params,
                                   SubscriberCallBack<List<Note>> callback) {

        if (getInstance() == null) return;
        Map<String, String> mm = params.getMapParams();
        Subscription subscription = doSubscribe(getInstance().getNoteList(mm), subscribeScheduler, observeScheduler, callback);
        bindActivity(activity, event, subscription);
    }
    //获取banner列表
    public static void getBannerList(BaseActivity activity,
                                     BaseActivity.LifeCycleEvent event,
                                     Scheduler subscribeScheduler,
                                     Scheduler observeScheduler,
                                     Integer type,
                                     SubscriberCallBack<List<Banner>> callback) {
        if (getInstance() == null) return;
        Subscription subscription = doSubscribe(getInstance().getBannerList(type), subscribeScheduler, observeScheduler, callback);
        bindActivity(activity, event, subscription);
    }
    //获取推荐武友列表
    public static void getRecFriendsList(BaseActivity activity,
                                            BaseActivity.LifeCycleEvent event,
                                            Scheduler subscribeScheduler,
                                            Scheduler observeScheduler,
                                            GetRecFriendsListParams params,
                                            SubscriberCallBack<List<RecommendFriends>> callback) {

        if (getInstance() == null) return;
        Map<String, String> mm = params.getMapParams();

        Subscription subscription = doSubscribe(getInstance().getRecFriendsList(mm), subscribeScheduler, observeScheduler, callback);
        bindActivity(activity, event, subscription);
    }


    /*具体网络接口函数 end*/
}
