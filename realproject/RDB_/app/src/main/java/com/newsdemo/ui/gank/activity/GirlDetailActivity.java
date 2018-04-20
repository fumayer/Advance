package com.newsdemo.ui.gank.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.newsdemo.R;
import com.newsdemo.app.App;
import com.newsdemo.app.Constants;
import com.newsdemo.base.SimpleActivity;
import com.newsdemo.model.bean.RealmLikeBean;
import com.newsdemo.model.db.RealmHelper;
import com.newsdemo.util.RxUtil;
import com.newsdemo.util.ShareUtil;
import com.newsdemo.util.SnackbarUtil;
import com.newsdemo.util.SystemUtil;
import com.newsdemo.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by jianqiang.hu on 2017/5/25.
 */

public class GirlDetailActivity extends SimpleActivity{

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.iv_girl_detail)
    ImageView ivGirlDetail;

    private static int ACTION_SAVE = 0x00;
    private static int ACTION_SHARE = 0x01;


    String url;
    String id;

    boolean isLiked;

    private Bitmap bitmap;
    private PhotoViewAttacher mAttacher;
    MenuItem meunItem;
    RxPermissions rxPermissions;
    private RealmHelper mRealmHelper;
    @Override
    protected int getLayout() {
        return R.layout.activity_girl_detail;
    }

    @Override
    protected void initEventAndData() {
        setToolBar(toolbar,"");
        mRealmHelper= App.getAppComponent().getRealmHelper();
        Intent intent=getIntent();
        url=intent.getExtras().getString(Constants.IT_GANK_GRIL_URL);
        id=intent.getExtras().getString(Constants.IT_GANK_GRIL_ID);
        if (url!=null){
            Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    bitmap=resource;
                    ivGirlDetail.setImageBitmap(resource);
                    mAttacher=new PhotoViewAttacher(ivGirlDetail);
                }
            });
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.girl_menu,menu);
        meunItem = menu.findItem(R.id.action_like);
        setLikeState(mRealmHelper.queryLikeId(id));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_like:
                if (isLiked){
                    item.setIcon(R.mipmap.ic_toolbar_like_n);
                    mRealmHelper.deleteLikeBean(id);
                }else{
                    item.setIcon(R.mipmap.ic_toolbar_like_p);
                    RealmLikeBean bean = new RealmLikeBean();
                    bean.setId(id);
                    bean.setImage(url);
                    bean.setType(Constants.TYPE_GIRL);
                    bean.setTime(System.currentTimeMillis());
                    mRealmHelper.insertLikeBean(bean);
                }
                break;
            case R.id.action_save:
                break;
            case R.id.action_share:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLikeState(boolean state){
        if (state){
            meunItem.setIcon(R.mipmap.ic_toolbar_like_p);
            isLiked=true;
        }else{
            meunItem.setIcon(R.mipmap.ic_toolbar_like_n);
            isLiked=false;
        }
    }

    private void checkPermissionAndAction(final int action){
        if (rxPermissions==null){
            rxPermissions=new RxPermissions(this);
        }

        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                            if (aBoolean){
                                if (action==ACTION_SAVE){
                                    SystemUtil.saveBitmapToFile(mContext,url,bitmap,ivGirlDetail,false);
                                }else if (action==ACTION_SHARE){
                                    ShareUtil.shareImage(mContext,SystemUtil.saveBitmapToFile(mContext,url,bitmap,ivGirlDetail,true),"分享一只妹纸");
                                }
                            }else{
                                ToastUtil.show("获取写入权限失败",ToastUtil.LENGTH_SHORT);
                            }
                        }
                    });


    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount()>1){
            pop();
        }else{
            finishAfterTransition();
        }
    }
}
