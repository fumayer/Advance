package com.newsdemo.dagger.component;

import android.app.Activity;

import com.newsdemo.dagger.module.FragmentModule;
import com.newsdemo.dagger.scope.FragmentScope;
import com.newsdemo.ui.gank.fragment.GirlFragment;
import com.newsdemo.ui.gank.fragment.TechFragment;
import com.newsdemo.ui.gold.fragment.GoldMainFragment;
import com.newsdemo.ui.gold.fragment.GoldPageFragment;
import com.newsdemo.ui.main.fragment.SettingFragment;
import com.newsdemo.ui.vtex.fragment.VtexPagerFragment;
import com.newsdemo.ui.wechat.fragment.WechatMainFragment;
import com.newsdemo.ui.zhihu.fragment.CommentFragment;
import com.newsdemo.ui.zhihu.fragment.DailyFragment;
import com.newsdemo.ui.zhihu.fragment.HotFragment;
import com.newsdemo.ui.zhihu.fragment.SectionFragment;
import com.newsdemo.ui.zhihu.fragment.Themefragment;

import dagger.Component;

/**
 * Created by jianqiang.hu on 2017/5/15.
 */

@FragmentScope
@Component(dependencies = AppComponent.class,modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();

    void inject (DailyFragment dailyFragment);

    void inject(Themefragment themefragment);

    void inject(SectionFragment sectionFragment);

    void inject(HotFragment hotFragment);

    void inject(CommentFragment commentFragment);

    void inject(WechatMainFragment wechatMainFragment);

    void inject(TechFragment techFragment);

    void inject(GirlFragment girlFragment);

    void inject(GoldMainFragment goldMainFragment);

    void inject(GoldPageFragment goldPageFragment);

    void inject(VtexPagerFragment vtexPagerFragment);

    void inject(SettingFragment settingFragment);
}
