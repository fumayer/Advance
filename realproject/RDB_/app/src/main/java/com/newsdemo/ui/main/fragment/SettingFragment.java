package com.newsdemo.ui.main.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newsdemo.R;
import com.newsdemo.app.Constants;
import com.newsdemo.base.BaseFragment;
import com.newsdemo.base.contract.main.SettingContract;
import com.newsdemo.component.ACache;
import com.newsdemo.component.RxBus;
import com.newsdemo.model.bean.VersionBean;
import com.newsdemo.model.event.NightModeEvent;
import com.newsdemo.presenter.main.SettingPresenter;
import com.newsdemo.ui.main.activity.MainActivity;
import com.newsdemo.util.ShareUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jianqiang.hu on 2017/5/12.
 */

public class SettingFragment extends BaseFragment<SettingPresenter> implements  SettingContract.View, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.cb_setting_cache)
    AppCompatCheckBox cbSettingCache;
    @BindView(R.id.cb_setting_image)
    AppCompatCheckBox cbSettingImage;
    @BindView(R.id.cb_setting_night)
    AppCompatCheckBox cbSettingNight;
    @BindView(R.id.ll_setting_feedback)
    LinearLayout llSettingFeedback;
    @BindView(R.id.tv_setting_clear)
    TextView tvSettingClear;
    @BindView(R.id.ll_setting_clear)
    LinearLayout llSettingClear;
    @BindView(R.id.tv_setting_update)
    TextView tvSettingUpdate;
    @BindView(R.id.ll_setting_update)
    LinearLayout llSettingUpdate;



    private File cacheFile;
    private String versionName;
    private boolean isNull = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isNull=savedInstanceState==null;
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.ll_setting_feedback)
    void doFreeBack(){
        ShareUtil.sendEmail(mContext,"选择邮件客户端");
    }

    @OnClick(R.id.ll_setting_clear)
    void doClear(){
        ACache.deleteDir(cacheFile);
        tvSettingClear.setText(ACache.getCacheSize(cacheFile));
    }

    @OnClick(R.id.ll_setting_update)
    void doUpdate(){
        mPresentter.checkVersion(versionName);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initEventAndData() {
        cacheFile = new File(Constants.PATH_CACHE);
        tvSettingClear.setText(ACache.getCacheSize(cacheFile));
        cbSettingCache.setChecked(mPresentter.getAutoCacheState());
        cbSettingImage.setChecked(mPresentter.getNoImageState());
        cbSettingNight.setChecked(mPresentter.getNightModeState());

        cbSettingCache.setOnCheckedChangeListener(this);
        cbSettingImage.setOnCheckedChangeListener(this);
        cbSettingNight.setOnCheckedChangeListener(this);

        try {
            PackageManager pm =getActivity().getPackageManager();
            PackageInfo pi=pm.getPackageInfo(getActivity().getPackageName(),PackageManager.GET_ACTIVITIES);
            versionName=pi.versionName;
            tvSettingUpdate.setText(String .format("当前版本号 v%s",versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showUpdateDialog(VersionBean bean) {
        StringBuilder content = new StringBuilder("版本号: v");
        content.append(bean.getCode());
        content.append("\r\n");
        content.append("版本大小: ");
        content.append(bean.getSize());
        content.append("\r\n");
        content.append("更新内容:\r\n");
        content.append(bean.getDes().replace("\\r\\n","\r\n"));
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setTitle("检测到新版本!");
        builder.setMessage(content);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Activity mActivity = getActivity();
                if (mActivity instanceof MainActivity) {
                    ((MainActivity) mActivity).checkPermissions();
                }
            }
        });
        builder.show();
    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.cb_setting_night:
                if (isNull){//防止夜间模式MainActivity执行reCreate后重复调用
                    mPresentter.setNightModeState(b);
                    NightModeEvent event=new NightModeEvent();
                    event.setNightMode(b);
                    RxBus.getDefault().post(event);
                }
                break;
            case R.id.cb_setting_image:
                mPresentter.setNoImageState(b);
                break;
            case R.id.cb_setting_cache:
                mPresentter.AutoCacheState(b);
                break;
        }
    }
}
