package com.aiwue.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwue.R;
import com.aiwue.base.AiwueConfig;
import com.aiwue.base.BaseMvpFragment;
import com.aiwue.controller.UserController;
import com.aiwue.iview.IMeView;
import com.aiwue.model.Notice;
import com.aiwue.model.User;
import com.aiwue.presenter.MePresenter;
import com.aiwue.ui.activity.LoginActivity;
import com.aiwue.ui.activity.ProfileMyInfomationActivity;
import com.aiwue.ui.activity.ProfileMyPublishActivity;
import com.aiwue.ui.activity.ProfileMycollectionActivity;
import com.aiwue.ui.activity.ProfileMyfriendActivity;
import com.aiwue.ui.activity.ProfilesettingActivity;
import com.aiwue.ui.view.HeaderZoomLayout;
import com.aiwue.utils.ConstantValue;
import com.aiwue.utils.ImageLoaderUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
/**
 * Created by Administrator on 2016/11/17 0017.
 */
public class MeFragment extends BaseMvpFragment<MePresenter> implements IMeView {
    @BindView(R.id.user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.txt_user_name)
    TextView userName;
    @BindView(R.id.member_level_textview)
    TextView memberLevel;
    @BindView(R.id.user_score_textview)
    TextView userScore;
    @BindView(R.id.note_count_textview)
    TextView noteCount;

    @BindView(R.id.zommLayout)
    HeaderZoomLayout zommLayout;
    @BindView(R.id.login_button)
    Button loginBtn;
    @BindView(R.id.content_lin)
    LinearLayout content_lin;
    @BindView(R.id.unlisted_lin)
    LinearLayout unlisted_lin;
    //加载布局
    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_me, null);
    }
    @Override
    protected void bindViews(View view) {ButterKnife.bind(this, rootView);}
    @Override
    protected void processLogic() {
//        txt_my_page_message.setText(txt_my_page_message.getClass().getSimpleName());
//        zommLayout.setHeaderView(ivBg);
        showContent();
    }
    //订阅消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(Notice event) {
        if (event.type == ConstantValue.EVENTBUS_MESSENGE_LOGIN_LOGOUT)
            showContent();
    }
    //显示数据（登录状态/未登录状态）
    private void showContent(){
        User user = UserController.getInstance().getUser();
         if (user == null) {
             unlisted_lin.setVisibility(View.VISIBLE);
             content_lin.setVisibility(View.GONE);
             user = new User();
         }else {
             unlisted_lin.setVisibility(View.GONE);
             content_lin.setVisibility(View.VISIBLE);
         }
        ImageLoaderUtils.displayAvatar(AiwueConfig.AIWUE_API_PIC_URL + user.getHeadPicName(), (ImageView) userAvatar);
        userName.setText(user.getNickName());
        memberLevel.setText(user.getMemberLevel()+"");
        userScore.setText(user.getScore()+"");
        noteCount.setText(user.getStatusNum()+"");
    }
    //初始化一些方法
    @Override
    protected MePresenter createPresenter() {
        return new MePresenter(this);
    }
    //别的监听事件
    @Override
    protected void setListener() {
    }
    //自己的监听事件
    @OnClick({R.id.setting_button,R.id.login_button, R.id.user_avatar,R.id.myfriend_fragme_rela,R.id.mycollection_fragme_rela,
            R.id.myissue_fragme_rela,R.id.personal_information_fragme_rela})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_button:
                startActivity(new Intent(mContext, ProfilesettingActivity.class));
                break;
            case R.id.user_avatar:
                if (!UserController.getInstance().isLogined()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), ProfilesettingActivity.class));
                }
                break;
            case R.id.login_button:
                startActivity(new Intent(mContext, LoginActivity.class));
            case R.id.myfriend_fragme_rela:
                startActivity(new Intent(getActivity(), ProfileMyfriendActivity.class));
                break;
            case R.id.mycollection_fragme_rela:
                startActivity(new Intent(getActivity(), ProfileMycollectionActivity.class));
                break;
            case R.id.myissue_fragme_rela:
                startActivity(new Intent(getActivity(), ProfileMyPublishActivity.class));
               // startActivity(new Intent(getActivity(), StatusDetailActivity.class));
             //   startActivity(new Intent(getActivity(), PictureViewerActivity.class));
                break;
            case R.id.personal_information_fragme_rela:
                startActivity(new Intent(getActivity(), ProfileMyInfomationActivity.class));
                break;
        }
    }
}
