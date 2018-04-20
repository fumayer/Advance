package com.newsdemo.ui.main.activity;


import android.content.DialogInterface;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.newsdemo.R;
import com.newsdemo.app.App;
import com.newsdemo.app.Constants;
import com.newsdemo.base.BaseActivity;
import com.newsdemo.base.contract.main.MainContract;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.event.SearchEvent;
import com.newsdemo.presenter.main.MainPresenter;
import com.newsdemo.ui.gank.fragment.GankMainFragment;
import com.newsdemo.ui.gold.fragment.GoldMainFragment;
import com.newsdemo.ui.main.fragment.AboutFragment;
import com.newsdemo.ui.main.fragment.LikeFragment;
import com.newsdemo.ui.main.fragment.SettingFragment;
import com.newsdemo.ui.vtex.fragment.VtexMainFragment;
import com.newsdemo.ui.wechat.fragment.WechatMainFragment;
import com.newsdemo.ui.zhihu.fragment.ZhihuMainFragment;
import com.newsdemo.util.SystemUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by jianqiang.hu on 2017/5/11.
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View{

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.navigation)
    NavigationView mNavigationView;

    @BindView(R.id.view_search)
    MaterialSearchView mSearchView;


    ZhihuMainFragment mZhihuMainFragment;
    GankMainFragment mGankMainFragment;
    WechatMainFragment mWechatMainFragment;
    GoldMainFragment mGoldMainFragment;
    VtexMainFragment mVtexMainFragment;
    LikeFragment mLikeFragment;
    SettingFragment mSettingFragment;
    AboutFragment mAboutFragment;

    private int hideFragment= Constants.TYPE_ZHIHU;
    private int showFragment=Constants.TYPE_ZHIHU;


    MenuItem mLastMenuItem;
    MenuItem mSearchMenuItem;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
       return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            mPresenter.setNightModeState(false);
        }else{
            showFragment=mPresenter.getCurrentItem();
            hideFragment=Constants.TYPE_ZHIHU;
            showHideFragment(getTargetFragment(showFragment),getTargetFragment(hideFragment));
            mNavigationView.getMenu().findItem(R.id.drawer_zhihu).setChecked(false);
            mToolbar.setTitle(mNavigationView.getMenu().findItem(getCurrentItem(showFragment)).getTitle().toString());
            hideFragment=showFragment;
        }
    }
    @Override
    protected void initEventAndData() {
        setToolBar(mToolbar,"知乎日报");
        mZhihuMainFragment = new ZhihuMainFragment();
        mGankMainFragment = new GankMainFragment();
        mWechatMainFragment = new WechatMainFragment();
        mGoldMainFragment = new GoldMainFragment();
        mVtexMainFragment = new VtexMainFragment();
        mLikeFragment = new LikeFragment();
        mSettingFragment = new SettingFragment();
        mAboutFragment = new AboutFragment();

        //toolbar绑定drawer
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mLastMenuItem=mNavigationView.getMenu().findItem(R.id.drawer_zhihu);
        loadMultipleRootFragment(R.id.fl_main_content,0,mZhihuMainFragment,mWechatMainFragment,mGankMainFragment,mGoldMainFragment,mVtexMainFragment,mLikeFragment,mSettingFragment,mAboutFragment);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_zhihu:
                        showFragment=Constants.TYPE_ZHIHU;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_gank:
                        showFragment=Constants.TYPE_GANK;
                        mSearchMenuItem.setVisible(true);
                        break;
                    case R.id.drawer_wechat:
                        showFragment = Constants.TYPE_WECHAT;
                        mSearchMenuItem.setVisible(true);
                        break;
                    case R.id.drawer_gold:
                        showFragment = Constants.TYPE_GOLD;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_vtex:
                        showFragment = Constants.TYPE_VTEX;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_setting:
                        showFragment = Constants.TYPE_SETTING;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_like:
                        showFragment = Constants.TYPE_LIKE;
                        mSearchMenuItem.setVisible(false);
                        break;
                    case R.id.drawer_about:
                        showFragment = Constants.TYPE_ABOUT;
                        mSearchMenuItem.setVisible(false);
                        break;
                }

                if (mLastMenuItem!=null){
                    mLastMenuItem.setChecked(false);
                }
                mLastMenuItem=item;
                mPresenter.setCurrentItem(showFragment);
                item.setChecked(true);
                mToolbar.setTitle(item.getTitle());
                mDrawerLayout.closeDrawers();
                showHideFragment(getTargetFragment(showFragment),getTargetFragment(hideFragment));
                hideFragment=showFragment;
                return true;
            }
        });
        //设置search
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (showFragment==Constants.TYPE_GANK){
                    mGankMainFragment.doSearch(query);
                }else if(showFragment==Constants.TYPE_WECHAT){
                    RxBus.getDefault().post(new SearchEvent(query,Constants.TYPE_WECHAT));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //如果是wifi
        if (!mPresenter.getVersionPoint()&& SystemUtil.isWifiConnected()){
            mPresenter.setVersionPoint(true);
        }

        //检查更新
     /*   try {
            PackageManager pm = getPackageManager();
            PackageInfo pi=pm.getPackageInfo(getPackageName(),PackageManager.GET_ACTIVITIES);
            String versionName=pi.versionName;
            mPresenter.checkVersion(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        item.setVisible(false);
        mSearchView.setMenuItem(item);
        mSearchMenuItem=item;
        return true;
    }

    @Override
    public void onBackPressedSupport() {
        if (mSearchView.isSearchOpen()){
            mSearchView.closeSearch();
        }else{
           showExitDialog();
        }
    }

    private void showExitDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定退出吗？");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                App.getInstance().exitApp();
            }
        });
        builder.show();
    }


    private int getCurrentItem(int item){
        switch (item){
            case Constants.TYPE_ZHIHU:
                return R.id.drawer_zhihu;
            case Constants.TYPE_GANK:
                return R.id.drawer_gank;
            case Constants.TYPE_WECHAT:
                return R.id.drawer_wechat;
            case Constants.TYPE_GOLD:
                return R.id.drawer_gold;
            case Constants.TYPE_VTEX:
                return R.id.drawer_vtex;
            case Constants.TYPE_LIKE:
                return R.id.drawer_like;
            case Constants.TYPE_SETTING:
                return R.id.drawer_setting;
            case Constants.TYPE_ABOUT:
                return R.id.drawer_about;
        }
        return R.id.drawer_zhihu;
    }


    private SupportFragment getTargetFragment(int item){
        switch (item){
            case Constants.TYPE_ZHIHU:
                return mZhihuMainFragment;
            case Constants.TYPE_GANK:
                return mGankMainFragment;
            case Constants.TYPE_WECHAT:
                return mWechatMainFragment;
            case Constants.TYPE_GOLD:
                return mGoldMainFragment;
            case Constants.TYPE_VTEX:
                return mVtexMainFragment;
            case Constants.TYPE_LIKE:
                return mLikeFragment;
            case Constants.TYPE_SETTING:
                return mSettingFragment;
            case Constants.TYPE_ABOUT:
                return mAboutFragment;
        }
        return mZhihuMainFragment;
    }



    @Override
    public void showUpdateDialog(String versionContent) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("检测到新版本!");
        builder.setMessage(versionContent);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkPermissions();
            }
        });
        builder.show();
    }

    @Override
    public void startDownloadService() {

    }

    public void checkPermissions(){
        mPresenter.checkPermissions(new RxPermissions(this));
    }

}
