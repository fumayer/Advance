package com.newsdemo.dagger.module;

import com.newsdemo.app.App;
import com.newsdemo.model.DataManager;
import com.newsdemo.model.db.DBHelper;
import com.newsdemo.model.db.RealmHelper;
import com.newsdemo.model.http.HttpHelper;
import com.newsdemo.model.http.RetrofitHelper;
import com.newsdemo.model.prefs.ImplPreferencesHelper;
import com.newsdemo.model.prefs.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.producers.Produces;

/**
 * Created by jianqiang.hu on 2017/5/11.
 */
@Module
public class AppModule  {
    private final App application;

    public AppModule(App application){
        this.application=application;
    }

    @Provides
    @Singleton
    App provideApplicationContext(){
        return application;
    }

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(RetrofitHelper retrofitHelper){//参数由RetrofitModule提供
        return retrofitHelper;
    }
    @Provides
    @Singleton
    DBHelper provideDBHelper(RealmHelper realmHelper){//参数通过RealmHelper构造方法的@Inject注入
        return realmHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(ImplPreferencesHelper implPreferencesHelper){//参数通过ImplPreferencesHelper构造方法的@Inject注入
        return implPreferencesHelper;
    }
    @Provides
    @Singleton
    DataManager provideDataManager(HttpHelper httpHelper, DBHelper DBHelper, PreferencesHelper preferencesHelper){
        return new DataManager(httpHelper,DBHelper,preferencesHelper);
    }




}
