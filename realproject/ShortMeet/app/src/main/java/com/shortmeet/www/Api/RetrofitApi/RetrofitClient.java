package com.shortmeet.www.Api.RetrofitApi;


import com.shortmeet.www.Api.ApiConstant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Fenglingyue on 2017/8/20.
 */

public class RetrofitClient {
    private volatile static RetrofitClient mRetroClientInstance;
   // private  OkHttpClient client;
    private  Retrofit retrofit;
    private static NetInterface netInter;
    private  RetrofitClient(){ };

    public static  RetrofitClient  getRetroClientInstance(){
        if(mRetroClientInstance==null){
            synchronized (RetrofitClient.class){
                if(mRetroClientInstance==null){
                    mRetroClientInstance=new RetrofitClient();
                }
            }
        }
        return  mRetroClientInstance;
    }

    public void initRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_URR)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create());
           //   client = OkHttpUtils.getInstance().getOkHttpClient();
          //   retrofit = builder.client(client).build();
               retrofit = builder.build();
    }
    public  NetInterface createRetroApi() {
        if (netInter == null) {
            netInter = createNetInterApi(NetInterface.class);
        }
        return netInter;
    }
    public  <T> T createNetInterApi(Class<T> clz) {
        return (T) retrofit.create(clz);
    }
}
