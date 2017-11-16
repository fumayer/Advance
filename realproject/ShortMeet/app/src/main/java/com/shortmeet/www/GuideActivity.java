package com.shortmeet.www;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shortmeet.www.Base.BaseActivity;
import com.shortmeet.www.bean.personalCenter.CreateVisitorBean;
import com.shortmeet.www.entity.percenalCenter.CreateVisitorEntity;
import com.shortmeet.www.mvp.IMVPView;
import com.shortmeet.www.mvp.IMVPrecenter;
import com.shortmeet.www.utilsUsed.LogUtils;
import com.shortmeet.www.utilsUsed.StatusBarUtil;
import com.shortmeet.www.utilsUsed.UserUtils;

public class GuideActivity extends BaseActivity implements IMVPView {
    private IndicatorViewPager indicatorViewPager;
    private ViewPager guideVp;
    private FixedIndicatorView guideIndaca;
    private LayoutInflater mLayoutInflater;
    private int[]images={R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4};
    /*
     *  Fly 注：IMVPrecenter
     */
    private IMVPrecenter   mPrecenter;
    /*
     *  Fly 注：请求bean
     */
   private  CreateVisitorBean bean;
    /*
     *  Fly 注：控制 点击最后一张Splash图片 能否跳转主页
     */
    private boolean canSkip;
    @Override
    public int setRootView() {
        return R.layout.activity_guide;
    }

    public void initView(){
        StatusBarUtil.translucentStatusBar(this, false);//设置状态栏透明 内容延伸到状态栏
        mLayoutInflater=LayoutInflater.from(this);
        guideVp = (ViewPager) findViewById(R.id.guide_vp);
        guideIndaca = (FixedIndicatorView) findViewById(R.id.guide_indaca);
        indicatorViewPager =new IndicatorViewPager(guideIndaca,guideVp);
        indicatorViewPager.setAdapter(mAdapter);
        mPrecenter=new IMVPrecenter(this);
        showRestarDig();
    }

    @Override
    public void initListener() { }
    @Override
    public void initData() {
        //创建 游客
        bean=new CreateVisitorBean();
        bean.setUser_sub_type(1);
        mPrecenter.createYk(bean);
      //  mPrecenter.doLoginInter(new LoginBean());
    }

    private IndicatorViewPager.IndicatorPagerAdapter mAdapter=new IndicatorViewPager.IndicatorViewPagerAdapter() {
        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if(convertView==null){
                convertView=mLayoutInflater.inflate(R.layout.item_guide_tab_roundindicator,container,false);
            }
            return convertView;
        }

        @Override
        public View getViewForPage(final int position, View convertView, ViewGroup container) {
            if(convertView==null){
                convertView=new View(getApplicationContext());
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            convertView.setBackgroundResource(images[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position==3&&canSkip){
                        Intent  intent=new Intent(GuideActivity.this,MainActivity.class);
                        startActivity(intent);
                        UserUtils.SetIsFirst(GuideActivity.this);//设置为false
                        GuideActivity.this.finish();
                    }
                }
            });
            return convertView;
        }


        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_NONE;
        }
        @Override
        public int getCount() {
            return images.length;
        }

    };

    @Override
    public void setData(Object o, int id) {
        switch (id) {
            case 0:
                CreateVisitorEntity   entity= (CreateVisitorEntity) o;
                if(entity.getCode()==0){  //游客创建成功
                  canSkip=true;
                 //游客创建成功  保存信息
                 if(entity.getData()!=null){
                     UserUtils.saveVisitor(this,entity.getData());
                     UserUtils.setUserIdintify(this,entity.getData().getUsertype());
                     LogUtils.e("createYk","游客创建成功，并成功保存信息");
                 }else{
                     LogUtils.e("createYk","游客创建成功，保存信息失败");
                 }
                }else if(entity.getCode()==101){
                    dialog.show();
                }else if(entity.getCode()==102){
                    dialog.show();
                     LogUtils.e("createYk","签名错误");
                }else if(entity.getCode()==10499){
                    dialog.show();
                    LogUtils.e("createYk","未知错误");
                }
                break;
        }
    }

    @Override
    public void showLoading(String msg) {
        dialog.show();
    }

    // 创建游客失败 时   重新请求或者退出
    AlertDialog dialog;
    private TextView tvTitle;
    private AppCompatButton btnNegative;
    private AppCompatButton btnPositive;
    public void  showRestarDig(){
        dialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).create();
        dialog.setCancelable(false);
        View view =mLayoutInflater.inflate(R.layout.item_showdialog_upload_confirm, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnNegative = (AppCompatButton)view. findViewById(R.id.btn_negative);
        btnPositive = (AppCompatButton)view. findViewById(R.id.btn_positive);
        btnNegative.setText("退出");
        btnPositive.setText("重试");
        tvTitle.setText("网络出现问题鸟~");
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuideActivity.this.finish();
                dialog.cancel();
            }
        });
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrecenter.createYk(bean);
                dialog.cancel();
            }
        });
               dialog.setView(view);
    }

}
